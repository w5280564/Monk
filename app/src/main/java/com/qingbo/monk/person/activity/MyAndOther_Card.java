package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.CustomCoordinatorLayout;
import com.qingbo.monk.home.fragment.HomeFocus_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;

public class MyAndOther_Card extends BaseTabLayoutActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_myandother_card;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.card_Tab);
        mViewPager = findViewById(R.id.card_ViewPager);
       CustomCoordinatorLayout headLayout = findViewById(R.id.headLayout);
       ImageView iv_img = findViewById(R.id.iv_img);
       ConstraintLayout title_Con = findViewById(R.id.title_Con);
       headLayout.setmMoveView(title_Con,mViewPager);
       headLayout.setmZoomView(iv_img);
        initMenuData();
    }

//    @Override
//    protected void initEvent() {
//        super.initEvent();
//    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("我的动态");
        tabName.add("档案");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        String titleType = "";
        fragments.add(HomeFocus_Fragment.newInstance(titleType));
        fragments.add(HomeFocus_Fragment.newInstance(titleType));
        initViewPager(0);
    }
}