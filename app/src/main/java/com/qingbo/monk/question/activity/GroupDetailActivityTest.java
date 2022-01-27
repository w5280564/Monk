package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.question.fragment.GroupDetailFragment_What;
import com.qingbo.monk.question.fragment.GroupDetailThemeListFragment;
import com.qingbo.monk.question.fragment.GroupDetailTopicListFragment;
import com.qingbo.monk.question.fragment.GroupDetailWaitAnswerListFragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群详情测试
 */
public class GroupDetailActivityTest extends BaseTabLayoutActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.group_AppBarLayout)
    AppBarLayout group_AppBarLayout;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_choose_theme)
    LinearLayout ll_choose_theme;
    @BindView(R.id.tv_shequn_name)
    TextView tv_shequn_name;
    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    private String id;
    private MyGroupBean sheQunBean;


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupDetailActivityTest.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail_test;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tabs);
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        setBar();
        registerEventBus();
    }


    @Subscribe
    public void onEditGroupEvent(EditGroupEvent event) {
        if(event.type == EditGroupEvent.EDIT_GROUP){
            getGroupDetail(false);
        }
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if(event.type == FinishEvent.EXIT_GROUP){
            back();
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



    @Override
    protected void setStatusBar() {

    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this).titleBar(mToolbar)
                .statusBarDarkFont(false)
                .init();
    }


    /**
     * 初始化4个频道
     */
    private void initMenuData() {
        for (int i = 0; i < 5; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                bean.setName("全部");
                fragments.add(GroupDetailTopicListFragment.NewInstance(0,id,sheQunBean.getRole(),sheQunBean.getShequnName()));
                menuList.add(bean);
            } else if (i == 1) {
                String role = sheQunBean.getRole();
                if ("2".equals(role)||"3".equals(role)) {//1管理员2合伙人0一般用户3群主
                    bean.setName("等你回答");
                    fragments.add(GroupDetailWaitAnswerListFragment.NewInstance(id));
                    menuList.add(bean);
                }
            } else if (i == 2) {
                bean.setName("去提问");
                fragments.add(GroupDetailFragment_What.NewInstance(id));
                menuList.add(bean);
            } else if (i == 3) {
                bean.setName("我的发布");
                fragments.add(GroupDetailTopicListFragment.NewInstance(1,id,sheQunBean.getRole(),sheQunBean.getShequnName()));
                menuList.add(bean);
            }else {
                bean.setName("预览主题");
                fragments.add(GroupDetailThemeListFragment.NewInstance(id,sheQunBean.getRole()));
                menuList.add(bean);
            }


        }
        setTabTextSize(15,15);
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
            tv_title_center.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
            initMenuData();
        }
    }






    @OnClick({R.id.iv_bianji,R.id.ll_choose_theme})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_bianji:
                PublisherGroupTopicActivity.actionStart(mActivity,id,sheQunBean.getShequnName());
                break;
            case R.id.ll_choose_theme:
                ChooseThemeActivity.actionStart(mActivity,id);
                break;

        }
    }



    @Override
    protected void initEvent() {
        super.initEvent();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int index) {
                if (menuList.get(index).getName().equals("预览主题")) {
                    String role = sheQunBean.getRole();
                    if ("2".equals(role)||"3".equals(role)) {////1管理员2合伙人0一般用户3群主
                        ll_choose_theme.setVisibility(View.VISIBLE);
                    }else{
                        ll_choose_theme.setVisibility(View.GONE);
                    }
                }else{
                    ll_choose_theme.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        group_AppBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 在我们的这个位置的话设置我们的监听
                int scrollHeight= iv_head_bag.getMeasuredHeight()-mToolbar.getMeasuredHeight();
                if (verticalOffset == 0) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));
                    ImmersionBar.with(mActivity).titleBar(mToolbar).statusBarDarkFont(false).init();//把标题栏和状态栏绑定在一块
                    mToolbar.setNavigationIcon(R.mipmap.icon_back_white);
                    tv_title_center.setVisibility(View.GONE);
                } else {
                    float alpha = (float) -verticalOffset / scrollHeight;
                    if (-verticalOffset < scrollHeight) {
                        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.white), alpha));
                        ImmersionBar.with(mActivity).titleBar(mToolbar).statusBarDarkFont(false).init();//把标题栏和状态栏绑定在一块
                        mToolbar.setNavigationIcon(R.mipmap.icon_back);
                        tv_title_center.setVisibility(View.VISIBLE);
                        tv_title_center.setTextColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.black), alpha));
                    } else if (-verticalOffset >= scrollHeight) {
                        mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.white), 1));
                        ImmersionBar.with(mActivity).titleBar(mToolbar).statusBarDarkFont(true).init();
                        mToolbar.setNavigationIcon(R.mipmap.icon_back);
                        tv_title_center.setVisibility(View.VISIBLE);
                        tv_title_center.setTextColor(ColorUtils.blendARGB(Color.TRANSPARENT, ContextCompat.getColor(mActivity, R.color.black), 1));
                    }

                }
            }
        });

        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        //点击左边返回按钮监听事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }


    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            PublisherGroupTopicActivity.actionStart(mActivity,id,sheQunBean.getShequnName());
            return true;
        }
    };
}
