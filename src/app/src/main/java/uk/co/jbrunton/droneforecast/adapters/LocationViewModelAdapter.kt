package uk.co.jbrunton.droneforecast.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import uk.co.jbrunton.droneforecast.R
import uk.co.jbrunton.droneforecast.viewholders.LocationItemViewHolder
import uk.co.jbrunton.droneforecast.viewmodels.LocationItemViewModel


/**
 * Created by jjbrunton on 31/10/2017.
 */
class LocationViewModelAdapter(private var locations: Observable<List<LocationItemViewModel>>, private val lifecycle: LifecycleProvider<Any>, private val itemListener: OnItemClickListener<LocationItemViewModel>) : RecyclerView.Adapter<LocationItemViewHolder>() {
    private var items: ArrayList<LocationItemViewModel>
    private val itemsRemovedSubject: PublishSubject<LocationItemViewModel> = PublishSubject.create()
    private var contextItem = -1

    val itemsRemovedStream: Observable<LocationItemViewModel>
        get() = this.itemsRemovedSubject

    init {
        this.items = ArrayList()
        this.locations.bindToLifecycle(lifecycle).observeOn(AndroidSchedulers.mainThread()).subscribe {
            this.items.clear()
            this.items.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationItemViewHolder {
        var itemView: View
        itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.location_item, parent, false)

        return LocationItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationItemViewHolder, position: Int) {
        holder.setViewModel(this.items[position])
        holder.itemView.setOnLongClickListener {
            this.contextItem = position
            false
        }
        holder.itemView.setOnClickListener {
            this.itemListener.onItemSelected(this.items[position])
        }
    }

    fun onItemDismiss(position: Int) {
        this.itemsRemovedSubject.onNext(this.items[position])
    }

    fun onContextDismiss() {
        this.onItemDismiss(this.contextItem)
    }
}