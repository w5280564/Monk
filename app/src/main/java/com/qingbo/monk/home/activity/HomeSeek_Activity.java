package com.qingbo.monk.home.activity;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.home.db.DbDao;
import com.qingbo.monk.home.fragment.HomeSeek_Whole_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.SearchEditText;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeSeek_Activity extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.seek_Tv)
    TextView seek_Tv;
    @BindView(R.id.back_Img)
    ImageView back_Img;
    @BindView(R.id.query_Edit)
    public SearchEditText query_Edit;

    public DbDao mDbDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_homeseek;
    }

    @Override
    protected void initView() {
        HideIMEUtil.wrap(this, query_Edit);
        viewTouchDelegate.expandViewTouchDelegate(back_Img, 100);
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
        mDbDao = new DbDao(this);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        seek_Tv.setOnClickListener(this);
        back_Img.setOnClickListener(this);

//        changeTab();
        query_Edit.addTextChangedListener(new query_EditTextChangeListener());
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("综合");
        tabName.add("用户");
        tabName.add("人物");
        tabName.add("股票");
        tabName.add("资讯");
        tabName.add("圈子");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        AppMenuBean bean = new AppMenuBean();
        fragments.add(HomeSeek_Whole_Fragment.newInstance());
        fragments.add(HomeSeek_User.newInstance(""));
        fragments.add(HomeSeek_Person.newInstance(""));
        fragments.add(HomeSeek_Fund.newInstance(""));
        fragments.add(HomeSeek_Topic.newInstance(""));
        fragments.add(HomeSeek_Group.newInstance(""));
        menuList.add(bean);

        initViewPager(0);
    }


    @Override
    public void onClick(View v) {
        int selectedTabPosition = mTabLayout.getSelectedTabPosition();
        CharSequence text = mTabLayout.getTabAt(selectedTabPosition).getText();
        switch (v.getId()) {
            case R.id.back_Img:
                finish();
                break;
            case R.id.seek_Tv:
                addSeekStr(text.toString());
                break;
        }
    }

    private class query_EditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                int selectedTabPosition = mTabLayout.getSelectedTabPosition();
                CharSequence text = mTabLayout.getTabAt(selectedTabPosition).getText();
                changeSeekData(text.toString(), s.toString());
            }
        }
    }


    private void addSeekStr(String text) {
        if (query_Edit.getText().toString().trim().length() != 0) {
            boolean hasData = mDbDao.hasData(query_Edit.getText().toString().trim());
            if (!hasData) {
                mDbDao.insertData(query_Edit.getText().toString().trim());
            } else {
//                T.s("该内容已在历史记录中", 3000);
            }
//            mAdapter.updata(mDbDao.queryData(""));
            List<String> strings = mDbDao.queryData("");
            FlowLayout label_flow = ((HomeSeek_Whole_Fragment) fragments.get(0)).label_Flow;
            ((HomeSeek_Whole_Fragment) fragments.get(0)).interestLabelFlow(label_flow, mActivity, strings);
            String query_EditString = query_Edit.getText().toString();
            changeSeekData(text, query_EditString);
        } else {
            T.s("请输入内容", 3000);
        }
    }

    private void changeSeekData(String type, String seekStr) {
        int selectedTabPosition = mTabLayout.getSelectedTabPosition();
        if (selectedTabPosition == -1) {
            selectedTabPosition = 0;
        }
        if (TextUtils.equals(type, "综合")) {
//                ((HomeSeek_Whole_Fragment) fragments.get(0)).onResume();
            ((HomeSeek_Whole_Fragment) fragments.get(selectedTabPosition)).SearchAllList(seekStr, true);
        } else if (TextUtils.equals(type, "用户")) {
            ((HomeSeek_User) fragments.get(selectedTabPosition)).getExpertList(seekStr, false);
        } else if (TextUtils.equals(type, "人物")) {
            ((HomeSeek_Person) fragments.get(selectedTabPosition)).getExpertList(seekStr, false);
        } else if (TextUtils.equals(type, "股票")) {
//            ((HomeSeek_Fund) fragments.get(3)).getExpertList(seekStr, false);
            ((HomeSeek_Fund) fragments.get(selectedTabPosition)).onRefreshData();
        } else if (TextUtils.equals(type, "资讯")) {
            ((HomeSeek_Topic) fragments.get(selectedTabPosition)).getExpertList(seekStr, false);
        } else if (TextUtils.equals(type, "圈子")) {
            ((HomeSeek_Group) fragments.get(selectedTabPosition)).getExpertList(seekStr, false);
        }
    }

    /**
     * 切换tab刷新数据
     */
    private void changeTab() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                changeTabAndRefresh(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void changeTabAndRefresh(int index) {
        mViewPager.setCurrentItem(index);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        BaseRecyclerViewSplitFragment fragment = (BaseRecyclerViewSplitFragment) fragments.get(index);
        fragment.onRefresh();
    }

}