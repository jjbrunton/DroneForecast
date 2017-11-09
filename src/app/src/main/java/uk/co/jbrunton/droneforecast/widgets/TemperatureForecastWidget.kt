package uk.co.jbrunton.droneforecast.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData







/**
 * Created by jamie on 09/11/2017.
 */
class TemperatureForecastWidget(private val forecastStream: Observable<ForecastResponse>, private val settingsService: SettingsService, private val context: Context) : WeatherWidget {
    private var view: View
    private var chart: LineChart

    init {
        var inflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.view = inflater.inflate(R.layout.widget_temperature_forecast, null)
        this.chart = this.view.findViewById(R.id.temperature_chart)
    }

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            WeatherStatus.OK
        }

    override val widgetProvidesIndication: Boolean
        get() = false

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
        get() = "temperatureforecast"

    override val widgetTitle: String
        get() = settingsService.getStringValue(R.string.widget_temperature_forecast_title)

    override val widgetView: Observable<View>
        get() = forecastStream.map { this.renderWidgetView(it) }

    private fun renderWidgetView(forecast: ForecastResponse): View {
        var entries = ArrayList<Entry>()
        forecast.hourly.data.forEach {
            entries.add(Entry(it.temperature, it.time.toFloat()))
        }
        val dataSet = LineDataSet(entries, "")
        dataSet.setCircleColor(android.R.color.white)
        dataSet.color = android.R.color.white
        dataSet.setDrawValues(false)
        dataSet.setLineWidth(2f)
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        val lineData = LineData(dataSet)
        lineData.setDrawValues(false)

        this.chart.data = lineData
        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)



        this.chart.legend.isEnabled = false
        // remove axis
        val leftAxis = chart.axisLeft
        leftAxis.isEnabled = false
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.isEnabled = false
        this.chart.invalidate()
        return this.view
    }
}