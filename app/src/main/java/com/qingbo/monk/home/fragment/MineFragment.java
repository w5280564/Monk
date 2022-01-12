package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
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
import com.qingbo.monk.base.baseview.MyCardEditView;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
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


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        refresh_layout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
        viewTouchDelegate.expandViewTouchDelegate(iv_qrcode, 50);
    }

    @Override
    protected void initEvent() {
        iv_qrcode.setOnClickListener(this);
        refresh_layout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserData();
    }

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
                    UserBean userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        PrefUtil.saveUser(userBean, "");
                        GlideUtils.loadCircleImage(requireActivity(), iv_userHeader, userBean.getAvatar());
                        tv_name.setText(userBean.getNickname());
                        labelFlow(label_Lin, requireActivity(), userBean.getTagName());
                        tv_follow_number.setText(userBean.getFollowNum());
                        tv_fans_number.setText(userBean.getFansNum());
                        interestLabelFlow(interest_Flow, requireActivity(), userBean.getInterested());

                        originalValue(userBean.getDomain(), "暂未填写", good_EditView.getContent_Tv());
                        originalValue(userBean.getResource(), "暂未填写", resources_EditView.getContent_Tv());
                        originalValue(userBean.getAchievement(), "暂未填写", achievement_EditView.getContent_Tv());
                        originalValue(userBean.getResearch(), "暂未填写", learn_EditView.getContent_Tv());
                        originalValue(userBean.getGetResource(), "暂未填写", harvest_EditView.getContent_Tv());
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
    private void originalValue(Object value, String originalStr, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(originalStr);
        } else {
            tv.setText((CharSequence) value);
        }
    }


    @Override
    public void onRefresh() {
        getUserData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qrcode:
                String id = SharePref.user().getUserId();
                MyAndOther_Card.actionStart(mActivity, id);
                break;
        }
    }
}
