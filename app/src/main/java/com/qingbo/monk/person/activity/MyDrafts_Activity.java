package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.person.fragment.MyDrafts_Crate_Fragment;
import com.qingbo.monk.person.fragment.MyDrafts_Group_Fragment;
import com.qingbo.monk.person.fragment.MyDrafts_question_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的草稿箱
 */
public class MyDrafts_Activity extends BaseTabLayoutActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mydrafts;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("问答广场");
        tabName.add("发布动态");
        tabName.add("社群");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(MyDrafts_question_Fragment.newInstance("1"));
        fragments.add(MyDrafts_Crate_Fragment.newInstance("2"));
        fragments.add(MyDrafts_Group_Fragment.newInstance("3"));
        initViewPager(0);
    }



}