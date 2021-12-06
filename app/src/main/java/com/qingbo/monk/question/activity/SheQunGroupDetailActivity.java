package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群详情
 */
public class SheQunGroupDetailActivity extends BaseActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<>();
    private String id;


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, SheQunGroupDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shequn_detail;
    }

    @Override
    protected void initView() {
        setBar();
        initMenuData();
    }



    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void getServerData() {
        getGroupDetail();
    }

    @Override
    protected void setStatusBar() {

    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this).titleBar(llTitle)
                .statusBarDarkFont(false)
                .init();
    }


    /**
     * 初始化4个频道
     */
    private void initMenuData() {
        List<AppMenuBean> menuList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                bean.setName("全部");
//                fragments.add(AnchorShopWindowFragment.NewInstance(user_id));
            } else if (i == 1) {
                bean.setName("等你回答");
            } else if (i == 2) {
                bean.setName("去提问");
            } else if (i == 2) {
                bean.setName("我的发布");
            }  else {
                bean.setName("审核");
            }

            menuList.add(bean);
        }
        initViewPager(menuList);
    }

    private void initViewPager(List<AppMenuBean> menuList) {
        NormalFragmentAdapter mFragmentAdapterAdapter = new NormalFragmentAdapter(getSupportFragmentManager(), fragments, menuList);
        //给ViewPager设置适配器
        viewpager.setAdapter(mFragmentAdapterAdapter);
        viewpager.setOffscreenPageLimit(1);
        //将TabLayout和ViewPager关联起来。
        tabs.setupWithViewPager(viewpager);
    }



    /**
     * 获取社群详情
     */
    private void getGroupDetail() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.shequnInfo, "获取社群详情", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
//                            PersonInfoBean obj = GsonUtil.getInstance().json2Bean(data, PersonInfoBean.class);
//                            handleFollowData(obj);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }

//    private void handleFollowData(PersonInfoBean obj) {
//        if (obj != null) {
//            initFollowValue(obj);
//        }
//    }

//    private void handleData(PersonInfoBean obj) {
//        if (obj != null) {
//        }
//
//    }



    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.ll_play_live:
//                jumpToPlayLiveActivity(channel_id,rtmp);
//                break;
            case R.id.ll_back:
                back();
                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }



}
