package uk.co.jbrunton.droneforecast.factories

import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.widgets.TemperatureWidget
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel

/**
 * Created by jjbrunton on 01/11/2017.
 */
class ForecastWidgetViewModelFactory(val settingsService: SettingsService, val forecastRepository: ForecastRepository) {

    fun create() : List<WeatherWidgetViewModel> {
        var list = ArrayList<WeatherWidgetViewModel>()
        list.add(TemperatureWidget(this.forecastRepository.forecastStream, this.settingsService))
        return list
    }
}