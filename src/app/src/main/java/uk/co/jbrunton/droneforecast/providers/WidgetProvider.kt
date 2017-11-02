package uk.co.jbrunton.droneforecast.providers

import uk.co.jbrunton.droneforecast.factories.ForecastWidgetViewModelFactory
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WidgetProvider(val forecastWidgetViewModelFactory: ForecastWidgetViewModelFactory) {
    val widgets: List<WeatherWidgetViewModel>

    init {
        this.widgets = this.forecastWidgetViewModelFactory.create()
    }
}