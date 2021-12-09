package com.qingbo.monk.home.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivityWithFragment;
import com.qingbo.monk.dialog.QuitDialog;
import com.qingbo.monk.home.fragment.HomeFragment;
import com.qingbo.monk.home.fragment.MessageFragment;
import com.qingbo.monk.home.fragment.MineFragment;
import com.qingbo.monk.home.fragment.QuestionFragment;
import com.qingbo.monk.home.fragment.UniverseFragment;
import com.qingbo.monk.login.activity.LoginActivity;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

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
    private long clickTime;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        initFragment();
        mBottomNavigationView.setItemIconTintList(null);
    }

    private void initFragment() {
        addFragment(new HomeFragment());
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.em_main_home:
                invalidateOptionsMenu();
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
        String avatar = PrefUtil.getUser().getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Tv, avatar);
        String nickName = PrefUtil.getUser().getNickname();
        nickName_Tv.setText(nickName);
        String fow = PrefUtil.getUser().getFollowNum();
        String fans = PrefUtil.getUser().getFansNum();
        String fowAndFans = String.format("关注 %1$s      粉丝 %2$s", fow, fans);
        followAndFans_Tv.setText(fowAndFans);
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

    @Override
    public void onClick(View v) {
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
        }
    }


    private void getQuit() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Login_Logout, "退出登录", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    PrefUtil.clearSharePrefInfo();
                    BaseApplication.getInstance().clearActivity();
                    skipAnotherActivity(LoginActivity.class);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


}