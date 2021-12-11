package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.question.fragment.GroupDetailFragment_All;
import com.qingbo.monk.question.fragment.GroupDetailFragment_What;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群详情
 */
public class GroupDetailActivity extends BaseTabLayoutActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.tv_shequn_name)
    TextView tv_shequn_name;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
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
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);
        setBar();
        initMenuData();
        registerEventBus();
    }

    @Subscribe
    public void onEditGroupEvent(EditGroupEvent event) {
        if(event.type == EditGroupEvent.EDIT_GROUP){
            getGroupDetail(false);
        }
    }



    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void getServerData() {
        getGroupDetail(true);
    }

//    @Override
//    protected void setStatusBar() {
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .statusBarColor(R.color.transparent)     //状态栏颜色，不写默认透明色
//                .statusBarDarkFont(true)
//                .init();
//    }


    @Override
    protected void setStatusBar() {

    }

    /**
     * 设置状态栏
     */
    private void setBar() {
//        ImmersionBar.with(this)
//                .fitsSystemWindows(true)
//                .statusBarColor(R.color.transparent)     //状态栏颜色，不写默认透明色
//                .statusBarDarkFont(true)
//                .init();
        ImmersionBar.with(this).titleBar(rl_title)
                .statusBarDarkFont(false)
                .init();
    }


    /**
     * 初始化4个频道
     */
    private void initMenuData() {
        for (int i = 0; i < 6; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                bean.setName("全部");
                fragments.add(new GroupDetailFragment_All());
            } else if (i == 1) {
                bean.setName("等你回答");
                fragments.add(new GroupDetailFragment_All());
            } else if (i == 2) {
                bean.setName("去提问");
                fragments.add(new GroupDetailFragment_What());
            } else if (i == 3) {
                bean.setName("我的发布");
                fragments.add(new GroupDetailFragment_All());
            } else if (i == 4) {
                bean.setName("审核");
                fragments.add(new GroupDetailFragment_All());
            } else {
                bean.setName("预览");
                fragments.add(new GroupDetailFragment_All());
            }

            menuList.add(bean);
        }
        initViewPager(0);
    }




    /**
     * 获取社群详情
     */
    private void getGroupDetail(boolean isShowAnimal) {
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
                }, isShowAnimal);
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




    @Override
    public void onRightClick() {
        GroupSettingActivity.actionStart(mActivity,sheQunBean);
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
