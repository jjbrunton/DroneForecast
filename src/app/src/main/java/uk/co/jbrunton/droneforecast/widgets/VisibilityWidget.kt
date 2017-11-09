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
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jamie on 02/11/2017.
 */
class VisibilityWidget(private val forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var dataTextView: TextView

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_single_text, null)
        this.dataTextView = this.view.findViewById(R.id.widget_text)
    }

    override val widgetKey: String
        get() = "visibilitywidget"

    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_visibility_title)

    override val widgetProvidesIndication: Boolean
        get() = true

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
            val visibility = distanceToConverter.convert(Measure.valueOf(it.currently.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
            when {
                visibility < this.settingsService.getMinVisibility().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_visibility))
                visibility < (this.settingsService.getMinVisibility().toDouble()*1.1) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_visibility))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
            val visibility = distanceToConverter.convert(Measure.valueOf(it.currently.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
            when {
                visibility < this.settingsService.getMinVisibility().toDouble() -> WeatherStatus.PROBLEM
                visibility < (this.settingsService.getMinVisibility().toDouble()*1.1) -> WeatherStatus.WARNING
                else -> WeatherStatus.OK
            }
        }

    override val widgetView: Observable<View>
        get() = forecastStream.map { this.renderWidgetView(it.currently) }

    private fun renderWidgetView(forecast: ForecastItemResponse): View {
        val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
        val visibility = distanceToConverter.convert(Measure.valueOf(forecast.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
        this.dataTextView.text = "%.1f".format(visibility) + settingsService.getDistanceUnit().toString().toUpperCase()
        return this.view
    }
}