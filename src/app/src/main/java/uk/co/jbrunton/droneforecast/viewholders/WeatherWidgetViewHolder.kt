package uk.co.jbrunton.droneforecast.viewholders

import android.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.extensions.toWeatherIcon
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import android.content.DialogInterface
import android.os.Build
import android.view.ContextMenu
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.jbrunton.droneforecast.adapters.ItemTouchHelperViewHolder
import uk.co.jbrunton.droneforecast.adapters.WidgetDismissListener
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget
import java.security.KeyStore


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder, View.OnCreateContextMenuListener {
    val title: TextView = itemView.findViewById(R.id.item_text)
    val data: TextView? = itemView.findViewById(R.id.data_text)
    val cardView: CardView = itemView.findViewById(R.id.card_view)
    val dataImage: ImageView? = itemView.findViewById(R.id.data_image)
    val dragImage: ImageView = itemView.findViewById(R.id.drag_image)
    val lineChart: LineChart? = itemView.findViewById(R.id.line_chart)
    private lateinit var viewModel: WeatherWidget
    lateinit var dismissListener: WidgetDismissListener

    fun setViewModel(viewModel: WeatherWidget) {
        this.viewModel = viewModel
        this.title.text = this.viewModel.widgetTitle
        this.viewModel.widgetDataText.observeOn(AndroidSchedulers.mainThread()).subscribe {
            this.data?.text = it
            this.dataImage?.setImageDrawable(this.itemView.context.getDrawable(it.toWeatherIcon()))
        }

        itemView.setOnCreateContextMenuListener(this)

        when(this.viewModel.widgetType) {
            WidgetType.LINECHART -> this.setLineChartMode()
        }

        if(viewModel.widgetProvidesIndication) {
            this.viewModel.widgetIndication.observeOn(AndroidSchedulers.mainThread()).subscribe {
                when (it.weatherState) {
                    WeatherStatus.OK -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetNeutral))
                    WeatherStatus.WARNING -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetWarning))
                    WeatherStatus.PROBLEM -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetBad))
                }

                var explanationViewModel = it
                this.itemView.setOnClickListener {
                    if (explanationViewModel.canExplain) {
                        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AlertDialog.Builder(this.itemView.context, android.R.style.Theme_Material_Dialog_Alert)
                        } else {
                            AlertDialog.Builder(this.itemView.context)
                        }
                        builder.setTitle(this.viewModel.widgetTitle)
                                .setMessage(explanationViewModel.message)
                                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                                    // continue with delete
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                    }
                }
            }
        }
    }

    private fun setLineChartMode() {
        var entries = ArrayList<Entry>()
        this.viewModel.widgetGraphData
    }

    override fun onItemSelected() {
    }

    override fun onItemClear() {
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu.setHeaderTitle("Edit Widget");
        menu.add(0, v.getId(), 0, "Delete")
    }

}