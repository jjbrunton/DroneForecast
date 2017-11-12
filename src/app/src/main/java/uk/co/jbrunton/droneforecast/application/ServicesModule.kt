package uk.co.jbrunton.droneforecast.application

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
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

    @Provides
    @Singleton
    fun provideFirebaseAnalytis(context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }
}