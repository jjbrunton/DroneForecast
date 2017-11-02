package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.ForecastItemViewModelFactory
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.OverallStatusViewModel
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel

/**
 * Created by jjbrunton on 31/10/2017.
 */
@Module
class ViewModelModule {
    @Provides
    fun provideWeatherGridViewModel(weatherRepo: ForecastRepository,
                                    forecastItemFactory: ForecastItemViewModelFactory,
                                    overallStatusViewModel: OverallStatusViewModel,
                                    widgetProvider: WidgetProvider,
                                    settingsService: SettingsService): WeatherGridViewModel {
        return WeatherGridViewModel(weatherRepo, forecastItemFactory, overallStatusViewModel, widgetProvider, settingsService)
    }

    @Provides
    fun providesOverallStatusViewModel(settingsService: SettingsService) : OverallStatusViewModel {
        return OverallStatusViewModel(ArrayList(), settingsService)
    }
}