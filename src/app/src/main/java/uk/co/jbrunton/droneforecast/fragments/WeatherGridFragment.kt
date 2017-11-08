package uk.co.jbrunton.droneforecast.fragments


import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_weather_grid.*
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.adapters.OnDragStartListener
import uk.co.jbrunton.droneforecast.adapters.WeatherWidgetViewModelAdapter
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.models.SimpleItemTouchHelperCallback
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Inject

class WeatherGridFragment : Fragment(), OnDragStartListener {
    @Inject lateinit var viewModel : WeatherGridViewModel
    lateinit var gridView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit private var overallText : TextView
    var adapter : WeatherWidgetViewModelAdapter? = null
    lateinit private var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        DFApplication.graph.inject(this)
        var view = inflater.inflate(R.layout.fragment_weather_grid, container, false)
        var fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            this.showWidgetPicker() }
        this.gridView = view.findViewById(R.id.weather_widget_list)
        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        this.overallText = view.findViewById(R.id.overall_text)
        registerForContextMenu(gridView)
        swipeRefreshLayout.setOnRefreshListener({
            this.refreshItems()
        })

        this.viewModel.activeWidgets.subscribe {
            if (this.adapter == null) {
                adapter = WeatherWidgetViewModelAdapter(it.toMutableList(), this)
                this.adapter!!.itemsRemovedStream.subscribe {
                    this.viewModel.removeWidget(it)
                }
                this.adapter!!.itemsReorderedStream.subscribe {
                    this.viewModel.widgetOrderChanged(it)
                }
            } else {
                this.adapter!!.setItems(it.toMutableList())
            }


            val numberOfColumns = 2
            gridView.layoutManager = GridLayoutManager(this.activity, numberOfColumns)
            gridView.adapter = adapter
            gridView.setHasFixedSize(true)
            val callback = SimpleItemTouchHelperCallback(this.adapter!!)
            itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this.gridView)
        }

        this.viewModel.canAddWidget.observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it) {
                this.fab.visibility = View.VISIBLE
            }
            else {
                this.fab.visibility = View.GONE
            }
        }


        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.refreshItems()

    }

    private fun refreshItems() {
        this.swipeRefreshLayout.isRefreshing = true
        this.viewModel.refreshData(50.892224999999996F, (-1.21005260000004).toFloat()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            this.swipeRefreshLayout.isRefreshing = false
        })

        this.viewModel.overallText.observeOn(AndroidSchedulers.mainThread()).subscribe {
            this.overallText.text = it
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    fun showWidgetPicker() {
        val builderSingle = AlertDialog.Builder(this.activity)
        builderSingle.setTitle(getString(R.string.dialog_add_widget))

        val arrayAdapter = ArrayAdapter<String>(this.activity, android.R.layout.select_dialog_item)
        this.viewModel.getAvailableWidgets().forEach { arrayAdapter.add(it.widgetTitle)}

        builderSingle.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter, DialogInterface.OnClickListener { dialog, which ->
            this.viewModel.addWidgetByTitle(arrayAdapter.getItem(which))
        })
        builderSingle.show()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        this.adapter!!.onContextDismiss()
        return super.onContextItemSelected(item)
    }

}