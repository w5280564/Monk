package com.qingbo.monk.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipCombination_Activity;
import com.qingbo.monk.Slides.activity.SideslipExpert_Activity;
import com.qingbo.monk.Slides.activity.SideslipFollow_Activity;
import com.qingbo.monk.Slides.activity.SideslipFund_Activity;
import com.qingbo.monk.Slides.activity.SideslipInsider_Activity;
import com.qingbo.monk.Slides.activity.SideslipInterest_Activity;
import com.qingbo.monk.Slides.activity.SideslipMogul_Activity;
import com.qingbo.monk.Slides.activity.SideslipPersonList_Activity;
import com.qingbo.monk.Slides.activity.SideslipStock_Activity;
import com.qingbo.monk.WebSocketHelper;
import com.qingbo.monk.base.BaseActivityWithFragment;
import com.xunda.lib.common.bean.ReceiveMessageBean;
import com.qingbo.monk.dialog.QuitDialog;
import com.qingbo.monk.home.fragment.HomeFragment;
import com.qingbo.monk.home.fragment.MessageFragment;
import com.qingbo.monk.home.fragment.MineFragment;
import com.qingbo.monk.home.fragment.QuestionFragment;
import com.qingbo.monk.home.fragment.UniverseFragment;
import com.qingbo.monk.login.activity.BindPhoneNumberActivity;
import com.qingbo.monk.login.activity.LoginActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.ReceiveSocketMessageEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue_No_Finish;
import com.xunda.lib.common.view.MyArrowItemView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import butterknife.BindView;

/**
 * 主首页
 */
