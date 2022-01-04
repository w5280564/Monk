package com.qingbo.monk.Slides.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.InterestAll_Fragment;
import com.qingbo.monk.Slides.fragment.StockAFragment;
import com.qingbo.monk.Slides.fragment.StockHKFragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.CombinationLineChart_Bean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.adapter.Combination_Shares_Adapter;
import com.qingbo.monk.home.fragment.ArticleDetail_Zan_Fragment;
import com.qingbo.monk.home.fragment.CombinationDetail_Comment_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 兴趣圈详情
 */
public class InterestDetail_Activity extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.appLayout)
    AppBarLayout appLayout;


    private String isShowTop;
    private String id;

    /**
     * @param context
     * @param id
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String isShowTop,  String id) {
        Intent intent = new Intent(context, InterestDetail_Activity.class);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_interestdetail;
    }


    @Override
    protected void initLocalData() {
        isShowTop = getIntent().getStringExtra("isShowTop");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
    }


    @Override
    protected void initEvent() {
    }

    @Override
    protected void getServerData() {
//        getUserDetail(true);
    }

    @Override
    public void onClick(View v) {
        if (homeCombinationBean == null) {
            return;
        }
        switch (v.getId()) {
        }
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("全部");
        tabName.add("我的发布");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }

//        fragments.add(StockAFragment.newInstance("",""));
////         name = "凤祥股份";
////         code = "156154";
//        fragments.add(StockHKFragment.newInstance("",""));
        fragments.add(InterestAll_Fragment.newInstance("1"));
        fragments.add(InterestAll_Fragment.newInstance("1"));
        initViewPager(0);
    }



    /**
     * 是否是自己
     * @param authorId2
     * @return
     */
    public boolean isMy(String authorId2){
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {//是自己不能评论
            return true;
        }
        return false;
    }


    private List<Object> tabFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
//    private void initTab() {
//        List<String> tabsList = new ArrayList<>();
//        tabsList.add("评论");
//        tabsList.add("赞");
//        mTabLayout.setTabMode(TabLayout.MODE_AUTO);
//        mTabLayout.setTabIndicatorFullWidth(false);//下标跟字一样宽
//        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
//        mTabLayout.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
////        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity,R.color.text_color_444444));
//        //添加tab
//        int sizes = tabsList.size();
//        for (int i = 0; i < sizes; i++) {
//            mTabLayout.addTab(mTabLayout.newTab().setText(tabsList.get(i)));
//        }
//        tabFragmentList.add(CombinationDetail_Comment_Fragment.newInstance(id));
//        tabFragmentList.add(ArticleDetail_Zan_Fragment.newInstance(id, ""));
//
//        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//            @NonNull
//            @Override
//            public Fragment getItem(int position) {
//                return (Fragment) tabFragmentList.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return tabFragmentList.size();
//            }
//
//            @Nullable
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return tabsList.get(position);
//            }
//        });
//        //设置TabLayout和ViewPager联动
//        mTabLayout.setupWithViewPager(mViewPager, false);
//    }

    HomeCombinationBean homeCombinationBean;
    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        HttpSender httpSender = new HttpSender(HttpUrl.Square_Position_List, "仓位组合详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeCombinationBean = GsonUtil.getInstance().json2Bean(json_data, HomeCombinationBean.class);
                    if (homeCombinationBean != null) {
//                        initTab();
                        isChangeFold();
                    }

                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }



    /**
     * 收起整个折叠页
     */
    private void isChangeFold() {
        if (TextUtils.equals(isShowTop, "1")) {
            appLayout.setExpanded(false);
        }
    }




}