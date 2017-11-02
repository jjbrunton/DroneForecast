package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.OverallStatus
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
class OverallStatusViewModel(private var forecastWidgets: List<WeatherWidgetViewModel>,
                             private var settingsService: SettingsService) {
    private var statusStream: BehaviorSubject<OverallStatus> = BehaviorSubject.create()
    private var disposeBag: CompositeDisposable = CompositeDisposable()

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

    fun updateWidgets(widgets: List<WeatherWidgetViewModel>) {
        this.disposeBag.dispose()
        this.disposeBag = CompositeDisposable()
        this.forecastWidgets = widgets
        var statusStreams = ArrayList<Observable<WeatherStatus>>()
        widgets.forEach {
            statusStreams.add(it.widgetWeatherState)
        }

        this.disposeBag.add(Observable.zip(statusStreams, { status ->
            var maxStatus = status.maxBy {
                var status = it as WeatherStatus
                status.ordinal
            }

            var maxCount = status.count { it == maxStatus }

            this.statusStream.onNext(
                when {
                    maxStatus == WeatherStatus.OK -> OverallStatus.GOOD
                    maxStatus == WeatherStatus.WARNING -> OverallStatus.BAD
                    maxStatus == WeatherStatus.PROBLEM && (maxCount > 1) -> OverallStatus.SEVERE
                    maxStatus ==  WeatherStatus.PROBLEM -> OverallStatus.BAD
                    else -> OverallStatus.OK
                }
            )

            Observable.just(true)
        }).subscribe { })

    }
}