package uk.co.jbrunton.droneforecast.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.WidgetType
import android.view.MotionEvent
import android.support.v4.view.MotionEventCompat
import android.view.View.OnTouchListener
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import uk.co.jbrunton.droneforecast.viewholders.WeatherWidgetViewHolder
import uk.co.jbrunton.droneforecast.widgets.WeatherWidgetViewModel


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherWidgetViewModelAdapter(private var widgets: MutableList<WeatherWidgetViewModel>, private val dragListener: OnDragStartListener) : RecyclerView.Adapter<WeatherWidgetViewHolder>(), ItemTouchHelperAdapter, WidgetDismissListener {
    private val itemsRemovedSubject: PublishSubject<WeatherWidgetViewModel> = PublishSubject.create()
    private val itemsReorderedSubject: PublishSubject<List<WeatherWidgetViewModel>> = PublishSubject.create()
    private var contextItem = -1

    val itemsRemovedStream: Observable<WeatherWidgetViewModel>
        get() = this.itemsRemovedSubject
    val itemsReorderedStream: Observable<List<WeatherWidgetViewModel>>
        get() = this.itemsReorderedSubject

    override fun getItemCount(): Int {
        return this.widgets.size
    }

    override fun getItemViewType(position: Int): Int {
        return this.widgets[position].widgetType.ordinal;
    }

    fun setItems(items: MutableList<WeatherWidgetViewModel>) {
        this.widgets = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWidgetViewHolder {
        var type = WidgetType.values()[viewType]
        var itemView: View
        if (type == WidgetType.TEXT) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_grid_item, parent, false)
        } else {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_grid_item_image, parent, false)
        }

        return WeatherWidgetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherWidgetViewHolder, position: Int) {
        holder.setViewModel(this.widgets[position])
        holder.dismissListener = this
        holder.dragImage.setOnTouchListener(OnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                dragListener.onStartDrag(holder)
            }
            false
        })

        holder.itemView.setOnLongClickListener {
            this.contextItem = position
            false
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val prev = this.widgets.removeAt(fromPosition)
        this.widgets.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, prev)
        this.notifyItemMoved(fromPosition, toPosition)
        this.itemsReorderedSubject.onNext(this.widgets)
    }

    override fun onItemDismiss(position: Int) {
        this.itemsRemovedSubject.onNext(this.widgets[position])
    }

    override fun onDismissWidget(widgetViewModel: WeatherWidgetViewModel) {
        this.onItemDismiss(this.widgets.indexOf(widgetViewModel))
    }

    override fun onContextDismiss() {
        this.onItemDismiss(this.contextItem)
    }
}