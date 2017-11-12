package uk.co.jbrunton.droneforecast.widgets

import android.view.View
import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
interface WeatherWidget {
    val widgetWeatherState: Observable<WeatherStatus>
    val widgetView: Observable<View>
    val widgetKey: String
    val widgetTitle: String
    val widgetProvidesIndication: Boolean
    val widgetIndication: Observable<WeatherWarningViewModel>
}