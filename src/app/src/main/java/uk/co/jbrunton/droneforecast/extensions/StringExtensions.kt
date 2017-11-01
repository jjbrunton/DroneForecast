package uk.co.jbrunton.droneforecast.extensions

import uk.co.jbrunton.droneforecast.R

/**
 * Created by jjbrunton on 31/10/2017.
 */
    fun String.toWeatherIcon(): Int {
        when(this) {
            "clear-day" -> return R.drawable.ic_clear_day
            "clear-night" -> return R.drawable.ic_clear_night
            "rain" -> return R.drawable.ic_rain
            "snow" -> return R.drawable.ic_snow
            "sleet" -> return R.drawable.ic_sleet
            "wind" -> return R.drawable.ic_wind
            "fog" -> return R.drawable.ic_fog
            "cloudy" -> return R.drawable.ic_cloudy
            "partly-cloudy-day" -> return R.drawable.ic_partly_cloudy_day
            "partly-cloudy-night" -> return R.drawable.ic_partly_cloudy_night
            else -> return R.drawable.ic_clear_day
        }
    }
