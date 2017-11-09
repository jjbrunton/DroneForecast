package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.extensions.toHeadingString
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel

/**
 * Created by jamie on 02/11/2017.
 */
class WindBearingWidget(private val forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var dataTextView: TextView
    private var bearingIcon: ImageView

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_wind_bearing, null)
        this.dataTextView = this.view.findViewById(R.id.bearing_text)
        this.bearingIcon = this.view.findViewById(R.id.widget_image)
    }

    override val widgetKey: String
        get() = "windbearing"

    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_wind_bearing_title)

    override val widgetProvidesIndication: Boolean
        get() = false

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            WeatherStatus.OK
        }

    override val widgetView: Observable<View>
        get() = forecastStream.observeOn(AndroidSchedulers.mainThread()).map { this.renderWidgetView(it.currently) }

    private fun renderWidgetView(forecast: ForecastItemResponse): View {
        this.dataTextView.text = forecast.windBearing.toHeadingString()
        this.bearingIcon.rotation = forecast.windBearing.toFloat()
        return this.view
    }
}