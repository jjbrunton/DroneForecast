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
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jjbrunton on 01/11/2017.
 */
class TemperatureWidget(private var forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var dataTextView: TextView

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_single_text, null)
        this.dataTextView = this.view.findViewById(R.id.widget_text)
    }

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(it.currently.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherStatus.PROBLEM
                temperature > this.settingsService.getMaxTemperature() -> WeatherStatus.PROBLEM
                else -> WeatherStatus.OK
            }
        }

    override val widgetProvidesIndication: Boolean
        get() = true

    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
            val temperature = temperatureToConverter.convert(Measure.valueOf(it.currently.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
            when {
                temperature < this.settingsService.getMinTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                temperature > this.settingsService.getMaxTemperature() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_temperature))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }

    override val widgetKey: String
        get() = "temperature"

    override val widgetTitle: String
        get() = settingsService.getStringValue(R.string.widget_temperature_title)

    override val widgetView: Observable<View>
        get() = forecastStream.observeOn(AndroidSchedulers.mainThread()).map { this.renderWidgetView(it.currently) }

    private fun renderWidgetView(forecast: ForecastItemResponse): View {
        val temperatureToConverter = SI.CELSIUS.getConverterTo(settingsService.getTemperatureUnit())
        val temperature = temperatureToConverter.convert(Measure.valueOf(forecast.temperature, SI.CELSIUS).doubleValue(SI.CELSIUS))
        this.dataTextView.text = "%.0f".format(temperature) + settingsService.getTemperatureUnit().toString().toUpperCase()
        return this.view
    }
}