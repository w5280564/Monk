package com.xunda.lib.common.base;


import android.annotation.SuppressLint;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.xunda.lib.common.bean.AppMenuBean;


/**
 * 不用传值的Adapter
 */
public class NormalFragmentAdapter extends FragmentPagerAdapter {
    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction;
    private List<Fragment> fragments;
    private List<AppMenuBean> titles;

    @SuppressLint("WrongConstant")
    public NormalFragmentAdapter(FragmentManager manager, List<Fragment> fragments, List<AppMenuBean> titles) {
        super(manager);
        mFragmentManager = manager;
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


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


    /**
     * 清除缓存fragment
     *
     * @param container ViewPager
     */
    public void clear(ViewGroup container) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }

        for (int i = 0; i < fragments.size(); i++) {
            long itemId = this.getItemId(i);
            String name = makeFragmentName(container.getId(), itemId);
            Fragment fragment = this.mFragmentManager.findFragmentByTag(name);
            if (fragment != null) {//根据对应的ID，找到fragment，删除
                mCurTransaction.remove(fragment);
            }
        }
        mCurTransaction.commitNowAllowingStateLoss();
    }

    /**
     * 等同于FragmentPagerAdapter的makeFragmentName方法，
     * 由于父类的该方法是私有的，所以在此重新定义
     * @param viewId
     * @param id
     * @return
     */
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

}
