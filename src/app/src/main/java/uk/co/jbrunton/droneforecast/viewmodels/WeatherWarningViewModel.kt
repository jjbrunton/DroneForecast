package uk.co.jbrunton.droneforecast.viewmodels

import uk.co.jbrunton.droneforecast.models.WeatherStatus

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WeatherWarningViewModel(private val weatherStatus: WeatherStatus, private val explanation:String = "") {
    val isInErrorState: Boolean
        get() = this.weatherStatus == WeatherStatus.PROBLEM

    val weatherState: WeatherStatus
        get() = this.weatherStatus

    val canExplain: Boolean
        get() = this.explanation != ""


    val message: String
        get() = this.explanation
}