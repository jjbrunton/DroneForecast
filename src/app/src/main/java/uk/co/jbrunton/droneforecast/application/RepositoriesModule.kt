package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.jbrunton.droneforecast.repositories.ForecastRepository
import javax.inject.Singleton
import okhttp3.OkHttpClient



/**
 * Created by jamie on 30/10/2017.
 */
@Module
class RepositoriesModule {
    @Provides
    @Singleton
    fun provideForecastRepository(context: Context): ForecastRepository {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        val interceptor = Interceptor({chain ->
            val origResponse = chain.proceed(chain.request())

            val maxAge = 600
            origResponse.newBuilder().header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
        })

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.darksky.net")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(ForecastRepository::class.java)
    }
}