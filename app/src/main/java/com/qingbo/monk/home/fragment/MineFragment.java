package com.qingbo.monk.home.fragment;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.InterestDetail_All_Adapter;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的
 */
public class MineFragment extends BaseFragment {
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void getServerData() {
        String id = PrefUtil.getUser().getId();
        if (!TextUtils.isEmpty(id)) {
            getUserData(id);
        }
    }

    private void getUserData(String userId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    UserBean userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        GlideUtils.loadCircleImage(requireActivity(), iv_userHeader, userBean.getAvatar());
                        tv_name.setText(userBean.getNickname());
                        labelFlow(label_Lin,requireActivity(),userBean.getTagName());
                        tv_follow_number.setText(userBean.getFollowNum());
                        tv_fans_number.setText(userBean.getFansNum());

                    }

                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 我的标签
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
            label_Name.setOnClickListener(v -> {
            });
        }
    }


}
