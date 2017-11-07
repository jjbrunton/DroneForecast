package uk.co.jbrunton.droneforecast.providers

import uk.co.jbrunton.droneforecast.factories.WeatherWidgetFactory
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WidgetProvider(private val weatherWidgetFactory: WeatherWidgetFactory) {
    val widgets: List<WeatherWidget> = this.weatherWidgetFactory.create()

}