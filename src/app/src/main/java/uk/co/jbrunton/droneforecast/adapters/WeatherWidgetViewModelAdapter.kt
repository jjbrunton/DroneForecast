package uk.co.jbrunton.droneforecast.adapters

import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.models.WidgetType
import uk.co.jbrunton.droneforecast.viewholders.WeatherWidgetViewHolder
import uk.co.jbrunton.droneforecast.widgets.WeatherWidget


/**
 * Created by jjbrunton on 31/10/2017.
 */
class WeatherWidgetViewModelAdapter(private var widgets: MutableList<WeatherWidget>, private val dragListener: OnDragStartListener, private val lifecycleProvider: LifecycleProvider<Any>) : RecyclerView.Adapter<WeatherWidgetViewHolder>(), ItemTouchHelperAdapter, WidgetDismissListener {
    private val itemsRemovedSubject: PublishSubject<WeatherWidget> = PublishSubject.create()
    private val itemsReorderedSubject: PublishSubject<List<WeatherWidget>> = PublishSubject.create()
    private var contextItem = -1

    val itemsRemovedStream: Observable<WeatherWidget>
        get() = this.itemsRemovedSubject
    val itemsReorderedStream: Observable<List<WeatherWidget>>
        get() = this.itemsReorderedSubject

    override fun getItemCount(): Int {
        return this.widgets.size
    }

    fun setItems(items: MutableList<WeatherWidget>) {
        this.widgets = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWidgetViewHolder {
        var type = WidgetType.values()[viewType]
        var itemView: View
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_grid_item, parent, false)
        return WeatherWidgetViewHolder(itemView, this.lifecycleProvider)
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

    override fun onDismissWidget(widget: WeatherWidget) {
        this.onItemDismiss(this.widgets.indexOf(widget))
    }

    override fun onContextDismiss() {
        this.onItemDismiss(this.contextItem)
    }
}