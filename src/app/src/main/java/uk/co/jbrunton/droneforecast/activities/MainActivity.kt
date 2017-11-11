package uk.co.jbrunton.droneforecast.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import uk.co.jbrunton.droneforecast.fragments.LocationsListFragment
import uk.co.jbrunton.droneforecast.fragments.SettingsFragment
import uk.co.jbrunton.droneforecast.fragments.WeatherGridFragment
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var contentFrame: FrameLayout
    lateinit var weatherFragment: WeatherGridFragment
    lateinit var settingsFragment: SettingsFragment
    lateinit var locationsFragment: LocationsListFragment


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
        this.locationsFragment = LocationsListFragment()

        var transaction = this.fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, this.weatherFragment)
        transaction.commit()

        this.requestPermissionsFromUser()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun requestPermissionsFromUser() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
                var transaction = this.fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, this.locationsFragment)
                transaction.commit()
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
