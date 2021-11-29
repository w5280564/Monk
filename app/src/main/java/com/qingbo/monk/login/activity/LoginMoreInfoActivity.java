package com.qingbo.monk.login.activity;

import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivityWithFragment;
import com.qingbo.monk.home.activity.MainActivity;
import com.qingbo.monk.login.fragment.StepFourFragmentLogin;
import com.qingbo.monk.login.fragment.StepOneFragmentLogin;
import com.qingbo.monk.login.fragment.StepThreeFragmentLogin;
import com.qingbo.monk.login.fragment.StepTwoFragmentLogin;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;

/**
 * 登录更多资料
 */
public class LoginMoreInfoActivity extends BaseActivityWithFragment {

    @BindView(R.id.iv_step_two)
    ImageView iv_step_two;
    @BindView(R.id.iv_step_three)
    ImageView iv_step_three;

    private int fragmentId = R.id.fl_login;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_more_info;
    }


    @Override
    protected void initView() {
        addFragment(new StepOneFragmentLogin());
        addFragment(new StepTwoFragmentLogin());
        addFragment(new StepThreeFragmentLogin());
        addFragment(new StepFourFragmentLogin());
        showStepOneFragment();
        registerEventBus();
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_background)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    private void showStepOneFragment() {
        iv_step_two.setImageResource(R.mipmap.icon_xingqu);
        iv_step_three.setImageResource(R.mipmap.icon_baocun);
        showFragment(0, fragmentId);
    }


    private void showStepTwoFragment() {
        iv_step_two.setImageResource(R.mipmap.icon_xingqu_xuanzhong);
        iv_step_three.setImageResource(R.mipmap.icon_baocun);
        showFragment(1, fragmentId);
    }


    private void showStepThreeFragment() {
        iv_step_two.setImageResource(R.mipmap.icon_xingqu_xuanzhong);
        iv_step_three.setImageResource(R.mipmap.icon_baocun);
        showFragment(2, fragmentId);
    }

    private void showStepFourFragment() {
        iv_step_two.setImageResource(R.mipmap.icon_xingqu_xuanzhong);
        iv_step_three.setImageResource(R.mipmap.icon_baocun_xuanzhong);
        showFragment(3, fragmentId);
    }

    @Subscribe
    public void onGetRegisterNextStepBroadEvent(LoginMoreInfoEvent event) {
        if (event instanceof LoginMoreInfoEvent) {
            switch (event.type) {
                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ZERO://登录填写更多信息第一步
                    showStepOneFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE://登录填写更多信息第一步点提交
                    showStepTwoFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO://登录填写更多信息第一步点提交
                    showStepThreeFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_THREE://登录填写更多信息第三步点提交
                    showStepFourFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_FOUR://登录填写更多信息第四步点提交
                    goToMainActivity();
                    break;

            }
        }
    }

    @Override
    public void onLeftClick() {
        onReturn();
    }


    @Override
    public void onBackPressed() {
        onReturn();
    }

    private void onReturn() {
        goToMainActivity();
    }

    /**
     * 去首页
     */
    private void goToMainActivity() {
        skipAnotherActivity(MainActivity.class);
        finish();
    }
}
