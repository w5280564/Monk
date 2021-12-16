package com.qingbo.monk.Slides.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.qingbo.monk.Slides.widget.StackCardsView;


/**
 *
 */
public abstract class BaseCardItem {

    public boolean fastDismissAllowed = true;
    int swipeDir = StackCardsView.SWIPE_ALL;
    public int dismissDir = StackCardsView.SWIPE_ALL;
    int maxRotation = 8;

    protected Context mContext;

    public BaseCardItem(Context context) {
        mContext = context;
    }

    public abstract View getView(View convertView, ViewGroup parent);
}
