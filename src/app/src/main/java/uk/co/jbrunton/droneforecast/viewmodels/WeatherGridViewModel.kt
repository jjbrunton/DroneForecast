package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.rxkotlin.toSingle
import io.reactivex.schedulers.Schedulers
import uk.co.jbrunton.droneforecast.factories.ForecastItemViewModelFactory
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import javax.measure.Measure
import javax.measure.converter.UnitConverter
import javax.measure.unit.NonSI.MILE
import javax.measure.unit.SI.KILOMETER


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherGridViewModel(private val forecastRepo: ForecastRepository, private val forecastItemViewModelFactory: ForecastItemViewModelFactory) {

    fun refreshData(lat: Float, lon: Float): Observable<List<ForecastItemViewModel>> {
        return this.forecastRepo.getForecastForLocation("404453f2e4b605045cc0e7c06a7efd95", lat, lon).subscribeOn(Schedulers.io()).flatMap{
            Observable.just(this.forecastItemViewModelFactory.createViewModels(it))
        }
    }
}