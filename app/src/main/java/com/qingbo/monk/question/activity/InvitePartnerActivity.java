package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
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
    private String shareUrl_partner;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_partner;
    }


    public static void actionStart(Context context, String shareUrl) {
        Intent intent = new Intent(context, InvitePartnerActivity.class);
        intent.putExtra("shareUrl", shareUrl);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        shareUrl_partner = getIntent().getStringExtra("shareUrl");
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
        ShareDialog mShareDialog = new ShareDialog(this,shareUrl_partner,"","邀请合伙人","快加入鹅先知吧","邀请合伙人");
        mShareDialog.show();
    }
}
