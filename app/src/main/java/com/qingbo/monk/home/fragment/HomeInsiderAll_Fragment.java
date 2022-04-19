package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.InsiderListBean;
import com.qingbo.monk.home.adapter.Insider_Adapter;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 首页滑动tab页--内部人
 */
public class HomeInsiderAll_Fragment extends BaseTabLayoutFragment {

    /**
     *
     * @param type 1是A股2是港股
     * @return
     */
    public static HomeInsiderAll_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        HomeInsiderAll_Fragment fragment = new HomeInsiderAll_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_insider;
    }


    @Override
    protected void initView(View mRootView) {
        mViewPager = mRootView.findViewById(R.id.card_ViewPager);
        mTabLayout = mRootView.findViewById(R.id.card_Tab);
        initMenuData();
    }


    @SuppressLint("WrongConstant")
    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(HomeInsider_Fragment.newInstance("1"));
                bean.setName("A股");
            } else {
                fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
                bean.setName("港股");
            }
            menuList.add(bean);
        }

        initChildViewPager(0);
    }



}
