package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import uk.co.jbrunton.droneforecast.services.SettingsService
import javax.inject.Singleton

/**
 * Created by jamie on 30/10/2017.
 */
@Module
class ServicesModule {
    @Provides
    @Singleton
    fun provideSettingsService(context: Context): SettingsService {
        return SettingsService(context)
    }
}