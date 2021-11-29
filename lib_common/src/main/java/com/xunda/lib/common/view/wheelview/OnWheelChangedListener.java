

package com.xunda.lib.common.view.wheelview;

/**
 * Wheel changed listener interface.
 * <p>The onChanged() method is called whenever current wheel positions is changed:
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 */
public interface OnWheelChangedListener {
	/**
	 * Callback method to be invoked when current item_choose_image changed
	 * @param wheel the wheel view whose state has changed
	 * @param oldValue the old values of current item_choose_image
	 * @param newValue the new values of current item_choose_image
	 */
	void onChanged(WheelView wheel, int oldValue, int newValue);
	void onStopChanged(WheelView wheel, int currentItem);
}
