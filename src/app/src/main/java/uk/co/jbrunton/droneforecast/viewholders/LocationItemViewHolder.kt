package uk.co.jbrunton.droneforecast.viewholders

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.extensions.toWeatherIcon
import uk.co.jbrunton.droneforecast.viewmodels.LocationItemViewModel

/**
 * Created by jamie on 11/11/2017.
 */
class LocationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
    private var title:TextView = itemView.findViewById(R.id.location_name)
    private var windSpeed:TextView = itemView.findViewById(R.id.mini_weather_wind_speed)
    private var windSpeedIcon:ImageView = itemView.findViewById(R.id.mini_wind_speed_icon)
    private var weatherIcon:ImageView = itemView.findViewById(R.id.mini_weather_icon)
    private var weatherTemperature:TextView = itemView.findViewById(R.id.weather_overview_temperature)

    init {
        Log.e("LocationItemViewHolder", "*** INIT ***")
    }


    fun setViewModel(viewModel: LocationItemViewModel) {
        itemView.setOnCreateContextMenuListener(this)
        this.title.text = viewModel.name
        this.windSpeedIcon.rotation = viewModel.windBearing.toFloat()
        this.weatherIcon.setImageResource(viewModel.iconString.toWeatherIcon())
        this.weatherTemperature.text = viewModel.temperature
        this.windSpeed.text = viewModel.windSpeed
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu.add(0, v.getId(), 0, "Delete")
    }
}