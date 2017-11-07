package uk.co.jbrunton.droneforecast.widgets

import io.reactivex.Observable
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.ForecastItemResponse
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.WeatherWarningViewModel
import javax.measure.Measure
import javax.measure.unit.SI

/**
 * Created by jamie on 02/11/2017.
 */
class VisibilityWidget(val forecastStream: Observable<ForecastItemResponse>, val settingsService: SettingsService) : WeatherWidget {
    override val widgetKey: String
        get() = "visibilitywidget"
    override val widgetType: WidgetType
        get() = WidgetType.TEXT
    override val widgetTitle: String
        get() = this.settingsService.getStringValue(R.string.widget_visibility_title)
    override val widgetDataText: Observable<String>
        get() = this.forecastStream.map {
            val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
            val visibility = distanceToConverter.convert(Measure.valueOf(it.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
            "%.1f".format(visibility) + settingsService.getDistanceUnit().toString().toUpperCase()
        }
    override val widgetProvidesIndication: Boolean
        get() = true
    override val widgetIndication: Observable<WeatherWarningViewModel>
        get() = this.forecastStream.map {
            val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
            val visibility = distanceToConverter.convert(Measure.valueOf(it.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
            when {
                visibility < this.settingsService.getMinVisibility().toDouble() -> WeatherWarningViewModel(WeatherStatus.PROBLEM, this.settingsService.getStringValue(R.string.warning_message_visibility))
                visibility < (this.settingsService.getMinVisibility().toDouble()*1.1) -> WeatherWarningViewModel(WeatherStatus.WARNING, this.settingsService.getStringValue(R.string.warning_message_visibility))
                else -> WeatherWarningViewModel(WeatherStatus.OK)
            }
        }

    override val widgetWeatherState: Observable<WeatherStatus>
        get() = forecastStream.map {
            val distanceToConverter = SI.KILOMETER.getConverterTo(settingsService.getDistanceUnit())
            val visibility = distanceToConverter.convert(Measure.valueOf(it.visibility, SI.KILOMETER).doubleValue(SI.KILOMETER))
            when {
                visibility < this.settingsService.getMinVisibility().toDouble() -> WeatherStatus.PROBLEM
                visibility < (this.settingsService.getMinVisibility().toDouble()*1.1) -> WeatherStatus.WARNING
                else -> WeatherStatus.OK
            }
        }
}