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
 * Created by jjbrunton on 01/11/2017.
 */
class TemperatureWidget(private var forecastStream: Observable<ForecastItemResponse>, private val settingsService: SettingsService) : WeatherWidget {
    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(it.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherStatus.PROBLEM
                temperature > this.settingsService.getMaxTemperature() -> WeatherStatus.PROBLEM
                else -> WeatherStatus.OK
            }
        }

    override val widgetProvidesIndication: Boolean
        get() = true

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(it.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                temperature > this.settingsService.getMaxTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }

    override val widgetKey: String
        get() = "temperature"

    override val widgetType: WidgetType
        get() = WidgetType.TEXT

    override val widgetTitle: String
        get() = settingsService.getStringValue(R.string.widget_temperature_title)

    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(it.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            "%.0f".format(temperature) + settingsService.getTemperatureUnit().toString().toUpperCase()
        }
}