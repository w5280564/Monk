package com.xunda.lib.common.view.wheelview;

import android.app.Activity;
import android.view.View;
import com.xunda.lib.common.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源是String形式的对象集合
 */
public class StringWheelMain {
	private View view;
	private Activity activity;
	private String value;
	private WheelView wv_string;
	private List<String> mList = new ArrayList<>();




	public StringWheelMain(View view, Activity activity) {
		this.view = view;
		this.activity = activity;
	}


    public void setList(List<String> provinceList) {
        this.mList = provinceList;
    }

	public void initStringPicker() {
		wv_string = (WheelView) view.findViewById(R.id.wv_string);
		wv_string.setVisibleItems(7);
		initStringWheelView();
	}


	/**
	 * 初始化公司类型资本选择器
	 */
	private void initStringWheelView() {
		wv_string.setViewAdapter((new StringWheelAdapter(activity, mList)));

		if(mList != null && mList.size()>0){
			wv_string.setCurrentItem(0);
			value = mList.get(0);
		}

		wv_string.addChangingListener(new OnWheelChangedListener() {


			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				value = mList.get(newValue);
			}

			@Override
			public void onStopChanged(WheelView wheel, int currentItem) {

			}
		});

	}



	public String getValue() {
		return value;

	}










}
