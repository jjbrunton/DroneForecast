package uk.co.jbrunton.droneforecast.application

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.jbrunton.droneforecast.proxies.ForecastProxy
import javax.inject.Singleton

/**
 * Created by jjbrunton on 01/11/2017.
 */
@Module
class ProxiesModule {

    @Provides
    @Singleton
    fun provideForecastRepository(context: Context): ForecastProxy {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        val interceptor = Interceptor({chain ->
            val origResponse = chain.proceed(chain.request())

            val maxAge = 6000
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(ForecastProxy::class.java)
    }
}