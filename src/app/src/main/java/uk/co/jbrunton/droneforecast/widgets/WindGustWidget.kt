package uk.co.jbrunton.droneforecast.widgets

import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jamie on 02/11/2017.
 */
class WindGustWidget(val forecastStream: Observable<ForecastItemResponse>, val settingsService: SettingsService) : WeatherWidgetViewModel(forecastStream, settingsService) {
    override val widgetKey: String
        get() = "windgusts"
    override val widgetType: WidgetType
        get() = WidgetType.TEXT
    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_wind_gusts_title)
    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map {
            val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
            val windGustUnit = windToConverter.convert(Measure.valueOf(it.windGust, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
            "%.1f".format(windGustUnit) + settingsService.getWindUnit().toString().toUpperCase()
        }
    override val widgetProvidesIndication: Boolean
        get() = false
    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
            val windUnit = windToConverter.convert(Measure.valueOf(it.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
            when {
                windUnit > this.settingsService.getMaxWindSpeed().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                windUnit > (this.settingsService.getMaxWindSpeed().toDouble()*0.9) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }
    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
            val windUnit = windToConverter.convert(Measure.valueOf(it.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
            when {
                windUnit > this.settingsService.getMaxWindSpeed().toDouble() -> WeatherStatus.PROBLEM
                windUnit > (this.settingsService.getMaxWindSpeed().toDouble()*0.9) -> WeatherStatus.PROBLEM
                else -> WeatherStatus.OK
            }
        }
}