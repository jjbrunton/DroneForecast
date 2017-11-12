package uk.co.jbrunton.droneforecast.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import kotlinx.android.synthetic.main.app_bar_main.*
import uk.co.jbrunton.droneforecast.R


/**
 * Created by jjbrunton on 10/11/2017.
 */
class LocationSearchActivity: AppCompatActivity() {
    lateinit var mapView: MapView
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app_bar_location_search)
        setSupportActionBar(toolbar)
        // Gets the MapView from the XML layout and creates it
        this.mapView = this.findViewById(R.id.map)
        this.mapView.onCreate(savedInstanceState)

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync {
            this.map = it
            map.uiSettings.isMyLocationButtonEnabled = false
            map.isMyLocationEnabled = true
        }

    }

    override fun onResume() {
        this.mapView.onResume()
        super.onResume()
    }
}