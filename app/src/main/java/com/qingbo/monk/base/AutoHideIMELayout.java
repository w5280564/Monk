package com.qingbo.monk.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

/**
 * 隐藏键盘
 */
public class AutoHideIMELayout extends FrameLayout {

    public AutoHideIMELayout(Context context) {
        super(context);
    }

    public AutoHideIMELayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHideIMELayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Context context = getContext();
            if (context == null || !(context instanceof Activity)) {
                return super.dispatchTouchEvent(ev);
            }

            Activity activity = (Activity) context;
            View focusView = activity.getCurrentFocus();

            if (focusView != null && shouldHideInputMethod(focusView, ev)) {
                hideInputMethod(focusView);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean shouldHideInputMethod(View focusView, MotionEvent event) {
        Rect rect = new Rect();
        focusView.getHitRect(rect);
        return !rect.contains((int) event.getX(), (int) event.getY());
    }

    private void hideInputMethod(View currentFocus) {
        if (currentFocus == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) currentFocus.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
