package com.qingbo.monk.Slides.activity;


import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.InterestAll_Fragment;
import com.qingbo.monk.Slides.fragment.InterestMy_Fragment;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧边栏-兴趣组
 */
public class SideslipInterest_Activity extends BaseActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private List<AppMenuBean> menuList = new ArrayList<>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

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


    private void initViewPager(int position) {
        NormalFragmentAdapter mFragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(), fragments, menuList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size());
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }





}