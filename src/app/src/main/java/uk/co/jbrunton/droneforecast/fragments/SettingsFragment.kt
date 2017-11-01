package uk.co.jbrunton.droneforecast.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.Fragment
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.services.SettingsService
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject lateinit var settingsService : SettingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DFApplication.graph.inject(this)
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onPause() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}