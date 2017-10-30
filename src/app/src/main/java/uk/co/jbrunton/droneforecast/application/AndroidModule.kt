package uk.co.jbrunton.droneforecast.application

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jamie on 30/10/2017.
 */
@Module
class AndroidModule(private val application: Application) {

    /**
     * Allow the application context to be injected but require that it be annotated with [ ][ForApplication] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

}