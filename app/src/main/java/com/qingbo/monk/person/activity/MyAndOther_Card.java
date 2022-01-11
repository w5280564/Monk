package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.CustomCoordinatorLayout;
import com.qingbo.monk.home.fragment.HomeFocus_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;

import butterknife.BindView;

public class MyAndOther_Card extends BaseTabLayoutActivity implements View.OnClickListener {

    @BindView(R.id.brief_Tv)
    TextView brief_Tv;

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_myandother_card;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.card_Tab);
        mViewPager = findViewById(R.id.card_ViewPager);
        AppBarLayout appbar_Layout = findViewById(R.id.appbar_Layout);
        ImageView iv_img = findViewById(R.id.iv_img);
//       ConstraintLayout title_Con = findViewById(R.id.title_Con);
        ConstraintLayout top_Con = findViewById(R.id.top_Con);
        NestedScrollView top_Src = findViewById(R.id.top_Src);
        CustomCoordinatorLayout headLayout = findViewById(R.id.headLayout);
        LinearLayout bot_Lin = findViewById(R.id.bot_Lin);
        headLayout.setmMoveView(top_Src, bot_Lin);
        headLayout.setmZoomView(iv_img);


        initMenuData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        brief_Tv.setOnClickListener(this);
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("我的动态");
        tabName.add("档案");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        String titleType = "";
        fragments.add(HomeFocus_Fragment.newInstance(titleType));
        fragments.add(HomeFocus_Fragment.newInstance(titleType));
        initViewPager(0);
    }


    /**
     * 加载完成后
     * 解决-->Tablayout+viewpager 刷新数据后不能滑动问题
     */
    private void initAppBarStatus(AppBarLayout mAppBarLayout) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brief_Tv:
                if (brief_Tv.getMaxLines() == 2) {
                    brief_Tv.setMaxLines(Integer.MAX_VALUE);
                }else {
                    brief_Tv.setMaxLines(2);
                }
                break;
        }
    }
}