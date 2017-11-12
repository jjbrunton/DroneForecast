package uk.co.jbrunton.droneforecast.factories

import android.content.Context
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.*

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WeatherWidgetFactory(private val settingsService: SettingsService, private val forecastRepository: ForecastRepository) {

    fun create(context: Context) : List<WeatherWidget> {
        var list = ArrayList<WeatherWidget>()
        list.add(CloudCoverWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(WindBearingWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(WeatherIconWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(VisibilityWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(RainPredictionWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(WindSpeedWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(WindGustWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        list.add(TemperatureForecastWidget(this.forecastRepository.forecastStream, this.settingsService, context))
        return list
    }
}