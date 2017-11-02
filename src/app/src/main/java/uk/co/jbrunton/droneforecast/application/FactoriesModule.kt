package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.ForecastItemViewModelFactory
import uk.co.jbrunton.droneforecast.factories.ForecastWidgetViewModelFactory
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
    fun provideForecastItemViewModelFactory(settingsService: SettingsService): ForecastItemViewModelFactory {
        return ForecastItemViewModelFactory(settingsService)
    }

    @Provides
    @Singleton
    fun provideForecastWidgetViewModelFactory(settingsService: SettingsService, forecastRepository: ForecastRepository) : ForecastWidgetViewModelFactory {
        return ForecastWidgetViewModelFactory(settingsService, forecastRepository)
    }
}