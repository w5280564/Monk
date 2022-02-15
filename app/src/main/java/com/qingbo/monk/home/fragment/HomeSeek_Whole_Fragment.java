package com.qingbo.monk.home.fragment;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.AAndHKDetail_Activity;
import com.qingbo.monk.Slides.activity.InterestDetail_Activity;
import com.qingbo.monk.Slides.activity.SideslipPersonDetail_Activity;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.Character_Bean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.HomeSeekTopic_Bean;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.qingbo.monk.bean.SearchAll_Bean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.home.activity.HomeSeek_User;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.person.activity.MyInterestList_Activity;
import com.qingbo.monk.question.activity.CheckOtherGroupDetailActivity;
import com.qingbo.monk.question.activity.GroupDetailActivity;
import com.qingbo.monk.question.activity.PreviewGroupDetailActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.view.SearchEditText;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * ——综合
 */
public class HomeSeek_Whole_Fragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @BindView(R.id.label_Flow)
    public FlowLayout label_Flow;
    @BindView(R.id.user_Lin)
    LinearLayout user_Lin;
    @BindView(R.id.person_Lin)
    LinearLayout person_Lin;
    @BindView(R.id.fund_Lin)
    LinearLayout fund_Lin;
    @BindView(R.id.topic_Lin)
    LinearLayout topic_Lin;
    @BindView(R.id.group_Lin)
    LinearLayout group_Lin;
    @BindView(R.id.seek_Con)
    ConstraintLayout seek_Con;
    @BindView(R.id.noMes_Con)
    ConstraintLayout noMes_Con;
    @BindView(R.id.dele_Tv)
    TextView dele_Tv;
    @BindView(R.id.userSeek_Tv)
    TextView userSeek_Tv;
    @BindView(R.id.personSeek_Tv)
    TextView personSeek_Tv;
    @BindView(R.id.fundSeek_Tv)
    TextView fundSeek_Tv;
    @BindView(R.id.topicSeek_Tv)
    TextView topicSeek_Tv;
    @BindView(R.id.groupSeek_Tv)
    TextView groupSeek_Tv;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private String word;
    private SearchEditText query_edit;

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
    protected void initView(View mRootView) {
        viewTouchDelegate.expandViewTouchDelegate(dele_Tv, 100);
        query_edit = ((HomeSeek_Activity) requireActivity()).query_Edit;
        mSwipeRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        List<String> strings = ((HomeSeek_Activity) requireActivity()).mDbDao.queryData("");
        interestLabelFlow(label_Flow, mActivity, strings);

    }

    @Override
    protected void initEvent() {
        dele_Tv.setOnClickListener(this);
        userSeek_Tv.setOnClickListener(this);
        personSeek_Tv.setOnClickListener(this);
        fundSeek_Tv.setOnClickListener(this);
        topicSeek_Tv.setOnClickListener(this);
        groupSeek_Tv.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
//        SearchAllList("", true);
    }


    @Override
    public void onResume() {
        super.onResume();
        word = ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        SearchAllList(word, true);
    }

    @Override
    public void onRefresh() {
        word = ((HomeSeek_Activity) requireActivity()).query_Edit.getText().toString();
        mSwipeRefreshLayout.setRefreshing(true);
        SearchAllList(word, true);
    }

    /**
     * 历史搜索
     */
    public void interestLabelFlow(FlowLayout myFlow, Context mContext, List<String> item) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (ListUtils.isEmpty(item)) {
            return;
        }
        int size = item.size();
        if (size > 10) {
            size = 10;
        }
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_label, null);
            TextView label_Name = view.findViewById(R.id.label_Name);
            label_Name.setText(item.get(i));
            label_Name.setTag(i);
            myFlow.addView(view);
            label_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    String s = item.get(tag);
                    query_edit.setText(s);
