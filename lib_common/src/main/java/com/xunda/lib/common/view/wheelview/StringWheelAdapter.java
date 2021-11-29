package com.xunda.lib.common.view.wheelview;

import android.content.Context;

import java.util.List;

/**
 * ouyang
 */
public class StringWheelAdapter extends AbstractWheelTextAdapter {

	// items
	private List<String> wheelDates;

	/**
	 * Constructor
	 */
	public StringWheelAdapter(Context context, List<String> wheelDates) {
		super(context);
		this.wheelDates = wheelDates;
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < wheelDates.size()) {
			String item = wheelDates.get(index);
			return item.toString();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return wheelDates.size();
	}

}
