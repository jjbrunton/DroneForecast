package uk.co.jbrunton.droneforecast.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.location.places.ui.PlacePicker
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.RxFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.activities.MainActivity
import uk.co.jbrunton.droneforecast.activities.WeatherViewActivity
import uk.co.jbrunton.droneforecast.adapters.LocationViewModelAdapter
import uk.co.jbrunton.droneforecast.adapters.OnItemClickListener
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.viewmodels.LocationItemViewModel
import uk.co.jbrunton.droneforecast.viewmodels.LocationListViewModel
import javax.inject.Inject


/**
 * Created by jamie on 10/11/2017.
 */
class LocationsListFragment : RxFragment(), OnItemClickListener<LocationItemViewModel> {
    @Inject lateinit var viewModel: LocationListViewModel
    private var adapter: LocationViewModelAdapter? = null
    private lateinit var locationsList: RecyclerView
    lateinit private var placeholderContainer: RelativeLayout
    lateinit private var contentViewContainer: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        DFApplication.graph.inject(this)
        var view = inflater.inflate(R.layout.fragment_location_list, container, false)
        this.locationsList = view.findViewById(R.id.locations)
        this.contentViewContainer = view.findViewById(R.id.content_container)
        this.placeholderContainer = view.findViewById(R.id.placeholder_container)
        var fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            val PLACE_PICKER_REQUEST = 105
            val builder = PlacePicker.IntentBuilder()

            this.startActivityForResult(builder.build(this.activity as MainActivity), PLACE_PICKER_REQUEST)
        }

        if (this.adapter == null) {
            adapter = LocationViewModelAdapter(this.viewModel.locationStream, this as LifecycleProvider<Any>, this)
        }

        this.viewModel.locationStream.observeOn(AndroidSchedulers.mainThread()).bindToLifecycle(this).subscribe {
            if (it.isNotEmpty()) {
                this.contentViewContainer.visibility = View.VISIBLE
                this.placeholderContainer.visibility = View.GONE
            } else {
                this.contentViewContainer.visibility = View.GONE
                this.placeholderContainer.visibility = View.VISIBLE
            }
        }



        var layoutManager = LinearLayoutManager(this.activity)
        locationsList.layoutManager = layoutManager
        var itemDivider = DividerItemDecoration(
                locationsList.context,
                layoutManager.orientation
        )
        locationsList.addItemDecoration(itemDivider)
        locationsList.adapter = adapter
        locationsList.adapter.notifyDataSetChanged()
        locationsList.setHasFixedSize(true)

        this.adapter!!.itemsRemovedStream.bindToLifecycle(this).subscribe {
            this.viewModel.removeLocation(it)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 105) {
            if (resultCode === Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this.activity)
                val toastMsg = String.format("Place: %s", place.latLng)
                Toast.makeText(this.activity, toastMsg, Toast.LENGTH_LONG).show()
                this.viewModel.saveLocation(place.name.toString(), place.latLng.latitude, place.latLng.longitude)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        this.adapter!!.onContextDismiss()
        return super.onContextItemSelected(item)
    }

    override fun onItemSelected(item: LocationItemViewModel) {
        var intent = Intent(this.activity, WeatherViewActivity::class.java)
        intent.putExtra("locationId", item.id)
        this.startActivity(intent)
    }
}