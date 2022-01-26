package com.qingbo.monk.Slides.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.FundCombination_Fragment;
import com.qingbo.monk.Slides.fragment.FundManager_Fragment;
import com.qingbo.monk.Slides.fragment.FundNitice_Fragment;
import com.qingbo.monk.Slides.fragment.StockOrFund_Mess_Fragment;
import com.qingbo.monk.Slides.fragment.StockOrFund_Question_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MogulTagListBean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 侧滑——基金
 */
public class SideslipFund_Activity extends BaseTabLayoutActivity {

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
        Intent intent = new Intent(context, SideslipFund_Activity.class);
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

    @Override
    protected void getServerData() {
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("资讯");
        tabName.add("问答");
        tabName.add("公告");
        tabName.add("基金持股");
        tabName.add("经理");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }

        fragments.add(StockOrFund_Mess_Fragment.newInstance(name, code));
        fragments.add(StockOrFund_Question_Fragment.newInstance(name, code));
        fragments.add(FundNitice_Fragment.newInstance(code));
        fragments.add(FundCombination_Fragment.newInstance(code));//"160613"
        fragments.add(FundManager_Fragment.newInstance(code));//"159001"
        initViewPager(0);
    }

    @Override
    public void onRightClick() {
        skipAnotherActivity(HomeSeek_Activity.class);
    }
}