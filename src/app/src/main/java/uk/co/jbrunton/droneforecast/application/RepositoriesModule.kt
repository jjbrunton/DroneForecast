package uk.co.jbrunton.droneforecast.application

import dagger.Module
import dagger.Provides
import io.realm.Realm
import uk.co.jbrunton.droneforecast.proxies.ForecastProxy
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.repositories.LocationRepository
import javax.inject.Singleton


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

    @Provides
    @Singleton
    fun provideLocationsRepository(): LocationRepository {
        return LocationRepository(Realm.getDefaultInstance())
    }
}