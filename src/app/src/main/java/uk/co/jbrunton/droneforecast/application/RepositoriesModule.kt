package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import uk.co.jbrunton.droneforecast.proxies.ForecastProxy
import javax.inject.Singleton
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository


/**
 * Created by jamie on 30/10/2017.
 */
@Module
class RepositoriesModule {
    @Provides
    @Singleton
    fun provideForecastRepository(forecastProxy: ForecastProxy): ForecastRepository {
        return ForecastRepository(forecastProxy)
    }
}