package com.qingbo.monk.home.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.home.db.DbDao;
import com.qingbo.monk.home.fragment.HomeInsider_Fragment;
import com.qingbo.monk.home.fragment.HomeSeek_Whole_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.SearchEditText;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeSeek_Activity extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.cancel_Tv)
    TextView cancel_Tv;
    @BindView(R.id.query_Edit)
    SearchEditText query_Edit;

    public DbDao mDbDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_homeseek;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
        mDbDao = new DbDao(this);
        List<String> strings = mDbDao.queryData("");
    }

    @Override
    protected void initEvent() {
        cancel_Tv.setOnClickListener(this);
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
        fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
        fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
        fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
        fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
        fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
        menuList.add(bean);

        initViewPager(0);
    }

    @Override
    public void onClick(View v) {
        int selectedTabPosition = mTabLayout.getSelectedTabPosition();
        CharSequence text = mTabLayout.getTabAt(selectedTabPosition).getText();
        switch (v.getId()) {
            case R.id.cancel_Tv:
//                if (TextUtils.equals(text, "综合")) {
                addSeekStr(text.toString());
//                }
                break;
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
            if (TextUtils.equals(text, "综合")) {
//                ((HomeSeek_Whole_Fragment) fragments.get(0)).onResume();
                ((HomeSeek_Whole_Fragment) fragments.get(0)).SearchAllList("c", false);;
            }
        } else {
            T.s("请输入内容", 3000);
        }
    }
}