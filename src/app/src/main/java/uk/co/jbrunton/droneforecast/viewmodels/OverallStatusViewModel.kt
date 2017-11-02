package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.OverallStatus
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.services.SettingsService

/**
 * Created by jjbrunton on 01/11/2017.
 */
class OverallStatusViewModel(private var forecastWidgets: List<ForecastWidgetViewModel>,
                             private var settingsService: SettingsService) {
    private var statusStream: BehaviorSubject<OverallStatus> = BehaviorSubject.create()

    val status: Observable<OverallStatus>
        get() = this.statusStream

    val statusText: Observable<String>
        get() {
            return this.statusStream.map {
                when (it) {
                    OverallStatus.OK -> this.settingsService.getStringValue(R.string.overall_status_ok)
                    OverallStatus.GOOD -> this.settingsService.getStringValue(R.string.overall_status_good)
                    OverallStatus.BAD -> this.settingsService.getStringValue(R.string.overall_status_bad)
                    OverallStatus.SEVERE -> this.settingsService.getStringValue(R.string.overall_status_severe)
                }
            }
        }

    init {
        this.statusStream.onNext(OverallStatus.GOOD)
    }

    fun updateWidgets(widgets: List<ForecastWidgetViewModel>) {
        this.forecastWidgets = widgets
        this.updateOverallStatus()
    }

    private fun updateOverallStatus() {
        var newStatus = OverallStatus.GOOD
        this.forecastWidgets.forEach {
            val result = it.isAcceptable.invoke()
            if (result.weatherState == WeatherStatus.WARNING && OverallStatus.BAD.ordinal > newStatus.ordinal) {
                newStatus = OverallStatus.BAD
            } else if (result.weatherState == WeatherStatus.PROBLEM && OverallStatus.SEVERE.ordinal > newStatus.ordinal) {
                newStatus = OverallStatus.SEVERE
            }
        }
        this.statusStream.onNext(newStatus)
    }
}