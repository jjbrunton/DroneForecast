package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Singleton

/**
 * Created by jjbrunton on 31/10/2017.
 */
@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideWeatherGridViewModel(weatherRepo: ForecastRepository): WeatherGridViewModel {
        return WeatherGridViewModel(weatherRepo)
    }
}