package com.qingbo.monk.question.activity;

import android.os.Bundle;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.dialog.ShareDialog;
import butterknife.OnClick;


/**
 * 邀请合伙人
 */
public class InvitePartnerActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_partner;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.text_color_f6f6f6)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }


    @OnClick(R.id.tv_invite)
    public void onClick() {
        showShareDialog();
    }


    private void showShareDialog() {
        ShareDialog mShareDialog = new ShareDialog(this,"https://www.baidu.com/","","邀请合伙人","快加入扫地僧吧");
        mShareDialog.show();
    }
}
