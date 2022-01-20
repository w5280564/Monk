package com.qingbo.monk.home.fragment;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.InterestDetail_Activity;
import com.qingbo.monk.Slides.fragment.SideslipMogul_Fragment;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.HomeInterestBean;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.home.activity.MainActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.btn_drawer_left)
    Button btn_drawer_left;
    @BindView(R.id.change_Tv)
    TextView change_Tv;
    @BindView(R.id.change_Img)
    ImageView change_Img;
    @BindView(R.id.interest_Lin)
    LinearLayout interest_Lin;
    @BindView(R.id.card_Tab)
    TabLayout card_Tab;
    @BindView(R.id.card_ViewPager)
    ViewPager card_ViewPager;
    @BindView(R.id.appLayout)
    AppBarLayout appLayout;
    @BindView(R.id.icon_Img)
    ImageView icon_Img;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        super.initView();
        initTab();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btn_drawer_left.setOnClickListener(new btn_drawer_leftClick());
        change_Tv.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getInterestLab();
    }

    private void getInterestLab() {
        HashMap<String, String> requestMap = new HashMap<>();
//        requestMap.put("page", page + "");
        requestMap.put("limit", "3");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_Group, "推荐--兴趣组列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeInterestBean interestBean = new Gson().fromJson(json_root, HomeInterestBean.class);
                    if (interestBean != null) {
                        labelList(mContext, interest_Lin, interestBean.getData());
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_Tv:
                rotate(change_Img);
                getInterestLab();
        }
    }


    private class btn_drawer_leftClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((MainActivity) requireActivity()).LeftDL();
        }
    }

    /**
     * 首页我的兴趣组
     * @param context
     * @param myFlex
     * @param model
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void labelList(Context context, LinearLayout myFlex, HomeInterestBean.DataDTO model) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        } else {
            return;
        }
        int size = model.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.interest_lable, null);
            HomeInterestBean.DataDTO.ListDTO data = model.getList().get(i);
            ImageView head_Tv = myView.findViewById(R.id.head_Tv);
            TextView shares_Tv = myView.findViewById(R.id.shares_Tv);
            TextView add_Tv = myView.findViewById(R.id.add_Tv);
            TextView join_Tv = myView.findViewById(R.id.join_Tv);
            String headUrl = data.getGroupImage();
            GlideUtils.loadCircleImage(context, head_Tv, headUrl, R.mipmap.icon_logo);
            shares_Tv.setText(data.getGroupName());
            add_Tv.setText(data.getJoinNum() + "加入");
            int state = data.getJoinStatus();
            joinState(state, join_Tv);
            myView.setTag(i);
            join_Tv.setTag(i);
            myFlex.addView(myView);
            myView.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                String id = model.getList().get(tag).getId();
                InterestDetail_Activity.startActivity(requireActivity(),"0",id);
            });
            join_Tv.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                int stateIndex = model.getList().get(tag).getJoinStatus();
                changeState(stateIndex, join_Tv, model);
                getJoin(model.getList().get(tag).getId());
            });
        }
    }

    //加入的状态
    private void joinState(int state, TextView joinTv) {
        if (state == 1) { //1已加入 其他都是未加入
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }

    //修改加入状态
    private void changeState(int state, TextView joinTv, HomeInterestBean.DataDTO model) {
        int tag = (Integer) joinTv.getTag();
        if (state == 1) {//1已加入 其他都是未加入
            model.getList().get(tag).setJoinStatus(0);
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        } else {
            model.getList().get(tag).setJoinStatus(1);
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        }
    }

    private List<Object> tabFragmentList = new ArrayList<>();

    private void initTab() {
        List<String> tabsList = new ArrayList<>();
        tabsList.add("关注");
        tabsList.add("推荐");
        tabsList.add("大佬");
        tabsList.add("内部人");
        tabsList.add("仓位组合");
        String titleType = "";
        card_Tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        card_Tab.setTabIndicatorFullWidth(false);//下标跟字一样宽
        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        card_Tab.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
//        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity,R.color.text_color_444444));
        //添加tab
        int sizes = tabsList.size();
        for (int i = 0; i < sizes; i++) {
            card_Tab.addTab(card_Tab.newTab().setText(tabsList.get(i)));
        }
        tabFragmentList.add(HomeFocus_Fragment.newInstance(titleType));
        tabFragmentList.add(HomeCommendFragment.newInstance(titleType));
//        tabFragmentList.add(HomeFocus_Fragment.newInstance(titleType));
        tabFragmentList.add(SideslipMogul_Fragment.newInstance(""));
        tabFragmentList.add(HomeInsider_Fragment.newInstance("1"));
        tabFragmentList.add(HomeCombination_Fragment.newInstance(titleType));

        card_ViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
        card_ViewPager.setOffscreenPageLimit(tabFragmentList.size());
        //设置TabLayout和ViewPager联动
        card_Tab.setupWithViewPager(card_ViewPager);
        changePager(1);
    }

    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    private void rotate(View view) {
        Long animateTime = 500L;
        RotateAnimation animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        //设置无线循环
        animation.restrictDuration(Animation.INFINITE);
        //设置匀速旋转
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(animateTime);
        view.startAnimation(animation);
    }

    /**
     * 修改导航页数
     *
     * @param index
     */
    public void changePager(int index) {
        card_ViewPager.setCurrentItem(index);
    }


}
