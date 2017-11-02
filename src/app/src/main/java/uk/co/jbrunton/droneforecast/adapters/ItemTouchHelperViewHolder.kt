package uk.co.jbrunton.droneforecast.adapters

import android.support.v7.widget.helper.ItemTouchHelper



/**
 * Created by jjbrunton on 01/11/2017.
 */
interface ItemTouchHelperViewHolder {

    /**
     * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    fun onItemSelected()


    /**
     * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
     * state should be cleared.
     */
    fun onItemClear()
}