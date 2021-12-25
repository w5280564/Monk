package com.qingbo.monk.Slides.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.Slides.fragment.PersonDetail_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.home.fragment.HomeInsider_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;

import butterknife.BindView;

/**
 * 侧边栏 人物
 */
public class SideslipPersonDetail_Activity extends BaseTabLayoutActivity {
    private String nickname, id;

    @BindView(R.id.sideslip_Con)
    ConstraintLayout sideslip_Con;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;

    /**
     * @param context
     * @param nickname
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String nickname, String id, String isShowTop) {
        Intent intent = new Intent(context, SideslipPersonDetail_Activity.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("id", id);
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
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initView() {
        sideslip_Con.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.fragment_background));
        title_bar.setTitle(nickname);
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(PersonDetail_Fragment.newInstance(nickname, id));
                bean.setName("个人信息");
            } else {
                fragments.add(HomeInsiderHK_Fragment.newInstance("2"));
                bean.setName("问答");
            }
            menuList.add(bean);
        }
        initViewPager(0);
    }


}