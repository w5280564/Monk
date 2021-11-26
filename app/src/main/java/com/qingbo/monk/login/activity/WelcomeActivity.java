package com.qingbo.monk.login.activity;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;

import butterknife.OnClick;

/**
 * 注册完以后的欢迎页
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    @OnClick({R.id.tv_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_join:
                skipAnotherActivity(LoginMoreInfoActivity.class);
                break;

        }
    }

    @Override
    public void onBackPressed() {

    }
}
