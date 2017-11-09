package uk.co.jbrunton.droneforecast.repositories

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.proxies.ForecastProxy

/**
 * Created by jjbrunton on 01/11/2017.
 */
class ForecastRepository(private val forecastProxy: ForecastProxy) {
    private val forecastSubject: BehaviorSubject<ForecastResponse> = BehaviorSubject.create()
    val forecastStream: Observable<ForecastResponse>
        get() = this.forecastSubject

    fun getCurrentForecast(key: String, lat: Float, lon: Float) : Observable<ForecastResponse> {
        this.forecastProxy.getForecastForLocation(key, lat, lon).subscribeOn(Schedulers.io()).subscribe { forecast -> this.forecastSubject.onNext(forecast) }

        return forecastStream
    }
}