package uk.co.jbrunton.droneforecast.widgets

import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WeatherIndication
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
abstract class WeatherWidgetViewModel(forecastStream: Observable<ForecastItemResponse>, settingsService: SettingsService) {
    abstract val widgetWeatherState: Observable<WeatherStatus>
    abstract val widgetKey: String
    abstract val widgetType: WidgetType
    abstract val widgetTitle: String
    abstract val widgetDataText: Observable<String>
    abstract val widgetProvidesIndication: Boolean
    abstract val widgetIndication: Observable<WeatherWarningViewModel>
}