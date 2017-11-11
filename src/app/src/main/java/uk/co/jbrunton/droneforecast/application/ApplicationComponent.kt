package uk.co.jbrunton.droneforecast.application

import dagger.Component
import uk.co.jbrunton.droneforecast.activities.MainActivity
import uk.co.jbrunton.droneforecast.activities.WeatherViewActivity
import uk.co.jbrunton.droneforecast.fragments.LocationsListFragment
import uk.co.jbrunton.droneforecast.fragments.SettingsFragment
import javax.inject.Singleton

/**
 * Created by jamie on 30/10/2017.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class, RepositoriesModule::class, ViewModelModule::class, ServicesModule::class, FactoriesModule::class, ProxiesModule::class, ProvidersModule::class))
interface ApplicationComponent {
    fun inject(application: DFApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(weatherViewActivity: WeatherViewActivity)

    fun inject(settingsFragment: SettingsFragment)

    fun inject(locationsListFragment: LocationsListFragment)
}