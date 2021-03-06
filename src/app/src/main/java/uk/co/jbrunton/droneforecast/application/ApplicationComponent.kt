package uk.co.jbrunton.droneforecast.application

import dagger.Component
import uk.co.jbrunton.droneforecast.activities.MainActivity
import javax.inject.Singleton

/**
 * Created by jamie on 30/10/2017.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class, RepositoriesModule::class))
interface ApplicationComponent {
    fun inject(application: DFApplication)

    fun inject(mainActivity: MainActivity)
}