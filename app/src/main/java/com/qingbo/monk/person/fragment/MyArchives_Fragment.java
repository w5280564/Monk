package com.qingbo.monk.person.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;
import java.util.HashMap;
import butterknife.BindView;

/**
 * 社交主页-我/他的档案
 */
public class MyArchives_Fragment extends BaseLazyFragment {
    @BindView(R.id.interest_Flow)
    FlowLayout interest_Flow;
    @BindView(R.id.good_Tv)
    TextView good_Tv;
    @BindView(R.id.resources_Tv)
    TextView resources_Tv;
    @BindView(R.id.achievement_Tv)
    TextView achievement_Tv;
    @BindView(R.id.learn_Tv)
    TextView learn_Tv;
    @BindView(R.id.harvest_Tv)
    TextView harvest_Tv;

    private String userID;

    public static MyArchives_Fragment newInstance(String userID) {
        Bundle args = new Bundle();
        args.putString("userID", userID);
        MyArchives_Fragment fragment = new MyArchives_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_myarchives;
    }

    @Override
    protected void initView(View mView) {
    }

    @Override
    protected void initLocalData() {
         userID = getArguments().getString("userID");
    }

    @Override
    protected void getServerData() {
        getUserData(userID, true);
    }

    @Override
    protected void loadData() {
        getUserData(userID, false);
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
                        interestLabelFlow(interest_Flow, requireActivity(), userBean.getInterested());
                        originalValue(userBean.getDomain(), "暂未填写", good_Tv);
                        originalValue(userBean.getResource(), "暂未填写", resources_Tv);
                        originalValue(userBean.getAchievement(), "暂未填写", achievement_Tv);
                        originalValue(userBean.getResearch(), "暂未填写", learn_Tv);
                        originalValue(userBean.getGetResource(), "暂未填写", harvest_Tv);
                    }
                }
            }
        }, isShow);
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



}