public class MainActivity extends BaseActivityWithFragment implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    @BindView(R.id.mBottomNavigationView)
    BottomNavigationView mBottomNavigationView;
    private int fragmentId = R.id.act_main_fragment;
    @BindView(R.id.dl_layout)
    DrawerLayout dl_layout;
    @BindView(R.id.lv_drawer_left)
    ConstraintLayout lv_drawer_left;
    @BindView(R.id.nickName_Tv)
    TextView nickName_Tv;
    @BindView(R.id.followAndFans_Tv)
    TextView followAndFans_Tv;
    @BindView(R.id.head_Tv)
    ImageView head_Tv;
    @BindView(R.id.quit_Tv)
    TextView quit_Tv;
    @BindView(R.id.focus_MyView)
    MyArrowItemView focus_MyView;
    @BindView(R.id.Insider_MyView)
    MyArrowItemView Insider_MyView;
    @BindView(R.id.recomm_MyView)
    MyArrowItemView recomm_MyView;
    @BindView(R.id.mogul_MyView)
    MyArrowItemView mogul_MyView;
    @BindView(R.id.zhuan_MyView)
    MyArrowItemView zhuan_MyView;
    @BindView(R.id.gu_MyView)
    MyArrowItemView gu_MyView;
    @BindView(R.id.fund_MyView)
    MyArrowItemView fund_MyView;
    @BindView(R.id.cang_MyView)
    MyArrowItemView cang_MyView;
    @BindView(R.id.person_MyView)
    MyArrowItemView person_MyView;
    @BindView(R.id.wen_MyView)
    MyArrowItemView wen_MyView;
    @BindView(R.id.Interest_MyView)
    MyArrowItemView Interest_MyView;

    private long clickTime;
    private TextView tv_unread_msg_number;
    private HomeFragment homeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public static void actionStart(Context context,String openid,int isFromType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("isFromType",isFromType);
        intent.putExtra("openid",openid);
        context.startActivity(intent);
    }

    public static void actionStart(Context context,int band_wx,int isFromType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("isFromType",isFromType);
        intent.putExtra("band_wx",band_wx);
        context.startActivity(intent);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initFragment();
        mBottomNavigationView.setItemIconTintList(null);
        addTabBadge();
        registerEventBus();
    }


    @Subscribe
    public void onReceiveSocketMessageEvent(ReceiveSocketMessageEvent event) {
        if(event.type == ReceiveSocketMessageEvent.RECEIVE_MESSAGE){
            L.e("websocket","首页接收消息");
            getAllUnreadNumber();
        }
    }



    private void initFragment() {
        homeFragment = new HomeFragment();
        addFragment(homeFragment);
        addFragment(new QuestionFragment());
        addFragment(new UniverseFragment());
        addFragment(new MessageFragment());
        addFragment(new MineFragment());
        showFragment(0, fragmentId);
    }

    @Override
    protected void initEvent() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        quit_Tv.setOnClickListener(this);
        Insider_MyView.setOnClickListener(this);
        recomm_MyView.setOnClickListener(this);
        focus_MyView.setOnClickListener(this);
        mogul_MyView.setOnClickListener(this);
        zhuan_MyView.setOnClickListener(this);
        gu_MyView.setOnClickListener(this);
        fund_MyView.setOnClickListener(this);
        cang_MyView.setOnClickListener(this);
        person_MyView.setOnClickListener(this);

        wen_MyView.setOnClickListener(this);
        Interest_MyView.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.em_main_home:
                showFragment(0, fragmentId);
                break;
            case R.id.em_main_questions:
                showFragment(1, fragmentId);
                break;
            case R.id.em_main_universe:
                showFragment(2, fragmentId);
                break;
            case R.id.em_main_message:
                showFragment(3, fragmentId);
                break;
            case R.id.em_main_mine:
                showFragment(4, fragmentId);
                break;
        }

        return true;
    }

    public void LeftDL() {
        if (dl_layout.isDrawerOpen(lv_drawer_left)) { // 左侧菜单列表已打开
            dl_layout.closeDrawer(lv_drawer_left); // 关闭左侧抽屉
        } else { // 左侧菜单列表未打开
            dl_layout.openDrawer(lv_drawer_left); // 打开左侧抽屉
        }
    }

    @Override
    protected void initLocalData() {
        super.initLocalData();
        int isFromType = getIntent().getIntExtra("isFromType",0);//1来自绑定微信  2来自绑定手机号  0从启动页进来
        if (isFromType==1) {
            String openid = getIntent().getStringExtra("openid");
            if (!StringUtil.isBlank(openid)) {
                showBindPhoneNumberDialog(openid);
            }
        }else if(isFromType==2){
            int band_wx = getIntent().getIntExtra("band_wx",0);
            if (band_wx==0) {
                showBindWechatDialog();
            }
        }else{
            if (PrefUtil.getUser()!=null) {
                int isBindWechat = PrefUtil.getUser().getBand_wx();
                if (isBindWechat==0) {
                    showBindWechatDialog();
                }
            }

        }


        String avatar = PrefUtil.getUser().getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Tv, avatar);
        String nickName = PrefUtil.getUser().getNickname();
        nickName_Tv.setText(nickName);
        String fow = PrefUtil.getUser().getFollowNum();
        String fans = PrefUtil.getUser().getFansNum();
        String fowAndFans = String.format("关注 %1$s      粉丝 %2$s", fow, fans);
        followAndFans_Tv.setText(fowAndFans);
    }

    private void showBindPhoneNumberDialog(String openid) {
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this,"为了您在扫地僧获得更好的用户体验，请绑定手机号。","退出登录","去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
            @Override
            public void onClickRight() {
                BindPhoneNumberActivity.actionStart(mActivity,openid);
            }

            @Override
            public void onClickLeft() {
                getQuit();
            }
        });
        mDialog.show();
    }

    private void showBindWechatDialog() {
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this,"为了您在扫地僧获得更好的用户体验，请绑定微信。","退出登录","去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
            @Override
            public void onClickRight() {
                wechatThirdLogin();
            }

            @Override
            public void onClickLeft() {
                getQuit();
            }
        });
        mDialog.show();
    }


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            T.ss(getString(R.string.Home_press_again));
            clickTime = System.currentTimeMillis();
        } else {
            closeApp();
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        String name;
        String code;
        switch (v.getId()) {
            case R.id.quit_Tv:
                new QuitDialog(mActivity, "退出登录/关闭", "关闭扫地僧", "退出登录", new QuitDialog.ConfirmListener() {
                    @Override
                    public void onClickClose() {
                        closeApp();
                    }

                    @Override
                    public void onClickQuit() {
                        new TwoButtonDialogBlue(mActivity, "确定退出登录吗？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
                            @Override
                            public void onClickRight() {
                                getQuit();
                            }

                            @Override
                            public void onClickLeft() {
                            }
                        }).show();
                    }
                }).show();
                break;
            case R.id.Insider_MyView:
                SideslipInsider_Activity.startActivity(this, "", "");
                closeLeft();
                break;
            case R.id.recomm_MyView:
                closeLeft();
                homeFragment.changePager(1);
                break;
            case R.id.focus_MyView:
                closeLeft();
                skipAnotherActivity(SideslipFollow_Activity.class);
                break;
            case R.id.mogul_MyView:
                closeLeft();
                skipAnotherActivity(SideslipMogul_Activity.class);
                break;
            case R.id.zhuan_MyView:
                closeLeft();
                skipAnotherActivity(SideslipExpert_Activity.class);
                break;
            case R.id.gu_MyView:
                closeLeft();
                name = "紫金银行";
                code = "601860";
                SideslipStock_Activity.startActivity(mActivity, name, code);
                break;
            case R.id.fund_MyView:
                closeLeft();
                name = "国泰中证动漫游戏ETF";
                code = "516010";
                SideslipFund_Activity.startActivity(mActivity, name, code);
                break;
            case R.id.cang_MyView:
                closeLeft();
                skipAnotherActivity(SideslipCombination_Activity.class);
                break;
            case R.id.person_MyView:
                closeLeft();
//                String s = "David Gardner";
//                String s1 = "706";
//                SideslipPersonDetail_Activity.startActivity(mActivity, s,s1, "0");
                SideslipPersonList_Activity.startActivity(mActivity);
                break;
            case R.id.wen_MyView:
                closeLeft();
                mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(1).getItemId());
                break;
            case R.id.Interest_MyView:
                closeLeft();
                SideslipInterest_Activity.startActivity(mActivity);
                break;
        }
    }

    // 关闭左侧抽屉
    private void closeLeft() {
        dl_layout.closeDrawer(lv_drawer_left);
    }

    private void getQuit() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Login_Logout, "退出登录", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    WebSocketHelper.getInstance().unbindWebSocketService(mContext);//解绑WebSocketService
                    PrefUtil.clearSharePrefInfo();
                    BaseApplication.getInstance().clearActivity();
                    skipAnotherActivity(LoginActivity.class);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }




    @Override
    protected void onResume() {
        super.onResume();
        getAllUnreadNumber();
    }

    /**
     * 获取所有未读消息数量
     */
    private void getAllUnreadNumber() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.getAllUnreadNumber, "获取所有未读消息数量", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    String tempNum = GsonUtil.getInstance().getValue(json_data,"num");
                    if (StringUtil.isBlank(tempNum)) {
                        return;
                    }

                    int unreadNum = Integer.parseInt(tempNum);
                    if (unreadNum==0) {
                        tv_unread_msg_number.setVisibility(View.GONE);
                    }else{
                        tv_unread_msg_number.setVisibility(View.VISIBLE);
                        tv_unread_msg_number.setText(handleUnreadNum(unreadNum));
                    }
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private String handleUnreadNum(int unreadMsgCount) {
        if(unreadMsgCount <= 99) {
            return String.valueOf(unreadMsgCount);
        }else {
            return "99+";
        }
    }

    /**
     * 添加右上角的红点
     */
    private void addTabBadge() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemTab = (BottomNavigationItemView) menuView.getChildAt(3);
        View badge = LayoutInflater.from(mContext).inflate(R.layout.layout_home_badge, menuView, false);
        tv_unread_msg_number = badge.findViewById(R.id.tv_unread_msg_number);
        itemTab.addView(badge);
    }

}