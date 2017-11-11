package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.repositories.LocationRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import uk.co.jbrunton.droneforecast.viewmodels.LocationListViewModel
import uk.co.jbrunton.droneforecast.viewmodels.OverallStatusViewModel
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel

/**
 * Created by jjbrunton on 31/10/2017.
 */
@Module
class ViewModelModule {
    @Provides
    fun provideWeatherGridViewModel(weatherRepo: ForecastRepository,
                                    overallStatusViewModel: OverallStatusViewModel,
                                    widgetProvider: WidgetProvider,
                                    settingsService: SettingsService,
                                    locationRepository: LocationRepository): WeatherGridViewModel {
        return WeatherGridViewModel(weatherRepo, overallStatusViewModel, widgetProvider, settingsService, locationRepository)
    }

    @Provides
    fun providesOverallStatusViewModel(settingsService: SettingsService) : OverallStatusViewModel {
        return OverallStatusViewModel(ArrayList(), settingsService)
    }

    @Provides
    fun providesLocationListViewModel(locationRepository: LocationRepository, forecastRepository: ForecastRepository, settingsService: SettingsService) : LocationListViewModel {
        return LocationListViewModel(locationRepository, forecastRepository, settingsService)
    }
}