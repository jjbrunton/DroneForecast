package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import uk.co.jbrunton.droneforecast.factories.ForecastItemViewModelFactory
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherGridViewModel(private val forecastRepo: ForecastRepository,
                           private val forecastItemViewModelFactory: ForecastItemViewModelFactory,
                           private val overalStatusViewModel: OverallStatusViewModel,
                           private val widgetProvider: WidgetProvider,
                           private val settingsService: SettingsService) {
    private var storedWidgets: ArrayList<WeatherWidgetViewModel> = ArrayList()
    private var activeWidgetsSubject: BehaviorSubject<List<WeatherWidgetViewModel>> = BehaviorSubject.create()
    val overallText: Observable<String>
        get() = this.overalStatusViewModel.statusText

    val allWidgets: List<WeatherWidgetViewModel>
        get() = this.widgetProvider.widgets

    val activeWidgets: Observable<List<WeatherWidgetViewModel>>
        get() = this.activeWidgetsSubject

    init {
        var storedWidgetKeys = this.settingsService.getWidgets().toList()
        var widgets = storedWidgetKeys.map {
            var key = it
            this.widgetProvider.widgets.first { it.widgetKey == key }
        }
        this.storedWidgets.addAll(widgets)
        this.activeWidgetsSubject.onNext(this.storedWidgets)
    }

    fun refreshData(lat: Float, lon: Float): Observable<List<WeatherWidgetViewModel>> {
        return this.forecastRepo.getCurrentForecast("404453f2e4b605045cc0e7c06a7efd95", lat, lon).subscribeOn(Schedulers.io()).flatMap{
            this.activeWidgets
        }
    }

    fun addWidgetByTitle(widgetTitle: String) {
        if (this.storedWidgets.firstOrNull { it.widgetTitle == widgetTitle} == null) {
            var widget = this.widgetProvider.widgets.first { it.widgetTitle == widgetTitle }
            this.storedWidgets.add(widget)
            this.settingsService.saveWidgets(this.storedWidgets.map { it.widgetKey }.toSet())
            this.activeWidgetsSubject.onNext(this.storedWidgets)
        }
    }

    fun removeWidget(widget: WeatherWidgetViewModel) {
        if (this.storedWidgets.firstOrNull { it.widgetKey == widget.widgetKey} != null) {
            this.storedWidgets.remove(widget)
            this.settingsService.saveWidgets(this.storedWidgets.map { it.widgetKey }.toSet())
            this.activeWidgetsSubject.onNext(this.storedWidgets)
        }
    }

    fun getAvailableWidgets() : List<WeatherWidgetViewModel> {
        var activeKeys = storedWidgets.map { it.widgetKey }
        var availableKeys = this.allWidgets.map { it.widgetKey }

        return availableKeys.union(activeKeys).subtract(availableKeys.intersect(activeKeys)).map {
            var key = it
            this.widgetProvider.widgets.first { it.widgetKey == key } }
    }
}