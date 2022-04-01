package com.qingbo.monk.Slides.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.StockAFragment;
import com.qingbo.monk.Slides.fragment.StockHKFragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.TouchRegion;
import com.qingbo.monk.bean.StockFundMes_ListBean;
import com.qingbo.monk.bean.Stock_Bean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧滑——个股
 */
public class SideslipStock_Activity extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.back_Tv)
    TextView back_Tv;
    @BindView(R.id.seek_Tv)
    TextView seek_Tv;
    @BindView(R.id.title_Con)
    ConstraintLayout title_Con;
    @BindView(R.id.title_Tv)
    TextView title_Tv;
    @BindView(R.id.left_Tv)
    TextView left_Tv;
    @BindView(R.id.right_Tv)
    TextView right_Tv;

    private String stockName;
    public String stockCode;
    private int page = 1;

    /**
     * @param context
     * @param name
     * @param code
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
        stockName = getIntent().getStringExtra("name");
        stockCode = getIntent().getStringExtra("code");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_slideslip_stock;
    }


    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);

        TouchRegion touchRegion = new TouchRegion(title_Con);
        touchRegion.expandViewTouchRegion(back_Tv, 100);
        touchRegion.expandViewTouchRegion(seek_Tv, 50);
        touchRegion.expandViewTouchRegion(left_Tv, 50);
        touchRegion.expandViewTouchRegion(right_Tv, 50);

//        new TouchRegion(back_Tv).expandViewTouchRegion(back_Tv,100);
//        new TouchRegion(seek_Tv).expandViewTouchRegion(seek_Tv,100);

        title_Tv.setText(stockName);
        initMenuData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        back_Tv.setOnClickListener(this);
        seek_Tv.setOnClickListener(this);
        left_Tv.setOnClickListener(this);
        right_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        stockString = HttpUrl.thigh_List;
        page = 1;
        getListData(true);
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        if (fragments != null) {
            fragments.clear();
        }
        ArrayList<String> tabName = new ArrayList<>();
        if (menuList != null) {
            menuList.clear();
        }
        tabName.add("A股");
        tabName.add("港股");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(StockAFragment.newInstance(stockName, stockCode));
//         name = "凤祥股份";
//         code = "156154";
        fragments.add(StockHKFragment.newInstance(stockName, stockCode));

        int selectedTabPosition = mTabLayout.getSelectedTabPosition();
        if (selectedTabPosition == -1) {
            selectedTabPosition = 0;
        }
        initViewPager(selectedTabPosition);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_Tv:
                onBackPressed();
                break;
            case R.id.seek_Tv:
                skipAnotherActivity(HomeSeek_Activity.class);
                break;
            case R.id.left_Tv:
                leftStock();
                break;
            case R.id.right_Tv:
                rightStock();
                break;
        }
    }

    String stockString;

    /**
     * 标题栏 上一个个股
     */
    private void leftStock() {
        if (page > 1) {
            page--;
        }
        addUrl();
        getListData(false);
    }

    /**
     * 标题栏 下一个个股
     */
    private void rightStock() {
        page++;
        addUrl();
        getListData(false);
    }

    private void addUrl() {
        CharSequence text = mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getText();
        if (TextUtils.equals(text, "A股")) {
            stockString = HttpUrl.thigh_List;
        } else {
            stockString = HttpUrl.etf_List;
        }
    }


    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", "1");
        HttpSender httpSender = new HttpSender(stockString, "A股/港股-切换", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    Stock_Bean stock_bean = GsonUtil.getInstance().json2Bean(json_data, Stock_Bean.class);
                    if (stock_bean != null) {
                        if (!ListUtils.isEmpty(stock_bean.getList())) {
                            Stock_Bean.ListDTO listDTO = stock_bean.getList().get(0);
                            CharSequence text = mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getText();
                            stockCode = listDTO.getNewsDigest();
                            if (TextUtils.equals(text, "A股")) {
                                stockName = listDTO.getNews_Author();
                            } else {
                                stockName = listDTO.getNewsTitle();
                            }
                            title_Tv.setText(stockName);
                            initMenuData();
                        }


                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


}