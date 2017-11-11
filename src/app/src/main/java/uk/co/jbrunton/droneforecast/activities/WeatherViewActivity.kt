package uk.co.jbrunton.droneforecast.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_weather_grid.*
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.adapters.OnDragStartListener
import uk.co.jbrunton.droneforecast.adapters.WeatherWidgetViewModelAdapter
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.models.SimpleItemTouchHelperCallback
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Inject

/**
 * Created by jamie on 11/11/2017.
 */
class WeatherViewActivity: RxAppCompatActivity(), OnDragStartListener {
    @Inject lateinit var viewModel : WeatherGridViewModel
    lateinit var gridView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit private var overallText : TextView
    var adapter : WeatherWidgetViewModelAdapter? = null
    lateinit private var itemTouchHelper: ItemTouchHelper
    lateinit private var placeholderContainer: RelativeLayout
    lateinit private var contentViewContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_view)
        setSupportActionBar(toolbar)
        this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar!!.setDisplayShowHomeEnabled(true)
        DFApplication.graph.inject(this)
        var fab: FloatingActionButton = this.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            this.showWidgetPicker() }
        this.gridView = this.findViewById(R.id.weather_widget_list)
        this.contentViewContainer = this.findViewById(R.id.content_view)
        this.placeholderContainer = this.findViewById(R.id.placeholder_container)
        this.swipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout)
        this.overallText = this.findViewById(R.id.overall_text)
        registerForContextMenu(gridView)
        swipeRefreshLayout.setOnRefreshListener({
            this.refreshItems()
        })

        this.viewModel.activeWidgets.bindToLifecycle(this).doOnDispose { Log.d("Subscription", "Unsubscribing") }.subscribe {
            if (it.isNotEmpty()) {
                this.contentViewContainer.visibility = View.VISIBLE
                this.placeholderContainer.visibility = View.GONE
            } else {
                this.contentViewContainer.visibility = View.GONE
                this.placeholderContainer.visibility = View.VISIBLE
            }

            if (this.adapter == null) {
                adapter = WeatherWidgetViewModelAdapter(it.toMutableList(), this, this as LifecycleProvider<Any>)
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
            gridView.layoutManager = GridLayoutManager(this, numberOfColumns)
            gridView.adapter = adapter
            gridView.adapter.notifyDataSetChanged()
            gridView.setHasFixedSize(true)
            val callback = SimpleItemTouchHelperCallback(this.adapter!!)
            itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this.gridView)
        }

        this.viewModel.canAddWidget.bindToLifecycle(this).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it) {
                this.fab.visibility = View.VISIBLE
            }
            else {
                this.fab.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.refreshItems()
    }

    private fun refreshItems() {
        if (this.intent.hasExtra("locationId")) {
            this.viewModel.loadData(this.intent.getStringExtra("locationId")).observeOn(AndroidSchedulers.mainThread()).subscribe({
            })

            this.viewModel.overallText.bindToLifecycle(this).observeOn(AndroidSchedulers.mainThread()).subscribe {
                this.overallText.text = it
            }
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    fun showWidgetPicker() {
        val builderSingle = AlertDialog.Builder(this)
        builderSingle.setTitle(getString(R.string.dialog_add_widget))

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}