//                    query_edit.setSelection(query_edit.length());//将光标移至文字末尾
                    SearchAllList(s, false);
                }
            });
        }
    }

    int page = 1;

    /**
     * ，默认搜索数据
     */
    public void SearchAllList(String word, boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word + "");
        requestMap.put("page", page + "");
        requestMap.put("limit", 2 + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_All, "搜索-默认页", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            seek_Con.setVisibility(View.VISIBLE);
                            noMes_Con.setVisibility(View.GONE);
                            SearchAll_Bean searchAll_bean = GsonUtil.getInstance().json2Bean(json_root, SearchAll_Bean.class);

                            List<SearchAll_Bean.DataDTO.UserDTO> user = searchAll_bean.getData().getUser();
                            userLabelLin(user_Lin, mActivity, user);

                            List<SearchAll_Bean.DataDTO.PeopleDTO> person = searchAll_bean.getData().getPeople();
                            personLabelLin(person_Lin, mActivity, person);

                            List<SearchAll_Bean.DataDTO.CompanyDTO> company = searchAll_bean.getData().getCompany();
                            fundLabelLin(fund_Lin, mActivity, company);

                            List<SearchAll_Bean.DataDTO.TopicDTO> topic = searchAll_bean.getData().getTopic();
                            topicLabelLin(topic_Lin, mActivity, topic);

                            List<SearchAll_Bean.DataDTO.GroupDTO> group = searchAll_bean.getData().getGroup();
                            groupLabelLin(group_Lin, mActivity, group);
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
            FrameLayout head_Frame = view.findViewById(R.id.head_Frame);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            TextView nickName_Tv = view.findViewById(R.id.nickName_Tv);
            TextView content_Tv = view.findViewById(R.id.content_Tv);
            TextView follow_Tv = view.findViewById(R.id.follow_Tv);
            TextView send_Mes = view.findViewById(R.id.send_Mes);
            FlowLayout label_Flow = view.findViewById(R.id.label_Flow);
            nickName_Tv.setFilters(new InputFilter[]{new ByteLengthFilter(14)});//昵称字数
            GlideUtils.loadCircleImage(mContext, head_Img, userDTO.getAvatar(), R.mipmap.icon_logo);
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), userDTO.getNickname(), query_edit.getText().toString());
            nickName_Tv.setText(searchChange);
            isFollow(userDTO.getFollow_status(), follow_Tv, send_Mes);
            String format = String.format("发表 %1$s条  关注 %2$s人", userDTO.getArticleNum(), userDTO.getFansNum());
            content_Tv.setText(format);
            labelAllFlow(label_Flow, mContext, userDTO.getTagName());
            follow_Tv.setTag(i);
            send_Mes.setTag(i);
            head_Frame.setTag(i);
            myLin.addView(view);
            follow_Tv.setOnClickListener(v -> {
                int tag = (int) v.getTag();
                String likeId = item.get(tag).getId();
                postFollowData(likeId, follow_Tv, send_Mes);
            });
            send_Mes.setOnClickListener(v -> {
                int tag = (int) v.getTag();
                String id = item.get(tag).getId();
                String nickname = item.get(tag).getNickname();
                String avatar = item.get(tag).getAvatar();
                ChatActivity.actionStart(mActivity, id, nickname, avatar);
            });
            head_Frame.setOnClickListener(v -> {
                int tag = (int) v.getTag();
                String id = item.get(tag).getId();
                MyAndOther_Card.actionStart(mActivity, id);
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
            nickName_Tv.setFilters(new InputFilter[]{new ByteLengthFilter(14)});//昵称字数
            GlideUtils.loadCircleImage(mContext, head_Img, s.getAvatar(), R.mipmap.icon_logo);
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), s.getNickname(), query_edit.getText().toString());
            nickName_Tv.setText(searchChange);
//            company_Tv.setText(s.getCompanyName());
            originalValue(s.getCompanyName(), "暂未填写", "", company_Tv);
            labelFlow(lable_Lin, mContext, s.getTagName());
            myLin.addView(view);
            head_Img.setOnClickListener(v -> {
                String id = s.getId();
                MyAndOther_Card.actionStart(mActivity, id);
            });
            view.setOnClickListener(v -> {
                String nickname = s.getNickname();
                String id = s.getId();
                SideslipPersonDetail_Activity.startActivity(mActivity, nickname, id, "0");
            });
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
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), s.getName(), query_edit.getText().toString());
            fundName_Tv.setText(searchChange);
            fundCode_Tv.setText(s.getNumber());
            myLin.addView(view);
        }
    }

    /**
     * 资讯
     */
    public void topicLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.TopicDTO> item) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(item)) {
            return;
        }
        for (SearchAll_Bean.DataDTO.TopicDTO s : item) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.homeseek_topic, null);
            ImageView art_Img = view.findViewById(R.id.art_Img);
            TextView artName_tv = view.findViewById(R.id.artName_tv);
            TextView artContent_Tv = view.findViewById(R.id.artContent_Tv);
