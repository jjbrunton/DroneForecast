package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.ForecastWidgetViewModelFactory
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import javax.inject.Singleton

/**
 * Created by jjbrunton on 01/11/2017.
 */
@Module
class ProvidersModule {

    @Provides
    @Singleton
    fun provideWidgetProvider(forecastWidgetViewModelFactory: ForecastWidgetViewModelFactory) : WidgetProvider {
        return WidgetProvider(forecastWidgetViewModelFactory)
    }
}