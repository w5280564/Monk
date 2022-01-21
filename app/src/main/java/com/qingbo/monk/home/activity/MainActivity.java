package com.qingbo.monk.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.qingbo.monk.bean.MainUpdateCount_Bean;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.person.activity.MyFeedBack_Activity;
import com.qingbo.monk.person.activity.MySet_Activity;
import com.xunda.lib.common.bean.ApkBean;
import com.xunda.lib.common.bean.AppMarketBean;
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
import com.xunda.lib.common.common.http.OnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ChooseAppMarketDialog;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue_No_Finish;
import com.xunda.lib.common.dialog.VersionDialog;
import com.xunda.lib.common.view.MyArrowItemView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 主首页
 */
public class MainActivity extends BaseActivityWithFragment implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
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
    @BindView(R.id.gen_MyView)
    MyArrowItemView gen_MyView;
    @BindView(R.id.yijian_MyView)
    MyArrowItemView yijian_MyView;
    @BindView(R.id.set_MyView)
    MyArrowItemView set_MyView;

    private long clickTime;
    private TextView tv_unread_msg_number;
    private HomeFragment homeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public static void actionStart(Context context, String openid, int isFromType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("isFromType", isFromType);
        intent.putExtra("openid", openid);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int band_wx, int isFromType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("isFromType", isFromType);
        intent.putExtra("band_wx", band_wx);
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
        if (event.type == ReceiveSocketMessageEvent.RECEIVE_MESSAGE) {
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
        gen_MyView.setOnClickListener(this);
        yijian_MyView.setOnClickListener(this);
        set_MyView.setOnClickListener(this);
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
        int isFromType = getIntent().getIntExtra("isFromType", 0);//1来自绑定微信  2来自绑定手机号  0从启动页进来
        if (isFromType == 1) {
            String openid = getIntent().getStringExtra("openid");
            if (!StringUtil.isBlank(openid)) {
                showBindPhoneNumberDialog(openid);
            }
        } else if (isFromType == 2) {
            int band_wx = getIntent().getIntExtra("band_wx", 0);
            if (band_wx == 0) {
                showBindWechatDialog();
            }
        } else {
            if (PrefUtil.getUser() != null) {
                int isBindWechat = PrefUtil.getUser().getBand_wx();
                if (isBindWechat == 0) {
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
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this, "为了您在扫地僧获得更好的用户体验，请绑定手机号。", "退出登录", "去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
            @Override
            public void onClickRight() {
                BindPhoneNumberActivity.actionStart(mActivity, openid);
            }

            @Override
            public void onClickLeft() {
                getQuit();
            }
        });
        mDialog.show();
    }

    private void showBindWechatDialog() {
        TwoButtonDialogBlue_No_Finish mDialog = new TwoButtonDialogBlue_No_Finish(this, "为了您在扫地僧获得更好的用户体验，请绑定微信。", "退出登录", "去绑定", new TwoButtonDialogBlue_No_Finish.ConfirmListener() {
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
            case R.id.gen_MyView:
                closeLeft();
                String id = PrefUtil.getUser().getId();
                MyAndOther_Card.actionStart(mActivity, id);
                break;
            case R.id.yijian_MyView:
                closeLeft();
                skipAnotherActivity(MyFeedBack_Activity.class);
                break;
            case R.id.set_MyView:
                closeLeft();
                skipAnotherActivity(MySet_Activity.class);
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
        getUpdateCount(false);
    }

    /**
     * 关注与问答消息数
     */
    private void getUpdateCount(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.User_Update_Count, "新增评论或文章的数量", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    MainUpdateCount_Bean mainUpdateCount_bean = GsonUtil.getInstance().json2Bean(json_data, MainUpdateCount_Bean.class);
                    if (mainUpdateCount_bean != null) {
                        String newArticle = mainUpdateCount_bean.getNewArticle();
                        if (!TextUtils.equals(newArticle, "0")) {
                            focus_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                            focus_MyView.getCount_Tv().setText(newArticle);
                        }
                        String newSquareComment = mainUpdateCount_bean.getNewSquareComment();
                        if (!TextUtils.equals(newSquareComment, "0")) {
                            wen_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                            wen_MyView.getCount_Tv().setText(newSquareComment);
                        }
//                        if (TextUtils.isEmpty(newArticle) && TextUtils.isEmpty(newSquareComment)){
//                            int count = Integer.parseInt(newArticle);
//                            int commentCount = Integer.parseInt(newSquareComment);
//
//                        }

                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
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
                    String tempNum = GsonUtil.getInstance().getValue(json_data, "num");
                    if (StringUtil.isBlank(tempNum)) {
                        return;
                    }

                    int unreadNum = Integer.parseInt(tempNum);
                    if (unreadNum == 0) {
                        tv_unread_msg_number.setVisibility(View.GONE);
                    } else {
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
        if (unreadMsgCount <= 99) {
            return String.valueOf(unreadMsgCount);
        } else {
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


    @Override
    protected void getServerData() {
        checkUpdate();//版本更新检测
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>版本更新开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private ChooseAppMarketDialog mChooseAppMarketDialog;


    /**
     * 版本更新检测
     */
    private void checkUpdate() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("version", String.valueOf(AndroidUtil.getVersionName(mContext)));
        HttpSender sender = new HttpSender(HttpUrl.appVersion, "版本更新检测",
                baseMap, new OnHttpResListener() {
            @Override
            public void onComplete(String json, int status, String description, String data) {
                if (status == Constants.REQUEST_SUCCESS_CODE) {
                    ApkBean apkObj = GsonUtil.getInstance().json2Bean(data, ApkBean.class);
                    if (apkObj != null) {
                        String tempIsForceUpdate = apkObj.getIs_force_update();//0不需要强制更新，1强制更新，2不需要更新
                        String remark = apkObj.getRemark();
                        if (!StringUtil.isBlank(tempIsForceUpdate)) {
                            int isForceUpdate = Integer.parseInt(tempIsForceUpdate);
                            if (isForceUpdate != 2) {
                                showVersionDialog(remark, isForceUpdate, apkObj.getPlatform());
                            }
                        }

                    }
                }
            }
        }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void showVersionDialog(String remark, int isForceUpdate, String platform) {
        VersionDialog dialog = new VersionDialog(this, remark, isForceUpdate,
                new VersionDialog.VersionConfirmListener() {
                    @Override
                    public void onDownload() {
                        if (!StringUtil.isBlank(platform)) {
                            List<String> mPlatformList = StringUtil.stringToList(platform);
                            if (!ListUtils.isEmpty(mPlatformList)) {
                                if (mPlatformList.size() == 1) {
                                    jumpToWebsite();//只有一个平台时，点击下载直接跳官网
                                } else {//否则弹出平台选择弹窗
                                    handlePlatformList(isForceUpdate, mPlatformList);
                                }
                            }
                        }
                    }

                });
        dialog.show();
    }


    private void handlePlatformList(int isForceUpdate, List<String> mPlatformList) {
        String deviceBrandName = android.os.Build.BRAND;
        List<AppMarketBean> mMarketList = new ArrayList<>();
        for (int i = 0; i < mPlatformList.size(); i++) {
            String name = mPlatformList.get(i);
            AppMarketBean obj = new AppMarketBean();
            obj.setMarketName(name);

            if (deviceBrandName.equalsIgnoreCase(Constants.BRAND_OPPO) && "OPPO".equals(name)) {
                obj.setMarketPakageName("com.heytap.market");
                obj.setIconResource(R.mipmap.icon_oppo);
                obj.setBrandName(Constants.BRAND_OPPO);
                mMarketList.add(obj);
                break;
            } else if (deviceBrandName.equalsIgnoreCase(Constants.BRAND_VIVO) && "VIVO".equals(name)) {
                obj.setMarketPakageName("com.bbk.appstore");
                obj.setIconResource(R.mipmap.icon_vivo);
                obj.setBrandName(Constants.BRAND_VIVO);
                mMarketList.add(obj);
                break;
            } else if (deviceBrandName.equalsIgnoreCase(Constants.BRAND_HUAWEI) && "华为".equals(name)) {
                obj.setMarketPakageName("com.huawei.appmarket");
                obj.setIconResource(R.mipmap.icon_huawei);
                obj.setBrandName(Constants.BRAND_HUAWEI);
                mMarketList.add(obj);
                break;
            } else if (deviceBrandName.equalsIgnoreCase(Constants.BRAND_HONOR) && "华为".equals(name)) {
                obj.setMarketPakageName("com.huawei.appmarket");
                obj.setIconResource(R.mipmap.icon_huawei);
                obj.setBrandName(Constants.BRAND_HUAWEI);
                mMarketList.add(obj);
                break;
            } else if (deviceBrandName.equalsIgnoreCase(Constants.BRAND_XIAOMI) && "小米".equals(name)) {
                obj.setMarketPakageName("com.xiaomi.market");
                obj.setIconResource(R.mipmap.icon_xiaomi);
                obj.setBrandName(Constants.BRAND_XIAOMI);
                mMarketList.add(obj);
                break;
            }
        }

        if (mPlatformList.contains("应用宝")) {
            AppMarketBean obj = new AppMarketBean();
            obj.setMarketName("应用宝");
            obj.setMarketPakageName("com.tencent.android.qqdownloader");
            obj.setIconResource(R.mipmap.icon_yyb);
            mMarketList.add(obj);
        }

        showChooseMarketDialog(isForceUpdate, mMarketList);
    }

    /**
     * 弹出选择市场框
     */
    public void showChooseMarketDialog(int isForceUpdate, List<AppMarketBean> mMarketList) {


        if (mChooseAppMarketDialog == null) {
            mChooseAppMarketDialog = new ChooseAppMarketDialog(this, mMarketList, isForceUpdate, new ChooseAppMarketDialog.DialogItemChooseListener() {
                @Override
                public void onItemChooseClick(AppMarketBean obj) {
                    if (obj != null) {
                        if ("官网".equals(obj.getMarketName())) {
                            jumpToWebsite();
                        } else if ("应用宝".equals(obj.getMarketName())) {
                            openTencentYingYongBao(obj.getMarketPakageName());
                        } else {
                            launchAppDetail(obj.getMarketPakageName());
                        }
                    }

                }
            });
        }

        if (!mChooseAppMarketDialog.isShowing()) {
            mChooseAppMarketDialog.show();
        }
    }

    private void jumpToWebsite() {
        Uri uri = Uri.parse(Constants.WEB_SITE_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    /**
     * 跳转到应用市场app详情界面
     *
     * @param marketPkg 应用市场包名
     */
    public void launchAppDetail(String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + Constants.APP_PKG);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!StringUtil.isBlank(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //打开应用宝
    private void openTencentYingYongBao(String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + Constants.APP_PKG);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!StringUtil.isBlank(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "请先安装应用宝", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://a.app.qq.com/o/simple.jsp?pkgname=com.qingbo.monk");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}