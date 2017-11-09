package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jamie on 02/11/2017.
 */
class CloudCoverWidget(private val forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var dataTextView: TextView

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_cloud_cover, null)
        this.dataTextView = this.view.findViewById(R.id.cloud_cover_text)
    }

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map { WeatherStatus.OK }

    override val widgetKey: String
        get() = "cloudcover"

    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_cloud_cover_title)

    override val widgetProvidesIndication: Boolean
        get() = false

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = TODO("not implemented")

    override val widgetView: Observable<View>
        get() = forecastStream.map { this.renderWidgetView(it.currently) }

    private fun renderWidgetView(forecast: ForecastItemResponse): View {
        this.dataTextView.text = "%.1f".format(forecast.cloudCover*100)+"%"
        return this.view
    }
}