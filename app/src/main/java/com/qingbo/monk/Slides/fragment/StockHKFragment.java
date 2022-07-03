package com.qingbo.monk.Slides.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;

/**
 * 侧滑——个股-港股
 */
public class StockHKFragment extends BaseTabLayoutFragment {
    private String name;
    private String code;

    public static StockHKFragment newInstance(String name, String code) {
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("code", code);
        StockHKFragment fragment = new StockHKFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLocalData() {
        name = getArguments().getString("name");
        code = getArguments().getString("code");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.stockhk_fragment;
    }

    @Override
    protected void initView(View mRootView) {
        mViewPager = mRootView.findViewById(R.id.viewpager);
        mTabLayout = mRootView.findViewById(R.id.tabs);
        initMenuData();
    }


    public void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("资讯");
        tabName.add("问答");
        tabName.add("公告");
        tabName.add("十大股东");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
//        name = "凤祥股份";
//        code = "156154";
        fragments.add(StockOrFund_Mess_Fragment.newInstance(name, code,"2"));
        fragments.add(StockOrFund_Question_Fragment.newInstance(name, code));
        fragments.add(StockNitice_Fragment.newInstance("2", name));
        fragments.add(StockThigh_HK_Fragment.newInstance(code, "1"));

        initChildViewPager(0);
    }


}
