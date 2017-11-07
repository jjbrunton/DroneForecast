package uk.co.jbrunton.droneforecast.widgets

import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jamie on 02/11/2017.
 */
class WeatherIconWidget(val forecastStream: Observable<ForecastItemResponse>, val settingsService: SettingsService) : WeatherWidget {
    override val widgetKey: String
        get() = "weathericon"
    override val widgetType: WidgetType
        get() = WidgetType.IMAGE
    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_weather_icon_title)
    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map { it.icon }
    override val widgetProvidesIndication: Boolean
        get() = false
    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            WeatherStatus.OK
        }
}