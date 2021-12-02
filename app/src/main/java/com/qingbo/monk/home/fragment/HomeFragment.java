package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.Topic_Bean;
import com.qingbo.monk.home.activity.MainActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.btn_drawer_left)
    Button btn_drawer_left;
    @BindView(R.id.interest_Lin)
    LinearLayout interest_Lin;

    int page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btn_drawer_left.setOnClickListener(new btn_drawer_leftClick());
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getInterestLab();
    }



    private void getInterestLab() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", "3");
        HttpSender httpSender = new HttpSender(HttpUrl.All_Group, "全部兴趣圈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    InterestBean interestBean = new Gson().fromJson(json_root, InterestBean.class);
                    if (interestBean != null) {
                        lableList(mContext, interest_Lin, interestBean.getData());
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private class btn_drawer_leftClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((MainActivity) requireActivity()).LeftDL();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void lableList(Context context, LinearLayout myFlex, InterestBean.DataDTO model) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = model.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.interest_lable, null);
            InterestBean.DataDTO.ListDTO data =  model.getList().get(i);
            ImageView head_Tv = myView.findViewById(R.id.head_Tv);
            TextView shares_Tv = myView.findViewById(R.id.shares_Tv);
            String headUrl = data.getGroupImage();
            GlideUtils.loadCircleImage(context, head_Tv, headUrl,R.mipmap.img_pic_none_square);
            shares_Tv.setText(data.getGroupName());
            head_Tv.setTag(i);
            myFlex.addView(myView);
            head_Tv.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
//                boolean onChoice = choiceLable.get(tag);
//                if (onChoice){
//                    choiceLable.set(tag,false);
//                    choiceTxt.get(tag).setTextColor(ContextCompat.getColor(context,R.color.text_color_a1a1a1));
//                    choiceTxt.get(tag).setBackgroundResource(R.drawable.label_stroke);
//                    choice_LableMap.remove(model.getChildren().get(tag).getName(),model.getChildren().get(tag).getName());
//                }else {
//                    choiceLable.set(tag,true);
//                    choiceTxt.get(tag).setTextColor(ContextCompat.getColor(context,R.color.text_color_444444));
//                    choiceTxt.get(tag).setBackgroundResource(R.drawable.label_stroke_yellow);
//                    choice_LableMap.put(model.getChildren().get(tag).getName(),model.getChildren().get(tag).getName());
//                }
            });
        }
    }


}
