package com.qingbo.monk.home.fragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.SideslipFind_Card_Fragment;
import com.qingbo.monk.Slides.fragment.SideslipMogul_Fragment;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.message.activity.ContactListActivity;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 会话列表
 */
public class MessageFragment extends BaseTabLayoutFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }


    @Override
    protected void initView(View mView) {
        mViewPager = mView.findViewById(R.id.viewpager);
        mTabLayout = mView.findViewById(R.id.tabs);
        initMenuData();
        initTab();
    }


    @OnClick({R.id.seek_Tv, R.id.ll_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seek_Tv:
                skipAnotherActivity(HomeSeek_Activity.class);
                break;
            case R.id.ll_contact:
                skipAnotherActivity(ContactListActivity.class);
                break;
        }
    }

    private void initMenuData() {
//        if (fragments != null){
//            fragments.clear();
//            menuList.clear();
//        }
//        for (int i = 0; i < 2; i++) {
//            AppMenuBean bean = new AppMenuBean();
//            if (i == 0) {
//                bean.setName("发现");
//                fragments.add(SideslipFind_Card_Fragment.newInstance("1"));
//            } else{
//                bean.setName("好友消息");
//                fragments.add(new Message_List_Fragment());
//            }
//            menuList.add(bean);
//        }
//        initViewPager(0);
    }

    private List<Object> tabFragmentList = new ArrayList<>();
    private void initTab() {
        List<String> tabsList = new ArrayList<>();
        tabsList.add("发现");
        tabsList.add("好友消息");
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabIndicatorFullWidth(false);//下标跟字一样宽
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        mTabLayout.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
        //添加tab
        int sizes = tabsList.size();
        for (int i = 0; i < sizes; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabsList.get(i)));
        }
        tabFragmentList.add(SideslipFind_Card_Fragment.newInstance("1"));
        tabFragmentList.add(new Message_List_Fragment());

        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return (Fragment) tabFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return tabFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabsList.get(position);
            }
        });
        mViewPager.setOffscreenPageLimit(tabFragmentList.size());
        //设置TabLayout和ViewPager联动
        mTabLayout.setupWithViewPager(mViewPager);
    }




}
