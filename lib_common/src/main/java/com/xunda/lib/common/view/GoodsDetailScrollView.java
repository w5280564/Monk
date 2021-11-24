package com.xunda.lib.common.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;

import java.util.ArrayList;

import androidx.core.widget.NestedScrollView;

public class GoodsDetailScrollView extends NestedScrollView {

    private final Point point;
    private int position = 0;
    private OnSelectedIndicatedAndScrollChangeListener onSelectedIndicateChangedListener;
    ArrayList<Integer> arrayDistance = new ArrayList<>();

    public GoodsDetailScrollView(Context context) {
        this(context,null,0);
    }

    public GoodsDetailScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GoodsDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onSelectedIndicateChangedListener != null) {
            onSelectedIndicateChangedListener.onScrollChanged(t);
        }
        int currentPosition = getCurrentPosition(t,arrayDistance);
        if(currentPosition!=position&&getOnSelectedIndicateChangedListener()!=null){
            getOnSelectedIndicateChangedListener().onSelectedChanged(currentPosition);
        }
        this.position = currentPosition;
    }

    private int getCurrentPosition(int t, ArrayList<Integer> arrayDistance) {

        int index = 0;
        for (int i=0;i<arrayDistance.size();i++){
            if(i==arrayDistance.size()-1&&t>0){
                index = i;
            }else {
                if(t>=arrayDistance.get(i)&&t<arrayDistance.get(i+1)){
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    private void scrollToPosition() {
        scrollToPosition(position);
    }

    private void scrollToPosition(int position){
        smoothScrollTo(0,arrayDistance.get(position));
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        scrollToPosition();
    }

    public void setArrayDistance(ArrayList<Integer> arrayDistance) {
        this.arrayDistance = arrayDistance;
    }

    public OnSelectedIndicatedAndScrollChangeListener getOnSelectedIndicateChangedListener() {
        return onSelectedIndicateChangedListener;
    }

    public void setOnSelectedIndicatedAndScrollChangeListener(OnSelectedIndicatedAndScrollChangeListener onSelectedIndicatedAndScrollChangeListener) {
        this.onSelectedIndicateChangedListener = onSelectedIndicatedAndScrollChangeListener;
    }



    public interface OnSelectedIndicatedAndScrollChangeListener{
        void onSelectedChanged(int position);
        void onScrollChanged(int scrollY);
    }


}