//            artName_tv.setText(s.getNickname());
//            artContent_Tv.setText(s.getContent());
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), s.getNickname(), query_edit.getText().toString());
            artName_tv.setText(searchChange);
            SpannableString searchChange1 = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), s.getContent(), query_edit.getText().toString());
            artContent_Tv.setText(searchChange1);
            if (!TextUtils.isEmpty(s.getImages())) {
                List<String> strings = Arrays.asList(s.getImages().split(","));
                GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0), 9);
            } else {
                art_Img.setImageResource(R.mipmap.img_pic_none_square);
            }
            myLin.addView(view);
            view.setOnClickListener(v -> {
                String articleId = s.getId();
                ArticleDetail_Activity.startActivity(mActivity, articleId, "0", true);
            });
        }
    }


    /**
     * 圈子
     */
    public void groupLabelLin(LinearLayout myLin, Context mContext, List<SearchAll_Bean.DataDTO.GroupDTO> item) {
        if (myLin != null) {
            myLin.removeAllViews();
        }
        if (ListUtils.isEmpty(item)) {
            return;
        }
        int size = item.size();
        for (int i = 0; i < size; i++) {
            SearchAll_Bean.DataDTO.GroupDTO groupDTO = item.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.homeseek_group, null);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            TextView nickName_Tv = view.findViewById(R.id.nickName_Tv);
            TextView content_Tv = view.findViewById(R.id.content_Tv);
            TextView followCount_Tv = view.findViewById(R.id.followCount_Tv);
            TextView join_Tv = view.findViewById(R.id.join_Tv);
            GlideUtils.loadCircleImage(mContext, head_Img, groupDTO.getShequnImage(), R.mipmap.icon_logo);
//            nickName_Tv.setText(groupDTO.getShequnName());
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), groupDTO.getShequnName(), query_edit.getText().toString());
            nickName_Tv.setText(searchChange);
            String format = String.format("关注 %1$s", groupDTO.getJoinNum());
            followCount_Tv.setText(format);
            content_Tv.setText(groupDTO.getShequnDes());
            String state = groupDTO.getJoinStatus();
            joinState(state, join_Tv);
            String type1 = item.get(i).getType();
            if (TextUtils.equals(type1, "1")) { //1是社群 2是兴趣组
                join_Tv.setVisibility(View.GONE);
            }
            join_Tv.setTag(i);
            view.setTag(i);
            myLin.addView(view);
            join_Tv.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                String joinStatus = item.get(tag).getJoinStatus();
                String type = item.get(tag).getType();
                if (TextUtils.equals(type, "2")) { //1是社群 2是兴趣组
                    changeState(joinStatus, join_Tv, item);
                    getJoin(item.get(tag).getId());
                }
            });
            view.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                groupOrInterest(item, tag);
            });
        }
    }

    private void groupOrInterest(List<SearchAll_Bean.DataDTO.GroupDTO> item, int tag) {
        String id = item.get(tag).getId();
        String type = item.get(tag).getType();
        String joinStatus = item.get(tag).getJoinStatus();
        if (TextUtils.equals(type, "1")) { //1是社群 2是兴趣组
            if (TextUtils.equals(joinStatus, "1")) {//1是已加入 其他都是未加入
                CheckOtherGroupDetailActivity.actionStart(mActivity, id);
            } else {
                GroupDetailActivity.actionStart(mActivity, id);
            }
        } else {
            InterestDetail_Activity.startActivity(mActivity, "0", id);
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
    public void labelAllFlow(FlowLayout myFlow, Context mContext, String tag) {
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
            itemParams.setMargins(0, 8, 0, 0);
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


    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    //加入的状态 state1已加入，其它都是未加入
    private void joinState(String state, TextView joinTv) {
        if (!TextUtils.isEmpty(state)) {
            int indexState = Integer.parseInt(state);
            if (indexState == 1) {
                joinTv.setText("已加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            } else {
                joinTv.setText("加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
            }
        }
    }


    //修改加入状态
    private void changeState(String state, TextView joinTv, List<SearchAll_Bean.DataDTO.GroupDTO> item) {
        if (!TextUtils.isEmpty(state)) {
            int indexState = Integer.parseInt(state);
            int tag = (Integer) joinTv.getTag();
            if (indexState == 1) {//1已加入 其他都是未加入
                item.get(tag).setJoinStatus("0");
                joinTv.setText("加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
            } else {
                item.get(tag).setJoinStatus("1");
                joinTv.setText("已加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dele_Tv:
                deleteHistory();
                break;
            case R.id.userSeek_Tv:
                changeTabAndRefresh(1);
                break;
            case R.id.personSeek_Tv:
                changeTabAndRefresh(2);
                break;
            case R.id.fundSeek_Tv:
                changeTabAndRefresh(3);
                break;
            case R.id.topicSeek_Tv:
                changeTabAndRefresh(4);
                break;
            case R.id.groupSeek_Tv:
                changeTabAndRefresh(5);
                break;
        }
    }

    private void changeTabAndRefresh(int index) {
        ((HomeSeek_Activity) requireActivity()).mViewPager.setCurrentItem(index);
        List<Fragment> fragments = requireActivity().getSupportFragmentManager().getFragments();
        BaseRecyclerViewSplitFragment fragment = (BaseRecyclerViewSplitFragment) fragments.get(index);
        fragment.onRefresh();
    }

    private void deleteHistory() {
        new TwoButtonDialogBlue(mActivity, "确定删除全部历史记录？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                ((HomeSeek_Activity) requireActivity()).mDbDao.deleteData();
                if (label_Flow != null) {
                    label_Flow.removeAllViews();
                }
            }

            @Override
            public void onClickLeft() {
            }
        }).show();
    }


}
