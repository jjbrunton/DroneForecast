package uk.co.jbrunton.droneforecast.widgets

import android.view.View
import android.widget.TextView
import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jamie on 02/11/2017.
 */
class RainPredictionWidget(val forecastStream: Observable<ForecastItemResponse>, val settingsService: SettingsService) : WeatherWidget {
    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map { WeatherStatus.OK }

    override val widgetKey: String
        get() = "rainprediction"

    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_rain_prediction_title)

    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map {
            "%.1f".format(it.precipProbability*100)+"%"
        }
    override val widgetProvidesIndication: Boolean
        get() = false

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun renderWidgetContent(container: View, context: Context) {
        var textView = TextView()
    }
}