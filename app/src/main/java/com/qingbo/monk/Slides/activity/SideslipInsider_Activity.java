package com.qingbo.monk.Slides.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.home.fragment.HomeInsider_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;

/**
 * 侧边栏 内部人列表
 */
public class SideslipInsider_Activity extends BaseTabLayoutActivity {
//    @BindView(R.id.card_Tab)
//    TabLayout mTabLayout;
//    @BindView(R.id.card_ViewPager)
//    ViewPager mViewPager;

    /**
     * @param context
     * @param articleId
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String articleId, String isShowTop) {
        Intent intent = new Intent(context, SideslipInsider_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        context.startActivity(intent);
    }


    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_main_color)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_insider;
    }


    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
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

        initViewPager(0);
    }


}