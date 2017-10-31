package uk.co.jbrunton.droneforecast.viewmodels

import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WidgetType

/**
 * Created by jjbrunton on 31/10/2017.
 */
class ForecastItemViewModel(private val data: String, private val title: String, private val type: WidgetType, val isAcceptable: () -> Boolean) {
    val text: String
    get() = this.title

    val dataPoint: String
        get() = this.data

    val widgetType: WidgetType
        get() = this.type
}