package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.rxkotlin.zip
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.repositories.LocationRepository
import uk.co.jbrunton.droneforecast.services.SettingsService

/**
 * Created by jamie on 11/11/2017.
 */
class LocationListViewModel(private val locationRepository: LocationRepository, private val forecastRepository: ForecastRepository, private val settingsService: SettingsService) {
    val locationStream: Observable<List<LocationItemViewModel>>
        get() = this.locationRepository.locations.flatMap{
            if(it.isEmpty()) {
                Observable.just(ArrayList<LocationItemViewModel>())
            } else {

                it.map {
                    val location = it
                    this.forecastRepository.getCurrentForecast(settingsService.weatherApiKey, location.lat!!.toFloat(), location.lng!!.toFloat()).map {
                        LocationItemViewModel(location, it, settingsService)
                    }.toObservable()
                }.zip {
                    it
                }
            }
        }

    fun saveLocation(name: String, lat: Double, lng: Double) {
        if (this.locationRepository.getLocationByLatLng(lat, lng) == null) {
            this.locationRepository.saveLocation(lat, lng, name)
        }
    }

    fun removeLocation(location: LocationItemViewModel) {
        this.locationRepository.removeLocation(location.lat!!, location.lng!!)
    }
}