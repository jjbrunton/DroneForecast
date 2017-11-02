package uk.co.jbrunton.droneforecast.factories

import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.ForecastWidgetViewModel
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI


/**
 * Created by jamie on 31/10/2017.
 */
class ForecastItemViewModelFactory(private val settingsService: SettingsService) {

    fun createViewModels(response: ForecastResponse): List<ForecastWidgetViewModel> {
        var items = ArrayList<ForecastWidgetViewModel>()

        val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
        val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
        val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
        val windUnit = windToConverter.convert(Measure.valueOf(response.currently.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))

        items.add(ForecastWidgetViewModel("%.1f".format(windUnit) + settingsService.getWindUnit().toString().toUpperCase(), "Wind Speed", WidgetType.TEXT, {
var maxRequired = this.settingsService.getMaxWindSpeed()
            when {
                windUnit > this.settingsService.getMaxWindSpeed().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                windUnit > (this.settingsService.getMaxWindSpeed().toDouble()*0.9) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }))

        val windGustUnit = windToConverter.convert(Measure.valueOf(response.currently.windGust, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
        items.add(ForecastWidgetViewModel("%.1f".format(windGustUnit) + settingsService.getWindUnit().toString().toUpperCase(), "Wind Gusts", WidgetType.TEXT, {
            WeatherWarningViewModel(WeatherStatus.OK)
        }))

        items.add(ForecastWidgetViewModel("%.1f".format(response.currently.cloudCover*100)+"%", "Cloud Cover", WidgetType.TEXT, {
            WeatherWarningViewModel(WeatherStatus.OK)
        }))

        var maxRequired = this.settingsService.getMinTemperature()
        val temperature = temperatureToConverter.convert(Measure.valueOf(response.currently.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
        items.add(ForecastWidgetViewModel("%.0f".format(temperature) + settingsService.getTemperatureUnit().toString().toUpperCase(), "Temperature", WidgetType.TEXT, {

            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                temperature > this.settingsService.getMaxTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }))

        items.add(ForecastWidgetViewModel(response.currently.windBearing.toString(), "Wind Bearing", WidgetType.TEXT, {
            WeatherWarningViewModel(WeatherStatus.OK)
        }))

        items.add(ForecastWidgetViewModel(response.currently.icon, "Weather", WidgetType.IMAGE, {
            WeatherWarningViewModel(WeatherStatus.OK)
        }))


        val visibility = distanceToConverter.convert(Measure.valueOf(response.currently.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
        items.add(ForecastWidgetViewModel("%.1f".format(visibility) + settingsService.getDistanceUnit().toString().toUpperCase(), "Visibility", WidgetType.TEXT, {
            when {
                visibility < this.settingsService.getMinVisibility().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_visibility))
                visibility < (this.settingsService.getMinVisibility().toDouble()*1.1) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_visibility))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }))

        val dewPoint = temperatureToConverter.convert(Measure.valueOf(response.currently.dewPoint, SI.CELSIUS).doubleValue(SI.CELSIUS))
        items.add(ForecastWidgetViewModel("%.0f".format(dewPoint) + settingsService.getTemperatureUnit().toString().toUpperCase(), "Dew Point", WidgetType.TEXT, {
            WeatherWarningViewModel(WeatherStatus.OK)
        }))

        return items
    }
}