package uk.co.jbrunton.droneforecast.factories

import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.*

/**
 * Created by jjbrunton on 01/11/2017.
 */
class WeatherWidgetFactory(val settingsService: SettingsService, val forecastRepository: ForecastRepository) {

    fun create() : List<WeatherWidget> {
        var list = ArrayList<WeatherWidget>()
        list.add(TemperatureWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(WindSpeedWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(WindGustWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(CloudCoverWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(WindBearingWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(WeatherIconWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(VisibilityWidget(this.forecastRepository.forecastStream, this.settingsService))
        list.add(RainPredictionWidget(this.forecastRepository.forecastStream, this.settingsService))
        return list
    }
}