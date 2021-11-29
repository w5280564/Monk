

package com.xunda.lib.common.view.wheelview;

/**
 * Wheel clicked listener interface.
 * <p>The onItemClicked() method is called whenever a wheel item_choose_image is clicked
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 */
public interface OnWheelClickedListener {
    /**
     * Callback method to be invoked when current item_choose_image clicked
     * @param wheel the wheel view
     * @param itemIndex the index of clicked item_choose_image
     */
    void onItemClicked(WheelView wheel, int itemIndex);
}
