package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jamie on 02/11/2017.
 */
class WindSpeedWidget(private val forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var dataTextView: TextView

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_single_text, null)
        this.dataTextView = this.view.findViewById(R.id.widget_text)
    }

    override val widgetKey: String
        get() = "windspeed"
    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_wind_speed_title)

    override val widgetProvidesIndication: Boolean
        get() = true
    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
            val windUnit = windToConverter.convert(Measure.valueOf(it.currently.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
            when {
                windUnit > this.settingsService.getMaxWindSpeed().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                windUnit > (this.settingsService.getMaxWindSpeed().toDouble()*0.9) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_wind_speed))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }
    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            WeatherStatus.OK
        }

    override val widgetView: Observable<View>
        get() = forecastStream.observeOn(AndroidSchedulers.mainThread()).map { this.renderWidgetView(it.currently) }

    private fun renderWidgetView(forecast: ForecastItemResponse): View {
        val windToConverter = SI.METERS_PER_SECOND.getConverterTo(settingsService.getWindUnit())
        val windUnit = windToConverter.convert(Measure.valueOf(forecast.windSpeed, SI.METRES_PER_SECOND).doubleValue(SI.METRES_PER_SECOND))
        this.dataTextView.text = "%.1f".format(windUnit) + settingsService.getWindUnit().toString().toUpperCase()
        return this.view
    }


}