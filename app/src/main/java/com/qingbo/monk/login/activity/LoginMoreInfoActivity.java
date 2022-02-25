package com.qingbo.monk.login.activity;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivityWithFragment;
import com.qingbo.monk.home.activity.MainActivity;
import com.qingbo.monk.login.fragment.StepFourFragmentLogin;
import com.qingbo.monk.login.fragment.StepOneFragmentLogin;
import com.qingbo.monk.login.fragment.StepThreeFragmentLogin;
import com.qingbo.monk.login.fragment.StepTwoFragmentLogin;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

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
    private HashMap<String, String> requestMap = new HashMap<>();


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
                    if (event.isNext) {//是点下一步操作
                        requestMap.put("avatar", event.avatar);
                        requestMap.put("nickname", event.nickname);
                        requestMap.put("city", event.city);
                        requestMap.put("county", event.county);
                        requestMap.put("work", event.work);
                        requestMap.put("industry", event.industry);
                    }
                    showStepTwoFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO://登录填写更多信息第二步点提交
                    if (event.isNext) {//是点下一步操作
                        requestMap.put("get_resource", event.get_resourceOrInterestedOrDescription);
                    }
                    showStepThreeFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_THREE://登录填写更多信息第三步点提交
                    if (event.isNext) {//是点下一步操作
                        requestMap.put("interested", event.get_resourceOrInterestedOrDescription);
                    }
                    showStepFourFragment();
                    break;

                case LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_FOUR://登录填写更多信息第四步点提交

                    if (event.isNext) {//是点下一步操作
                        requestMap.put("description", event.get_resourceOrInterestedOrDescription);
                    }
                    edit_Info();
                    break;

            }
        }
    }


    /**
     * 修改个人信息
     */
    private void edit_Info() {
        HttpSender httpSender = new HttpSender(HttpUrl.Edit_Info, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    UserBean obj = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    saveUserInfo(obj);
                    goToMainActivity();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * 保存用户信息
     *
     * @param userObj 用户对象
     */
    private void saveUserInfo(UserBean userObj) {
        if (userObj!=null) {
            PrefUtil.saveUser(userObj,"");
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

    }

    /**
     * 去首页
     */
    private void goToMainActivity() {
        skipAnotherActivity(MainActivity.class);
        finish();
    }
}
