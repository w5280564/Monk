package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.baseview.MyCardEditView;
import com.qingbo.monk.base.viewTouchDelegate;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.MyArrowItemView;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MyAndOtherEdit_Card extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back_Btn)
    Button back_Btn;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.head_Img)
    ImageView head_Img;
    @BindView(R.id.nickName_MyView)
    MyArrowItemView nickName_MyView;
    @BindView(R.id.address_MyView)
    MyArrowItemView address_MyView;
    @BindView(R.id.brief_Tv)
    TextView brief_Tv;

    @BindView(R.id.interest_Flow)
    FlowLayout interest_Flow;
    @BindView(R.id.good_EditView)
    MyCardEditView good_EditView;
    @BindView(R.id.resources_EditView)
    MyCardEditView resources_EditView;
    @BindView(R.id.achievement_EditView)
    MyCardEditView achievement_EditView;
    @BindView(R.id.learn_EditView)
    MyCardEditView learn_EditView;
    @BindView(R.id.harvest_EditView)
    MyCardEditView harvest_EditView;


    private String userID;

    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyAndOtherEdit_Card.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.myandother_edit_card;
    }

    @Override
    protected void initLocalData() {
        userID = getIntent().getStringExtra("userID");
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(back_Btn, 50);
    }

    @Override
    protected void initEvent() {
        back_Btn.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getUserData(userID, true);
    }

    UserBean userBean;

    private void getUserData(String userId, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
//                if (refresh_layout.isRefreshing()) {
//                    refresh_layout.setRefreshing(false);
//                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        GlideUtils.loadCircleImage(mActivity, iv_img, userBean.getCover_image());
                        GlideUtils.loadCircleImage(mActivity, head_Img, userBean.getAvatar());
                        nickName_MyView.getTvContent().setText(userBean.getNickname());
                        String s = userBean.getCountry() + userBean.getCity() + userBean.getCounty();
                        address_MyView.getTvContent().setText(s);
//                        brief_Tv.setText(userBean.getDescription());
                        originalValue(userBean.getDescription(), "暂未填写", "", brief_Tv);
                        interestLabelFlow(interest_Flow, mActivity, userBean.getInterested());
//                        labelFlow(label_Lin, mActivity, userBean.getTagName());
//                        tv_follow_number.setText(userBean.getFollowNum());
//                        tv_fans_number.setText(userBean.getFansNum());
//
                        originalValue(userBean.getDomain(), "暂未填写", "", good_EditView.getContent_Tv());
                        originalValue(userBean.getResource(), "暂未填写", "", resources_EditView.getContent_Tv());
                        originalValue(userBean.getAchievement(), "暂未填写", "", achievement_EditView.getContent_Tv());
                        originalValue(userBean.getResearch(), "暂未填写", "", learn_EditView.getContent_Tv());
                        originalValue(userBean.getGetResource(), "暂未填写", "", harvest_EditView.getContent_Tv());
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 我的兴趣
     */
    public void interestLabelFlow(FlowLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        for (String s : tagS) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.changeShapColor(label_Name, ContextCompat.getColor(mContext, com.xunda.lib.common.R.color.lable_color_1F8FE5));
            label_Name.setText(s);
            myFlow.addView(view);
        }
    }


    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + (CharSequence) value);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_Btn:
                finish();
                break;
        }
    }

}