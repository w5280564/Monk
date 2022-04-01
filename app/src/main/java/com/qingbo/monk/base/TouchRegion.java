package com.qingbo.monk.base;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;

/**
 * 扩大触摸区域
 *
 * @author venshine
 */
public class TouchRegion {

    private TouchDelegateGroup touchDelegateGroup;

    /**
     * 同一个布局下需要多个扩大点击区域 用此方法
     * 构造方法
     *
     * @param viewGroup 触摸view的父类
     */
    public TouchRegion(ViewGroup viewGroup) {
        touchDelegateGroup = new TouchDelegateGroup(viewGroup);
    }

    /**
     * 构造方法
     *
     * @param view 触摸view
     */
    public TouchRegion(View view) {
        touchDelegateGroup = new TouchDelegateGroup((View) view.getParent());
    }

    /**
     * 扩大View的触摸和点击范围，最大不超过其父View范围
     *
     * @param view
     * @param margin
     */
    public void expandViewTouchRegion(View view, int margin) {
        expandViewTouchRegion(view, margin, margin, margin, margin);
    }

    /**
     * 扩大View的触摸和点击范围，最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public void expandViewTouchRegion(final View view, final int left, final int top, final int right
            , final int bottom) {

        if (view == null) {
            throw new RuntimeException("view cannot be null.");
        }

        final ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.post(() -> {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.left -= left;
                bounds.top -= top;
                bounds.right += right;
                bounds.bottom += bottom;

//                touchDelegateGroup.clearTouchDelegates();
                touchDelegateGroup.addTouchDelegate(new TouchDelegate(bounds, view));

                if (View.class.isInstance(viewGroup)) {
                    viewGroup.setTouchDelegate(touchDelegateGroup);
                }
            });
        }

    }

    /**
     * 恢复View的触摸和点击范围，最小不小于View自身范围
     *
     * @param view
     */
    public void restoreViewTouchRegion(final View view) {

        if (view == null) {
            throw new RuntimeException("view cannot be null.");
        }

        final ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.post(new Runnable() {
                @Override
                public void run() {
                    Rect bounds = new Rect();
                    bounds.setEmpty();
                    touchDelegateGroup.addTouchDelegate(new TouchDelegate(bounds, view));

                    if (View.class.isInstance(viewGroup)) {
                        viewGroup.setTouchDelegate(touchDelegateGroup);
                    }
                }
            });
        }

    }

}
