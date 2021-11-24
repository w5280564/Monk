package com.xunda.lib.common.base;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.xunda.lib.common.bean.AppMenuBean;


/**
 * 不用传值的Adapter
 */
public class NormalFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<AppMenuBean> titles;

    public NormalFragmentAdapter(FragmentManager manager, List<Fragment> fragments, List<AppMenuBean> titles) {
        super(manager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.titles = titles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getName();
    }
}
