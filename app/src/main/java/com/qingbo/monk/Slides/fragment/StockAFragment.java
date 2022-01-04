package com.qingbo.monk.Slides.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;

/**
 * 侧滑——个股-A股
 */
public class StockAFragment extends BaseTabLayoutFragment {
    private String name;
    private String code;

    public static StockAFragment newInstance(String name,String code) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("code", code);
        StockAFragment fragment = new StockAFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.stocka_fragment;
    }

    @Override
    protected void initView(View mRootView) {
        mViewPager = mRootView.findViewById(R.id.viewpager);
        mTabLayout = mRootView.findViewById(R.id.tabs);
        initMenuData();
    }

    @Override
    protected void initLocalData() {
        name = getArguments().getString("name");
        code = getArguments().getString("code");
    }

    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("资讯");
        tabName.add("问答");
        tabName.add("公告");
        tabName.add("十大股东");
        tabName.add("十大流通股东");
        tabName.add("基金持股");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(StockOrFund_Mess_Fragment.newInstance(name, code));
        fragments.add(StockOrFund_Question_Fragment.newInstance(name, code));
        fragments.add(StockNitice_Fragment.newInstance("1", code));
        fragments.add(StockThigh_Fragment.newInstance(code, "1"));
        fragments.add(StockThigh_Fragment.newInstance(code, "2"));
        fragments.add(StockThigh_Fragment.newInstance(code, "3"));

        initChildViewPager(0);
    }


}
