package com.qingbo.monk.Slides.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.Slides.fragment.InterestMy_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.Slides.fragment.InterestAll_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;

/**
 * 侧边栏-兴趣组
 */
public class SideslipInterest_Activity extends BaseTabLayoutActivity {
    /**
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SideslipInterest_Activity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_interest;
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
                fragments.add(InterestAll_Fragment.newInstance("1"));
                bean.setName("全部");
            } else {
                fragments.add(InterestMy_Fragment.newInstance("2"));
                bean.setName("我的兴趣组");
            }
            menuList.add(bean);
        }

        initViewPager(0);

    }


}