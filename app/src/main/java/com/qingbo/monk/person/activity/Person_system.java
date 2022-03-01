package com.qingbo.monk.person.activity;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.view.MyArrowItemView;

import butterknife.BindView;

/**
 * 系统通知
 */
public class Person_system extends BaseActivity {
    @BindView(R.id.system_MyView)
    MyArrowItemView system_MyView;
    @BindView(R.id.comment_MyView)
    MyArrowItemView comment_MyView;
    @BindView(R.id.focus_MyView)
    MyArrowItemView focus_MyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_system;
    }

    @Override
    protected void initView() {

        system_MyView.getCount_Tv().setVisibility(View.VISIBLE);
        system_MyView.getCount_Tv().setText("5");
        comment_MyView.getCount_Tv().setVisibility(View.VISIBLE);
        comment_MyView.getCount_Tv().setText("5");
        focus_MyView.getCount_Tv().setVisibility(View.VISIBLE);
        focus_MyView.getCount_Tv().setText("5");

    }
}