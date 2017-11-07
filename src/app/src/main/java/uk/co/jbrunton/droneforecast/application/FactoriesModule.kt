package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.WeatherWidgetFactory
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import javax.inject.Singleton

/**
 * Created by jamie on 30/10/2017.
 */
@Module
class FactoriesModule {
    @Provides
    @Singleton
    fun provideForecastWidgetFactory(settingsService: SettingsService, forecastRepository: ForecastRepository) : WeatherWidgetFactory {
        return WeatherWidgetFactory(settingsService, forecastRepository)
    }
}