package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.view.MyArrowItemView;

import butterknife.BindView;

/**
 * 我的设置
 */
public class MySetting_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_bar)
    CustomTitleBar titleBar;
    @BindView(R.id.tv_comment)
    MyArrowItemView tv_comment;
    @BindView(R.id.tv_history)
    MyArrowItemView tv_history;
    @BindView(R.id.tv_fankui)
    MyArrowItemView tv_fankui;
    @BindView(R.id.tv_my_wallet)
    MyArrowItemView tv_my_wallet;
    @BindView(R.id.tv_shezhi)
    MyArrowItemView tv_shezhi;
    @BindView(R.id.tv_fabu)
    MyArrowItemView tv_fabu;
    @BindView(R.id.tv_caogao)
    MyArrowItemView tv_caogao;
    @BindView(R.id.tv_dongtai)
    MyArrowItemView tv_dongtai;
    @BindView(R.id.tv_group)
    MyArrowItemView tv_group;
    private String isOriginator;
    /**
     * 是否是创作者
     * @param context
     * @param isOriginator
     */
    public static void actionStart(Context context, String isOriginator) {
        Intent intent = new Intent(context, MySetting_Activity.class);
        intent.putExtra("isOriginator", isOriginator);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_setting;
    }

    @Override
    protected void initLocalData() {
        isOriginator = getIntent().getStringExtra("isOriginator");
    }


    @Override
    protected void initView() {


    }

    @Override
    protected void initEvent() {
        tv_comment.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_fankui.setOnClickListener(this);
        tv_my_wallet.setOnClickListener(this);
        tv_shezhi.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);
        tv_caogao.setOnClickListener(this);
        tv_dongtai.setOnClickListener(this);
        tv_group.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String id = SharePref.user().getUserId();
        switch (v.getId()) {
            case R.id.tv_comment:
                skipAnotherActivity(MyComment_Activity.class);
                break;
            case R.id.tv_history:
                skipAnotherActivity(MyHistory_Activity.class);
                break;
            case R.id.tv_fankui:
                skipAnotherActivity(MyFeedBack_Activity.class);
                break;
            case R.id.tv_my_wallet:
                skipAnotherActivity(MyWallet_Activity.class);
                break;
            case R.id.tv_shezhi:
                skipAnotherActivity(MySet_Activity.class);
                break;
            case R.id.tv_fabu:
                if (!TextUtils.isEmpty(isOriginator)) {
                    MyCrateArticle_Avtivity.actionStart(mActivity, isOriginator);
                }
                break;
            case R.id.tv_caogao:
                skipAnotherActivity(MyDrafts_Activity.class);
                break;
            case R.id.tv_dongtai:
                skipAnotherActivity(MyDynamic_Activity.class);
                break;
            case R.id.tv_group:
                MyGroupList_Activity.actionStart(mActivity, id);
                break;
        }
    }


}