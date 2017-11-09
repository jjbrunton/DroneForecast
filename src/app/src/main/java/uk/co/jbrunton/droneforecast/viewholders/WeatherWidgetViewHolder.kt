package uk.co.jbrunton.droneforecast.viewholders

import android.app.ActionBar
import android.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.extensions.toWeatherIcon
import uk.co.jbrunton.droneforecast.models.WeatherStatus
import android.content.DialogInterface
import android.os.Build
import android.view.ContextMenu
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import uk.co.jbrunton.droneforecast.adapters.ItemTouchHelperViewHolder
import uk.co.jbrunton.droneforecast.adapters.WidgetDismissListener
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget
import java.security.KeyStore


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherWidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder, View.OnCreateContextMenuListener {
    val title: TextView = itemView.findViewById(R.id.item_text)
    val widgetContent: RelativeLayout = itemView.findViewById(R.id.widget_content)
    val cardView: CardView = itemView.findViewById(R.id.card_view)
    val dragImage: ImageView = itemView.findViewById(R.id.drag_image)
    private lateinit var viewModel: WeatherWidget
    lateinit var dismissListener: WidgetDismissListener
    private var disposeBag = CompositeDisposable()

    fun setViewModel(viewModel: WeatherWidget) {
        this.disposeBag.dispose()
        this.disposeBag = CompositeDisposable()
        this.viewModel = viewModel
        this.title.text = this.viewModel.widgetTitle
        itemView.setOnCreateContextMenuListener(this)

        if(viewModel.widgetProvidesIndication) {
            this.disposeBag.add(this.viewModel.widgetIndication.observeOn(AndroidSchedulers.mainThread()).subscribe {
                when (it.weatherState) {
                    WeatherStatus.OK -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetNeutral))
                    WeatherStatus.WARNING -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetWarning))
                    WeatherStatus.PROBLEM -> this.cardView.setCardBackgroundColor(this.itemView.resources.getColor(R.color.widgetBad))
                }

                var explanationViewModel = it
                this.itemView.setOnClickListener {
                    if (explanationViewModel.canExplain) {
                        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AlertDialog.Builder(this.itemView.context, android.R.style.Theme_Material_Dialog_Alert)
                        } else {
                            AlertDialog.Builder(this.itemView.context)
                        }
                        builder.setTitle(this.viewModel.widgetTitle)
                                .setMessage(explanationViewModel.message)
                                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                                    // continue with delete
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                    }
                }
            })
        }

        this.disposeBag.addAll(this.viewModel.widgetView.observeOn(AndroidSchedulers.mainThread()).subscribe {
            this.widgetContent.removeAllViews()
            if (it.parent != null) {
                var parent = it.parent as ViewGroup
                parent.removeView(it)
            }
            it.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            this.widgetContent.addView(it)
        })
    }

    override fun onItemSelected() {
    }

    override fun onItemClear() {
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu.setHeaderTitle("Edit Widget");
        menu.add(0, v.getId(), 0, "Delete")
    }

}