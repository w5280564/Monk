package com.qingbo.monk.Slides.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.SideslipMogul_Fragment;
import com.qingbo.monk.Slides.fragment.StockAFragment;
import com.qingbo.monk.Slides.fragment.StockHKFragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MogulTagListBean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧滑——个股
 */
public class SideslipStock_Activity extends BaseTabLayoutActivity {

    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private String name;
    private String code;

    /**
     * @param context
     * @param name
     * @param  code
     */
    public static void startActivity(Context context, String name, String code) {
        Intent intent = new Intent(context, SideslipStock_Activity.class);
        intent.putExtra("name", name);
        intent.putExtra("code", code);
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
    protected void initLocalData() {
        name = getIntent().getStringExtra("name");
        code = getIntent().getStringExtra("code");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_slideslip_stock;
    }


    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setTitle(name);
        initMenuData();
    }


    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("A股");
        tabName.add("港股");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }

        fragments.add(StockAFragment.newInstance(name,code));
//         name = "凤祥股份";
//         code = "156154";
        fragments.add(StockHKFragment.newInstance(name,code));
        initViewPager(0);
    }

    @Override
    public void onRightClick() {
        skipAnotherActivity(HomeSeek_Activity.class);
    }
}