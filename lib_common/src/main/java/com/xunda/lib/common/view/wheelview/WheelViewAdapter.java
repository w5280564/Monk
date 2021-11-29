

package com.xunda.lib.common.view.wheelview;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
	/**
	 * Gets items count
	 * @return the count of wheel items
	 */
	public int getItemsCount();
	
	/**
	 * Get a View that displays the data at the specified position in the data set
	 * 
	 * @param index the item_choose_image index
	 * @param convertView the old view to reuse if possible
	 * @param parent the parent that this view will eventually be attached to
	 * @return the wheel item_choose_image View
	 */
	public View getItem(int index, View convertView, ViewGroup parent);

	/**
	 * Get a View that displays an empty wheel item_choose_image placed before the first or after
	 * the last wheel item_choose_image.
	 * 
	 * @param convertView the old view to reuse if possible
     * @param parent the parent that this view will eventually be attached to
	 * @return the empty item_choose_image View
	 */
	public View getEmptyItem(View convertView, ViewGroup parent);
	
	/**
	 * Register an observer that is called when changes happen to the data used by this adapter.
	 * @param observer the observer to be registered
	 */
	public void registerDataSetObserver(DataSetObserver observer);
	
	/**
	 * Unregister an observer that has previously been registered
	 * @param observer the observer to be unregistered
	 */
	void unregisterDataSetObserver(DataSetObserver observer);
}
