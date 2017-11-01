package uk.co.jbrunton.droneforecast.fragments


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.adapters.ForecastItemViewModelAdapter
import uk.co.jbrunton.droneforecast.application.DFApplication
import uk.co.jbrunton.droneforecast.viewmodels.ForecastItemViewModel
import uk.co.jbrunton.droneforecast.viewmodels.WeatherGridViewModel
import javax.inject.Inject
import android.support.v7.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import android.support.v4.widget.SwipeRefreshLayout

class WeatherGridFragment : Fragment() {
    @Inject lateinit var viewModel : WeatherGridViewModel
    lateinit var gridView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        DFApplication.graph.inject(this)
        var view = inflater.inflate(R.layout.fragment_weather_grid, container, false)
        this.gridView = view.findViewById(R.id.weather_widget_list)
        this.swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener({
            this.refreshItems()
        })

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.refreshItems()
    }

    private fun refreshItems() {
        this.swipeRefreshLayout.isRefreshing = true
        this.viewModel.refreshData(50.892224999999996F, (-1.21005260000004).toFloat()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            var adapter = ForecastItemViewModelAdapter(it)
            val numberOfColumns = 3
            gridView.layoutManager = GridLayoutManager(this.activity, numberOfColumns)
            gridView.adapter = adapter
            this.swipeRefreshLayout.isRefreshing = false
        })
    }

}