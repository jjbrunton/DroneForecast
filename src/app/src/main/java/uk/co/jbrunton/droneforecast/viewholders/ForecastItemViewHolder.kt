package uk.co.jbrunton.droneforecast.viewholders

import android.graphics.Color
import android.os.Debug
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.forecast_grid_item.view.*
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.extensions.toWeatherIcon
import uk.co.jbrunton.droneforecast.viewmodels.ForecastItemViewModel

/**
 * Created by jjbrunton on 31/10/2017.
 */
class ForecastItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.item_text)
    val data: TextView? = itemView.findViewById(R.id.data_text)
    val cardView: CardView = itemView.findViewById(R.id.card_view)
    val dataImage: ImageView? = itemView.findViewById(R.id.data_image)
    private lateinit var viewModel: ForecastItemViewModel

    fun setViewModel(viewModel: ForecastItemViewModel) {
        this.viewModel = viewModel
        this.title.text = this.viewModel.text
        this.data?.text = this.viewModel.dataPoint
        this.dataImage?.background = this.itemView.context.getDrawable(this.viewModel.dataPoint.toWeatherIcon())
        this.itemView.setOnClickListener(View.OnClickListener { Log.e("ForecastItemViewHolder", this.viewModel.isAcceptable.invoke().toString())
            if(!this.viewModel.isAcceptable.invoke()) {
            this.cardView.setCardBackgroundColor(Color.RED)}})
    }

}