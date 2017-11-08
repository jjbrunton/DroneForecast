package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
import android.view.View
import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.models.*
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
interface WeatherWidget {
    val widgetWeatherState: Observable<WeatherStatus>
    val widgetKey: String
    val widgetTitle: String
    val widgetProvidesIndication: Boolean
    val widgetIndication: Observable<WeatherWarningViewModel>
    fun renderWidgetContent(container: View, context: Context)
}