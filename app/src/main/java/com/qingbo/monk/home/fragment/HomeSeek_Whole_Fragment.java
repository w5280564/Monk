package com.qingbo.monk.home.fragment;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.bean.BaseMessageRecordBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeInterestBean;
import com.qingbo.monk.bean.SearchAll_Bean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.home.adapter.ArticleZan_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
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
    @BindView(R.id.fund_Lin)
    LinearLayout fund_Lin;

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

                            List<SearchAll_Bean.DataDTO.UserDTO> user = searchAll_bean.getData().getUser();
                            userLabelLin(user_Lin, mActivity, user);

                            List<SearchAll_Bean.DataDTO.PeopleDTO> person = searchAll_bean.getData().getPeople();
                            personLabelLin(person_Lin, mActivity, person);

                            List<SearchAll_Bean.DataDTO.CompanyDTO> company = searchAll_bean.getData().getCompany();
                            fundLabelLin(fund_Lin, mActivity, company);


                        }
                    }

                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendPost();
    }


    /**
     * 用户
     */
    public void userLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.UserDTO> item) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(item)) {
            return;
        }
        int size = item.size();
        for (int i = 0; i < size; i++) {
            SearchAll_Bean.DataDTO.UserDTO userDTO = item.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.homeseek_persn, null);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            TextView nickName_Tv = view.findViewById(R.id.nickName_Tv);
            TextView content_Tv = view.findViewById(R.id.content_Tv);
            TextView follow_Tv = view.findViewById(R.id.follow_Tv);
            TextView send_Mes = view.findViewById(R.id.send_Mes);
            LinearLayout label_Lin = view.findViewById(R.id.label_Lin);
            nickName_Tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数
            GlideUtils.loadCircleImage(mContext, head_Img, userDTO.getAvatar(), R.mipmap.icon_logo);
            nickName_Tv.setText(userDTO.getNickname());
            isFollow(userDTO.getFollow_status(), follow_Tv, send_Mes);
            String format = String.format("发表 %1$s条  关注 %2$s人", userDTO.getArticleNum(), userDTO.getFansNum());
            content_Tv.setText(format);
            GlideUtils.loadCircleImage(mContext, head_Img, userDTO.getAvatar(), R.mipmap.icon_logo);
            nickName_Tv.setText(userDTO.getNickname());
            labelAllFlow(label_Lin, mContext, userDTO.getTagName());
            follow_Tv.setTag(i);
            send_Mes.setTag(i);
            myLin.addView(view);
            follow_Tv.setOnClickListener(v -> {
                int tag1 = (int) v.getTag();
                String likeId = item.get(tag1).getId();
                postFollowData(likeId, follow_Tv, send_Mes);
            });
            send_Mes.setOnClickListener(v -> {
                int tag1 = (int) v.getTag();
                String id = item.get(tag1).getId();
                String nickname = item.get(tag1).getNickname();
                String avatar = item.get(tag1).getAvatar();
                ChatActivity.actionStart(mActivity, id, nickname, avatar);
            });
        }
    }

    /**
     * 人物
     */
    public void personLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.PeopleDTO> tag) {
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
            GlideUtils.loadCircleImage(mContext, head_Img, s.getAvatar(), R.mipmap.icon_logo);
            nickName_Tv.setText(s.getNickname());
//            company_Tv.setText(s.getCompanyName());
            originalValue(s.getCompanyName(), "暂未填写", "", company_Tv);
            labelFlow(lable_Lin, mContext, s.getTagName());
            myLin.addView(view);
        }
    }



    /**
     * 股票
     */
    public void fundLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.CompanyDTO> tag) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(tag)) {
            return;
        }
        for (SearchAll_Bean.DataDTO.CompanyDTO s : tag) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.homeseek_fund_label, null);
            TextView fundName_Tv = view.findViewById(R.id.fundName_Tv);
            TextView fundCode_Tv = view.findViewById(R.id.fundCode_Tv);
            fundName_Tv.setText(s.getName());
            fundCode_Tv.setText(s.getNumber());
            myLin.addView(view);
        }
    }


    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(String follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 标签 只显示两个
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
            label_Name.setOnClickListener(v -> {
            });
        }
    }

    /**
     * 标签 全部显示
     *
     * @param myFlow
     * @param mContext
     * @param tag
     */
    public void labelAllFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
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

    private void postFollowData(String otherUserId, TextView follow_Tv, View send_Mes) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    isFollow(followStateBena.getFollowStatus() + "", follow_Tv, send_Mes);

                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}
