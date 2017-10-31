package uk.co.jbrunton.droneforecast.extensions

import uk.co.jbrunton.droneforecast.R

/**
 * Created by jjbrunton on 31/10/2017.
 */
    fun String.toWeatherIcon(): Int {
        when(this) {
            "cloud" -> return R.drawable.ic_cloudy
            else -> return R.drawable.ic_clear_day
        }
    }
