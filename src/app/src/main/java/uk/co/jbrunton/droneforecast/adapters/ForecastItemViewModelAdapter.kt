package uk.co.jbrunton.droneforecast.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import uk.co.jbrunton.droneforecast.viewholders.ForecastItemViewHolder
import uk.co.jbrunton.droneforecast.viewmodels.ForecastItemViewModel
import android.view.LayoutInflater
import android.view.View
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.WidgetType


/**
 * Created by jjbrunton on 31/10/2017.
 */
class ForecastItemViewModelAdapter(private val items: List<ForecastItemViewModel>) : RecyclerView.Adapter<ForecastItemViewHolder>() {

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].widgetType.ordinal;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemViewHolder {
        var type = WidgetType.values()[viewType]
        var itemView: View
        if (type == WidgetType.TEXT) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_grid_item, parent, false)
        } else {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_grid_item_image, parent, false)
        }

        return ForecastItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForecastItemViewHolder, position: Int) {
        holder.setViewModel(this.items[position])
    }
}