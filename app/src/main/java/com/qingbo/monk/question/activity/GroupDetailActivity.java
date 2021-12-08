package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.question.fragment.GroupFragment_All;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群详情
 */
public class GroupDetailActivity extends BaseActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.tv_shequn_name)
    TextView tv_shequn_name;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<>();
    private String id;
    private MyGroupBean sheQunBean;


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupDetailActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail;
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
        ImmersionBar.with(this).titleBar(rl_title)
                .statusBarDarkFont(false)
                .init();
    }


    /**
     * 初始化4个频道
     */
    private void initMenuData() {
        List<AppMenuBean> menuList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                bean.setName("全部");
                fragments.add(new GroupFragment_All());
            } else if (i == 1) {
                bean.setName("等你回答");
                fragments.add(new GroupFragment_All());
            } else if (i == 2) {
                bean.setName("去提问");
                fragments.add(new GroupFragment_All());
            } else if (i == 3) {
                bean.setName("我的发布");
                fragments.add(new GroupFragment_All());
            } else if (i == 4) {
                bean.setName("审核");
                fragments.add(new GroupFragment_All());
            } else {
                bean.setName("预览");
                fragments.add(new GroupFragment_All());
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
                            sheQunBean = GsonUtil.getInstance().json2Bean(data, MyGroupBean.class);
                            handleData();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData() {
        if (sheQunBean != null) {
            sheQunBean.setId(id);
            String group_header = sheQunBean.getShequnImage();
            if (StringUtil.isBlank(group_header)) {
                iv_head_bag.setImageResource(R.mipmap.bg_group_top);
            }else{
                GlideUtils.loadImage(mContext,iv_head_bag,group_header);
            }
            tv_shequn_name.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
        }
    }




    @OnClick({R.id.ll_back,R.id.ll_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                back();
                break;
            case R.id.ll_menu:
                GroupSettingActivity.actionStart(mActivity,sheQunBean);
                break;
        }
    }




}
