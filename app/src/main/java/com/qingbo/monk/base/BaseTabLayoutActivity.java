package com.qingbo.monk.base;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qingbo.monk.R;
import com.google.android.material.tabs.TabLayout;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * tablayout的基类
 */
public class BaseTabLayoutActivity extends BaseActivity {
    protected List<Fragment> fragments = new ArrayList<>();
    protected List<AppMenuBean> menuList = new ArrayList<>();
    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tablayout;
    }



    protected void initViewPager(int position) {
        NormalFragmentAdapter mFragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(), fragments, menuList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size());
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }


}
