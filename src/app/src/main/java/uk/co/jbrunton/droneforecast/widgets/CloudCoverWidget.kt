package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
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
class CloudCoverWidget(val forecastStream: Observable<ForecastItemResponse>, val settingsService: SettingsService) : WeatherWidget {

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map { WeatherStatus.OK }

    override val widgetKey: String
        get() = "cloudcover"

    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_cloud_cover_title)

    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map {
            "%.1f".format(it.cloudCover*100)+"%"
        }
    override val widgetProvidesIndication: Boolean
        get() = false

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = TODO("not implemented")

    override fun renderWidgetContent(container: View, context:Context) {
        var textView = TextView(context)
        textView.text = "%.1f".format(it.cloudCover*100)+"%"
    }
}