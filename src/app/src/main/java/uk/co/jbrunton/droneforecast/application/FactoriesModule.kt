package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.ForecastItemViewModelFactory
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
}