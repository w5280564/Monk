package com.qingbo.monk.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qingbo.monk.bean.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * ListView和GridView通用适配器
 * @param <T>
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {
	protected BaseActivity mActivity;
	protected HashMap<String, String> baseMap = new HashMap<String, String>();
	protected List<T> mlist;
	private int mlayout;


	public BaseListViewAdapter(BaseActivity activity, List<T> list, int layout) {
		this.mActivity = activity;
		this.mlist = list;
		this.mlayout = layout;
	}


	@Override
	public int getCount() {
		return mlist != null ? mlist.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mlist != null ? mlist.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = ViewHolder.get(mActivity, arg1, arg2, mlayout, arg0);
		getView(vh, arg0, mlist.get(arg0));
		return vh.getConvertView();
	};

	public abstract void getView(ViewHolder vh, int position, T T);

	public void add(T T) {
		if (T != null) {
			mlist.add(T);
			notifyDataSetChanged();
		}
	}

	public void addAll(List<? extends T> list) {
		if (list != null && list.size() != 0) {
			mlist.addAll(list);
			notifyDataSetChanged();
		}
	}

	public void remove(T T) {
		if (T != null) {
			mlist.remove(T);
			notifyDataSetChanged();
		}
	}

	public List<T> getList() {
		return mlist;
	}

	public void setMlist(List<T> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}

	public void clear() {
		mlist.clear();
	}


}
