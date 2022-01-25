package com.qingbo.monk.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.bean.BaseMessageRecordBean;
import com.qingbo.monk.bean.SearchAll_Bean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 搜索——综合
 */
public class HomeSeek_Whole_Fragment extends BaseLazyFragment {
    @BindView(R.id.label_Flow)
    public FlowLayout label_Flow;
    @BindView(R.id.user_Lin)
    LinearLayout user_Lin;
    @BindView(R.id.person_Lin)
    LinearLayout person_Lin;

    public static HomeSeek_Whole_Fragment newInstance() {
        Bundle args = new Bundle();
        HomeSeek_Whole_Fragment fragment = new HomeSeek_Whole_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.homeseek_whole_fragment;
    }

    @Override
    protected void initView() {
        List<String> strings = ((HomeSeek_Activity) requireActivity()).mDbDao.queryData("");
        interestLabelFlow(label_Flow, mActivity, strings);
    }

    @Override
    protected void loadData() {
        SearchAllList("c", false);
    }

    /**
     * 历史搜索
     */
    public void interestLabelFlow(FlowLayout myFlow, Context mContext, List<String> tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (ListUtils.isEmpty(tag)) {
            return;
        }
        for (String s : tag) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_label, null);
            TextView label_Name = view.findViewById(R.id.label_Name);
//            StringUtil.changeShapColor(label_Name, ContextCompat.getColor(mContext, com.xunda.lib.common.R.color.lable_color_1F8FE5));
            label_Name.setText(s);
            myFlow.addView(view);
        }
    }

    /**
     *
     */
    public void SearchAllList(String word, boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word + "");
        requestMap.put("page", 1 + "");
        requestMap.put("limit", 10 + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_All, "搜索-默认页", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            SearchAll_Bean searchAll_bean = GsonUtil.getInstance().json2Bean(json_root, SearchAll_Bean.class);
//                            handleSplitListData(mObj, mAdapter, 40);

                            List<SearchAll_Bean.DataDTO.UserDTO> user= searchAll_bean.getData().getUser();
                            personLabelLin(user_Lin,mActivity,user);

                            List<SearchAll_Bean.DataDTO.PeopleDTO> person = searchAll_bean.getData().getPeople();
                            userLabelLin(person_Lin,mActivity,person);
                        }
                    }

                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    /**
     *用户
     */
    public void personLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.UserDTO> tag) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(tag)) {
            return;
        }
        for (SearchAll_Bean.DataDTO.UserDTO s : tag) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.character_adapter, null);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            TextView nickName_Tv = view.findViewById(R.id.nickName_Tv);
            LinearLayout lable_Lin = view.findViewById(R.id.lable_Lin);
            TextView company_Tv = view.findViewById(R.id.company_Tv);

            GlideUtils.loadCircleImage(mContext,head_Img,s.getAvatar(),R.mipmap.icon_logo);
            nickName_Tv.setText(s.getNickname());
//            company_Tv.setText(s.getCompanyName());
//            labelFlow(lable_Lin, mContext, s.getTagName());
            myLin.addView(view);
        }
    }


    /**
     *人物
     */
    public void userLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.PeopleDTO> tag) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(tag)) {
            return;
        }
        for (SearchAll_Bean.DataDTO.PeopleDTO s : tag) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.character_adapter, null);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            TextView nickName_Tv = view.findViewById(R.id.nickName_Tv);
            LinearLayout lable_Lin = view.findViewById(R.id.lable_Lin);
            TextView company_Tv = view.findViewById(R.id.company_Tv);

            GlideUtils.loadCircleImage(mContext,head_Img,s.getAvatar(),R.mipmap.icon_logo);
            nickName_Tv.setText(s.getNickname());
            company_Tv.setText(s.getCompanyName());
            labelFlow(lable_Lin, mContext, s.getTagName());
            myLin.addView(view);
        }
    }



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
