package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.factories.WeatherWidgetFactory
import uk.co.jbrunton.droneforecast.providers.WidgetProvider
import javax.inject.Singleton

/**
 * Created by jjbrunton on 01/11/2017.
 */
@Module
class ProvidersModule {

    @Provides
    @Singleton
    fun provideWidgetProvider(weatherWidgetFactory: WeatherWidgetFactory, context: Context) : WidgetProvider {
        return WidgetProvider(weatherWidgetFactory, context)
    }
}