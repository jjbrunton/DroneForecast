package uk.co.jbrunton.droneforecast.viewmodels

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherGridViewModel(private val forecastRepo: ForecastRepository,
                           private val overalStatusViewModel: OverallStatusViewModel,
                           private val widgetProvider: WidgetProvider,
                           private val settingsService: SettingsService) {
    private var canAddWidgetSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private var storedWidgets: ArrayList<WeatherWidget> = ArrayList()
    private var activeWidgetsSubject: BehaviorSubject<List<WeatherWidget>> = BehaviorSubject.create()
    val overallText: Observable<String>
        get() = this.overalStatusViewModel.statusText

    val canAddWidget: Observable<Boolean>
        get() = this.canAddWidgetSubject

    val allWidgets: List<WeatherWidget>
        get() = this.widgetProvider.widgets

    val activeWidgets: Observable<List<WeatherWidget>>
        get() = this.activeWidgetsSubject

    init {
        var storedWidgetKeys = this.settingsService.getWidgets()
        var widgets = storedWidgetKeys.map {
            var key = it
            this.widgetProvider.widgets.first { it.widgetKey == key }
        }

        this.storedWidgets.addAll(widgets)
        this.activeWidgetsSubject.onNext(this.storedWidgets)
        this.canAddWidgetSubject.onNext(this.getAvailableWidgets().any())
        this.overalStatusViewModel.updateWidgets(this.storedWidgets)
    }

    fun refreshData(lat: Float, lon: Float): Observable<List<WeatherWidget>> {
        return this.forecastRepo.getCurrentForecast("404453f2e4b605045cc0e7c06a7efd95", lat, lon).subscribeOn(Schedulers.io()).flatMap{
            this.activeWidgets
        }
    }

    fun addWidgetByTitle(widgetTitle: String) {
        if (this.storedWidgets.firstOrNull { it.widgetTitle == widgetTitle} == null) {
            var widget = this.widgetProvider.widgets.first { it.widgetTitle == widgetTitle }
            this.storedWidgets.add(widget)
            this.settingsService.saveWidgets(this.storedWidgets.map { it.widgetKey })
            this.activeWidgetsSubject.onNext(this.storedWidgets)
        }

        this.overalStatusViewModel.updateWidgets(this.storedWidgets)
        this.canAddWidgetSubject.onNext(this.getAvailableWidgets().any())
    }

    fun widgetOrderChanged(widgetList: List<WeatherWidget>) {
        this.storedWidgets.clear()
        this.storedWidgets.addAll(widgetList)
        this.settingsService.saveWidgets(this.storedWidgets.map { it.widgetKey })
    }

    fun removeWidget(widget: WeatherWidget) {
        if (this.storedWidgets.firstOrNull { it.widgetKey == widget.widgetKey} != null) {
            this.storedWidgets.remove(widget)
            this.settingsService.saveWidgets(this.storedWidgets.map { it.widgetKey })
            this.activeWidgetsSubject.onNext(this.storedWidgets)
        }

        this.overalStatusViewModel.updateWidgets(this.storedWidgets)
        this.canAddWidgetSubject.onNext(this.getAvailableWidgets().any())
    }

    fun getAvailableWidgets() : List<WeatherWidget> {
        var activeKeys = storedWidgets.map { it.widgetKey }
        var availableKeys = this.allWidgets.map { it.widgetKey }

        return availableKeys.union(activeKeys).subtract(availableKeys.intersect(activeKeys)).map {
            var key = it
            this.widgetProvider.widgets.first { it.widgetKey == key } }
    }
}