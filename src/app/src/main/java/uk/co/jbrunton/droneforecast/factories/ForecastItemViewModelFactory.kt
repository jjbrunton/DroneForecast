package uk.co.jbrunton.droneforecast.factories

import uk.co.jbrunton.droneforecast.services.SettingsService
import android.preference.PreferenceManager
import android.content.SharedPreferences
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.viewmodels.ForecastItemViewModel
import javax.measure.Measure
import javax.measure.unit.NonSI
import javax.measure.unit.SI


/**
 * Created by jamie on 31/10/2017.
 */
class ForecastItemViewModelFactory(private val settingsService: SettingsService) {

    fun createViewModels(response: ForecastResponse): List<ForecastItemViewModel> {
        var items = ArrayList<ForecastItemViewModel>()

        val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
        val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
        val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
        val windUnit = windToConverter.convert(Measure.valueOf(response.currently.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))

        items.add(ForecastItemViewModel("%.1f".format(windUnit) + settingsService.getWindUnit().toString().toUpperCase(), "Wind Speed", WidgetType.TEXT, {
            when {
                (response.currently.windSpeed in 0.0..5.0) -> WeatherStatus.OK
                (response.currently.windSpeed in 5.0..10.0) -> WeatherStatus.WARNING
                else -> WeatherStatus.PROBLEM
            }
        }))

        val windGustUnit = windToConverter.convert(Measure.valueOf(response.currently.windGust, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
        items.add(ForecastItemViewModel("%.1f".format(windGustUnit) + settingsService.getWindUnit().toString().toUpperCase(), "Wind Gusts", WidgetType.TEXT, {
            WeatherStatus.OK
        }))

        items.add(ForecastItemViewModel("%.1f".format(response.currently.cloudCover*100)+"%", "Cloud Cover", WidgetType.TEXT, {
            WeatherStatus.OK
        }))


        val temperature = temperatureToConverter.convert(Measure.valueOf(response.currently.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
        items.add(ForecastItemViewModel("%.0f".format(temperature) + settingsService.getTemperatureUnit().toString().toUpperCase(), "Temperature", WidgetType.TEXT, {
            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherStatus.PROBLEM
                temperature > this.settingsService.getMaxTemperature() -> WeatherStatus.PROBLEM
                else -> WeatherStatus.OK
            }
        }))

        items.add(ForecastItemViewModel(response.currently.windBearing.toString(), "Wind Bearing", WidgetType.TEXT, {
            WeatherStatus.OK
        }))

        items.add(ForecastItemViewModel(response.currently.icon, "Weather", WidgetType.IMAGE, {
            WeatherStatus.OK
        }))


        val visibility = distanceToConverter.convert(Measure.valueOf(response.currently.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
        items.add(ForecastItemViewModel("%.1f".format(visibility) + settingsService.getDistanceUnit().toString().toUpperCase(), "Visibility", WidgetType.TEXT, {
            WeatherStatus.OK
        }))

        val dewPoint = temperatureToConverter.convert(Measure.valueOf(response.currently.dewPoint, SI.CELSIUS).doubleValue(SI.CELSIUS))
        items.add(ForecastItemViewModel("%.0f".format(dewPoint) + settingsService.getTemperatureUnit().toString().toUpperCase(), "Dew Point", WidgetType.TEXT, {
            WeatherStatus.OK
        }))

        return items
    }
}