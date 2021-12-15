package com.qingbo.monk.Slides.activity;

import android.annotation.SuppressLint;

import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.home.fragment.HomeFocus_Fragment;
import com.qingbo.monk.home.fragment.HomeInsider_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;

public class SidesliFollow_Activity extends BaseTabLayoutActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sidesli_follow;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(HomeInsider_Fragment.newInstance("1"));
                bean.setName("发现");
            } else {
//                fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
                fragments.add(HomeFocus_Fragment.newInstance(""));
                bean.setName("关注动态");
            }
            menuList.add(bean);
        }
        initViewPager(1);
    }

}