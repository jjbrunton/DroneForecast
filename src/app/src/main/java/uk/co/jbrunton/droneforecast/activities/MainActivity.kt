package uk.co.jbrunton.droneforecast.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.fragments.SettingsFragment
import uk.co.jbrunton.droneforecast.fragments.WeatherGridFragment
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var contentFrame: FrameLayout
    lateinit var weatherFragment: WeatherGridFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DFApplication.graph.inject(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        this.contentFrame = this.findViewById(R.id.content_frame)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        this.weatherFragment = WeatherGridFragment()
        this.settingsFragment = SettingsFragment()

        var transaction = this.fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, this.weatherFragment)
        transaction.commit()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                var transaction = this.fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, this.weatherFragment)
                transaction.commit()
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {
                var transaction = this.fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, this.settingsFragment)
                transaction.commit()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
