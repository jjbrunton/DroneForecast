package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository

/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherGridViewModel(private val forecastRepo: ForecastRepository) {

    fun refreshData(lat: Float, lon: Float): Observable<List<ForecastItemViewModel>> {
        return this.forecastRepo.getForecastForLocation("404453f2e4b605045cc0e7c06a7efd95", lat, lon).subscribeOn(Schedulers.io()).flatMap{
            var items = ArrayList<ForecastItemViewModel>()
            items.add(ForecastItemViewModel(it.currently.windSpeed.toString() + "MPH", "Wind Speed", WidgetType.TEXT, {
                it.currently.windSpeed > 10
            }))
            items.add(ForecastItemViewModel(it.currently.windGust.toString() + "MPH", "Wind Gusts", WidgetType.TEXT, {
                true
            }))
            items.add(ForecastItemViewModel((it.currently.cloudCover*100).toString() + "%", "Cloud Cover", WidgetType.TEXT, {
                true
            }))
            items.add(ForecastItemViewModel(((it.currently.temperature - 32)*5/9).toString() + "C", "Temperature", WidgetType.TEXT, {
                true
            }))
            items.add(ForecastItemViewModel(it.currently.windBearing.toString(), "Wind Bearing", WidgetType.TEXT, {
                true
            }))
            items.add(ForecastItemViewModel(it.currently.icon, "Weather", WidgetType.IMAGE, {
                true
            }))
            items.add(ForecastItemViewModel(((it.currently.dewPoint - 32)*5/9).toString() + "C", "Dew Point", WidgetType.TEXT, {
                true
            }))

            Observable.just(items)
        }
    }
}