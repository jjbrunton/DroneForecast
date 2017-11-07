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
interface WeatherWidget {
    val widgetWeatherState: Observable<WeatherStatus>
    val widgetKey: String
    val widgetType: WidgetType
    val widgetTitle: String
    val widgetDataText: Observable<String>
    val widgetProvidesIndication: Boolean
    val widgetIndication: Observable<WeatherWarningViewModel>
}