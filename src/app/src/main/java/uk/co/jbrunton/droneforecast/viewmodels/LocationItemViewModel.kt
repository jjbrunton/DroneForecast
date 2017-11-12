package uk.co.jbrunton.droneforecast.viewmodels

import android.util.Log
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.LocationEntity
import uk.co.jbrunton.droneforecast.services.SettingsService
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jamie on 11/11/2017.
 */
class LocationItemViewModel(private val locationEntity: LocationEntity, private val forecastResponse: ForecastResponse, private val settingsService: SettingsService) {

    init {
        Log.e("LocationItemViewModel", "*** LocationItemViewModel INIT ***")
    }

    val id: String?
        get() = this.locationEntity.locationId

    val name: String?
        get() = this.locationEntity.name

    val lat: Double?
        get() = this.locationEntity.lat

    val lng: Double?
        get() = this.locationEntity.lng

    val windBearing: Int
        get() = this.forecastResponse.currently.windBearing

    val iconString: String
        get() = this.forecastResponse.currently.icon

    val windSpeed: String
        get() {
            val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
            val windGustUnit = windToConverter.convert(Measure.valueOf(forecastResponse.currently.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
            return "%.1f".format(windGustUnit) + settingsService.getWindUnit().toString().toUpperCase()
        }

    val temperature: String
        get() {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(forecastResponse.currently.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            return "%.0f".format(temperature) + settingsService.getTemperatureUnit().toString().toUpperCase()
        }
}