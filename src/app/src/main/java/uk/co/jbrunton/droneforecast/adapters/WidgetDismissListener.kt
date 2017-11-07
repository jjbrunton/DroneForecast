package uk.co.jbrunton.droneforecast.adapters

import uk.co.jbrunton.droneforecast.widgets.WeatherWidget

/**
 * Created by jamie on 02/11/2017.
 */
interface WidgetDismissListener {
    fun onContextDismiss()
    fun onDismissWidget(widget: WeatherWidget)
}