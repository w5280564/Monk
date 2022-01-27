package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.base.baseview.MyCardEditView;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.person.activity.MyComment_Activity;
import com.qingbo.monk.person.activity.MyCrateArticle_Avtivity;
import com.qingbo.monk.person.activity.MyDrafts_Activity;
import com.qingbo.monk.person.activity.MyDynamic_Activity;
import com.qingbo.monk.person.activity.MyFansActivity;
import com.qingbo.monk.person.activity.MyFeedBack_Activity;
import com.qingbo.monk.person.activity.MyFollowActivity;
import com.qingbo.monk.person.activity.MyGroupList_Activity;
import com.qingbo.monk.person.activity.MyHistory_Activity;
import com.qingbo.monk.person.activity.MySet_Activity;
import com.qingbo.monk.person.activity.MyWallet_Activity;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的
 */
@SuppressLint("NonConstantResourceId")
public class MineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @BindView(R.id.iv_userHeader)
    ImageView iv_userHeader;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.label_Lin)
    LinearLayout label_Lin;
    @BindView(R.id.tv_follow_number)
    TextView tv_follow_number;
    @BindView(R.id.tv_fans_number)
    TextView tv_fans_number;
    @BindView(R.id.iv_qrcode)
    TextView iv_qrcode;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;

    @BindView(R.id.interest_EditView)
    MyCardEditView interest_EditView;
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
    @BindView(R.id.tv_comment)
    TextView tv_comment;
    @BindView(R.id.tv_history)
    TextView tv_history;
    @BindView(R.id.tv_fankui)
    TextView tv_fankui;
    @BindView(R.id.tv_shezhi)
    TextView tv_shezhi;
    @BindView(R.id.tv_fabu)
    TextView tv_fabu;
    @BindView(R.id.tv_caogao)
    TextView tv_caogao;
    @BindView(R.id.tv_dongtai)
    TextView tv_dongtai;
    @BindView(R.id.tv_group)
    TextView tv_group;
    @BindView(R.id.tv_my_wallet)
    TextView tv_my_wallet;
    @BindView(R.id.sex_Tv)
    TextView sex_Tv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        refresh_layout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
        viewTouchDelegate.expandViewTouchDelegate(iv_qrcode, 50);
        viewTouchDelegate.expandViewTouchDelegate(tv_follow_number, 50);
        viewTouchDelegate.expandViewTouchDelegate(tv_fans_number, 50);
        tv_name.setFilters(new InputFilter[]{new ByteLengthFilter(14)});
    }

    @Override
    protected void initEvent() {
        tv_my_wallet.setOnClickListener(this);
        iv_qrcode.setOnClickListener(this);
        refresh_layout.setOnRefreshListener(this);
        tv_comment.setOnClickListener(this);
        tv_history.setOnClickListener(this);
        tv_fankui.setOnClickListener(this);
        tv_shezhi.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);
        tv_caogao.setOnClickListener(this);
        tv_dongtai.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        tv_follow_number.setOnClickListener(this);
        tv_fans_number.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

    UserBean userBean;

    private void getUserData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", SharePref.user().getUserId());
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (refresh_layout.isRefreshing()) {
                    refresh_layout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        PrefUtil.saveUser(userBean, "");
                        GlideUtils.loadCircleImage(requireActivity(), iv_userHeader, userBean.getAvatar());
                        tv_name.setText(userBean.getNickname());
                        labelFlow(label_Lin, requireActivity(), userBean.getTagName());
                        tv_follow_number.setText(userBean.getFollowNum());
                        tv_fans_number.setText(userBean.getFansNum());
                        interestLabelFlow(interest_EditView.getLabel_Flow(), requireActivity(), userBean.getInterested());
                        interestLabelFlow(good_EditView.getLabel_Flow(), requireActivity(), userBean.getDomain());
                        interestLabelFlow(resources_EditView.getLabel_Flow(), requireActivity(), userBean.getResource());
                        interestLabelFlow(learn_EditView.getLabel_Flow(), requireActivity(), userBean.getResearch());
                        interestLabelFlow(harvest_EditView.getLabel_Flow(), requireActivity(), userBean.getGetResource());

                        originalValue(userBean.getAchievement(), "暂未填写","", achievement_EditView.getContent_Tv());
                        originalValue(userBean.getSex(), "未知", "性别：", sex_Tv);
                    }
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 我的标签
     *
     * @param myFlow
     * @param mContext
     * @param tag
     */
    public void labelFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
        }
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
    public void onRefresh() {
        getUserData();
    }

    @Override
    public void onClick(View v) {
        String id = SharePref.user().getUserId();
        switch (v.getId()) {
            case R.id.tv_follow_number:
                skipAnotherActivity(MyFollowActivity.class);
                break;
            case R.id.tv_fans_number:
                skipAnotherActivity(MyFansActivity.class);
                break;

            case R.id.tv_my_wallet:
                skipAnotherActivity(MyWallet_Activity.class);
                break;

            case R.id.iv_qrcode:
                MyAndOther_Card.actionStart(mActivity, id);
                break;
            case R.id.tv_comment:
                skipAnotherActivity(MyComment_Activity.class);
                break;
            case R.id.tv_history:
                skipAnotherActivity(MyHistory_Activity.class);
                break;
            case R.id.tv_fankui:
                skipAnotherActivity(MyFeedBack_Activity.class);
                break;
            case R.id.tv_shezhi:
                skipAnotherActivity(MySet_Activity.class);
                break;
            case R.id.tv_fabu:
                if (userBean != null) {
                    String isOriginator = userBean.getIsOriginator();
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
