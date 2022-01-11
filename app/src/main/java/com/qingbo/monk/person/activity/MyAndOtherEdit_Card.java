package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;

public class MyAndOtherEdit_Card extends BaseActivity {




    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyAndOtherEdit_Card.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.myandother_edit_card;
    }


}