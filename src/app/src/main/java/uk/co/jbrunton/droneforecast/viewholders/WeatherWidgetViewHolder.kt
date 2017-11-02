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
import uk.co.jbrunton.droneforecast.viewmodels.ForecastWidgetViewModel
import android.content.DialogInterface
import android.os.Build
import android.view.MotionEvent
import android.support.v4.view.MotionEventCompat
import android.view.View.OnTouchListener
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.forecast_grid_item.view.*
import uk.co.jbrunton.droneforecast.adapters.ItemTouchHelperViewHolder
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
    val title: TextView = itemView.findViewById(R.id.item_text)
    val data: TextView? = itemView.findViewById(R.id.data_text)
    val cardView: CardView = itemView.findViewById(R.id.card_view)
    val dataImage: ImageView? = itemView.findViewById(R.id.data_image)
    val statusImage: ImageView = itemView.findViewById(R.id.status_image)
    private lateinit var viewModel: WeatherWidgetViewModel

    fun setViewModel(viewModel: WeatherWidgetViewModel) {
        this.viewModel = viewModel
        this.title.text = this.viewModel.widgetTitle
        this.viewModel.widgetDataText.observeOn(AndroidSchedulers.mainThread()).subscribe {
            this.data?.text = it
            this.dataImage?.background = this.itemView.context.getDrawable(it.toWeatherIcon())
        }


        this.viewModel.widgetIndication.observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it.weatherState) {
                WeatherStatus.OK -> statusImage.setImageDrawable(this.itemView.context.getDrawable(R.drawable.ic_tick))
                WeatherStatus.WARNING -> statusImage.setImageDrawable(this.itemView.context.getDrawable(R.drawable.ic_warning))
                WeatherStatus.PROBLEM -> statusImage.setImageDrawable(this.itemView.context.getDrawable(R.drawable.ic_cross))
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

    override fun onItemSelected() {
    }

    override fun onItemClear() {
    }

}