package com.qingbo.monk.Slides.fragment;

import android.os.Bundle;
import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;

/**
 * 侧滑——个股-A股
 */
public class StockAFragment extends BaseTabLayoutFragment {

    public static StockAFragment newInstance(String tagId) {
        Bundle args = new Bundle();
        args.putString("tagId", tagId);
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
        String name = "国泰中证动漫游戏ETF";
        String code = "516010";
        fragments.add(StockOrFund_Mess_Fragment.newInstance(name, code));
        fragments.add(StockOrFund_Question_Fragment.newInstance(name, code));
        fragments.add(FundNitice_Fragment.newInstance(code));
        fragments.add(Stockthigh_Fragment.newInstance("600000","1"));

        fragments.add(Stockthigh_Fragment.newInstance("600000","2"));
        fragments.add(Stockthigh_Fragment.newInstance("600000","3"));
//        fragments.add(FundManager_Fragment.newInstance("159001"));
        initViewPager(0);

//        for (int i = 0; i < 2; i++) {
//            AppMenuBean bean = new AppMenuBean();
//            if (i == 0) {
//                fragments.add(new QuestionFragment_Square());
//                bean.setName("问答广场");
//            } else{
//                fragments.add(new QuestionFragment_Group());
//                bean.setName("问答社群");
//            }
//            menuList.add(bean);
//        }

//        initViewPager(0);
    }





}
