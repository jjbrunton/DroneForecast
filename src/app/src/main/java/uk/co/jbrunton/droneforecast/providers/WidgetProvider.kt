package uk.co.jbrunton.droneforecast.providers

import android.content.Context
import uk.co.jbrunton.droneforecast.factories.WeatherWidgetFactory
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WidgetProvider(private val weatherWidgetFactory: WeatherWidgetFactory, private val context: Context) {
    val widgets: List<WeatherWidget> = this.weatherWidgetFactory.create(this.context)

}