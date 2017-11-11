package uk.co.jbrunton.droneforecast.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.RxFragment
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.activities.LocationSearchActivity
import uk.co.jbrunton.droneforecast.application.DFApplication

/**
 * Created by jamie on 10/11/2017.
 */
class LocationsListFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        DFApplication.graph.inject(this)
        var view = inflater.inflate(R.layout.fragment_location_list, container, false)
        var fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            startActivity(Intent(this.activity, LocationSearchActivity::class.java))
        }

        return view;
    }
}