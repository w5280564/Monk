package com.qingbo.monk.Slides.widget.carditem;

import android.view.View;
import android.view.ViewGroup;


import com.qingbo.monk.Slides.widget.StackCardsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wensefu on 17-3-4.
 */
public class CardAdapter extends StackCardsView.Adapter {

    private List<BaseCardItem> mItems;

    public void appendItems(List<BaseCardItem> items){
        int size = items == null ? 0 : items.size();
        if (size == 0) {
            return;
        }
        if (mItems == null) {
            mItems = new ArrayList<>(size);
        }
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(int position){
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public List<BaseCardItem> getmItems() {
        return mItems;
    }


    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mItems.get(position).getView(convertView,parent);
    }

    @Override
    public int getSwipeDirection(int position) {
        //这里控制每张卡的支持滑动超过一定距离消失的方向
        BaseCardItem item = mItems.get(position);
        return item.swipeDir;
    }

    @Override
    public int getDismissDirection(int position) {
        //这里控制每张卡的支持滑动超过一定距离消失的方向
        BaseCardItem item = mItems.get(position);
        return item.dismissDir;
    }

    @Override
    public boolean isFastDismissAllowed(int position) {
        //这里控制每张卡的支持快速滑动消失的方向
        BaseCardItem item = mItems.get(position);
        return item.fastDismissAllowed;
    }

    @Override
    public int getMaxRotation(int position) {
        //这里控制每张卡的最大旋转角
        BaseCardItem item = mItems.get(position);
        return item.maxRotation;
    }
}
