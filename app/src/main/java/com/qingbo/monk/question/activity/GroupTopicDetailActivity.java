package com.qingbo.monk.question.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.baseview.AppBarStateChangeListener;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.qingbo.monk.home.fragment.ArticleDetail_Comment_Fragment;
import com.qingbo.monk.home.fragment.ArticleDetail_Zan_Fragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 社群话题详情
 */
public class GroupTopicDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.group_Img)
    ImageView person_Img;
    @BindView(R.id.group_Name)
    TextView person_Name;
    @BindView(R.id.tv_role)
    TextView tv_role;
    @BindView(R.id.time_Tv)
    TextView time_Tv;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.ll_container_answer)
    LinearLayout ll_container_answer;
    @BindView(R.id.title_Tv)
    TextView title_Tv;
    @BindView(R.id.content_Tv)
    TextView content_Tv;
    @BindView(R.id.tv_answer)
    TextView tv_answer;
    @BindView(R.id.follow_Tv)
    TextView follow_Tv;
    @BindView(R.id.send_Mes)
    TextView send_Mes;
    @BindView(R.id.nine_grid)
    RecyclerView mNineView;
    @BindView(R.id.follow_Img)
    ImageView follow_Img;
    @BindView(R.id.follow_Count)
    TextView follow_Count;
    @BindView(R.id.mes_Img)
    ImageView mes_Img;
    @BindView(R.id.mes_Count)
    TextView mes_Count;
    @BindView(R.id.card_Tab)
    TabLayout card_Tab;
    @BindView(R.id.card_ViewPager)
    ViewPager card_ViewPager;
    @BindView(R.id.appLayout)
    AppBarLayout appLayout;
    @BindView(R.id.title_Img)
    ImageView title_Img;
    @BindView(R.id.titleNickName_Tv)
    TextView titleNickName_Tv;
    @BindView(R.id.center_Tv)
    TextView center_Tv;
    @BindView(R.id.titleFollow_Tv)
    TextView titleFollow_Tv;
    @BindView(R.id.titleSend_Mes)
    TextView titleSend_Mes;
    @BindView(R.id.titleSeek_Img)
    ImageView titleSeek_Img;
    @BindView(R.id.sendComment_Et)
    EditText sendComment_Et;
    @BindView(R.id.release_Tv)
    public TextView release_Tv;
    @BindView(R.id.back_Tv)
    TextView back_Tv;


    private String articleId;
    private String isShowTop;
    private String type;
    boolean isReply = false;

    private int topicType;
    private String role_self;

    /**
     * @param context
     * @param articleId
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String articleId, String isShowTop, String type, OwnPublishBean topicDetailBean,int topicType,String role_self) {
        Intent intent = new Intent(context, GroupTopicDetailActivity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("type", type);
        intent.putExtra("topicDetailBean", topicDetailBean);
        intent.putExtra("topicType", topicType);
        intent.putExtra("role_self", role_self);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_topic_detail;
    }


    @Override
    protected void initLocalData() {
        articleId = getIntent().getStringExtra("articleId");
        isShowTop = getIntent().getStringExtra("isShowTop");
        type = getIntent().getStringExtra("type");
        OwnPublishBean topicDetailBean = (OwnPublishBean) getIntent().getSerializableExtra("topicDetailBean");
        topicType = getIntent().getIntExtra("topicType",0);
        role_self = getIntent().getStringExtra("role_self");
        if (topicDetailBean!=null) {
            showDetailData(topicDetailBean);
        }
    }

    private void showDetailData(OwnPublishBean item) {
        viewTouchDelegate.expandViewTouchDelegate(follow_Img,100);

        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }

        follow_Count.setText(item.getLikecount());
        mes_Count.setText(item.getCommentcount());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);



        if (topicType==0) {
            tv_status.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);

//            String publish_user_id = item.getAuthorId();
//            if (SharePref.user().getUserId().equals(publish_user_id)) {
//                iv_delete.setVisibility(View.VISIBLE);
//                viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
//            }else{
//                String publish_role = item.getRole();
//                if ("3".equals(role_self)) {//1管理员2合伙人0一般用户3群主
//                    iv_delete.setVisibility(View.VISIBLE);
//                    viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
//                }else if ("2".equals(role_self)) {
//                    if ("1".equals(publish_role)||"0".equals(publish_role)) {
//                        iv_delete.setVisibility(View.VISIBLE);
//                        viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
//                    }
//                }else if ("1".equals(role_self)) {//1管理员2合伙人0一般用户3群主
//                    if ("0".equals(publish_role)) {
//                        iv_delete.setVisibility(View.VISIBLE);
//                        viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
//                    }
//                }else{
//                    iv_delete.setVisibility(View.GONE);
//                }
//            }
        }else{
            iv_delete.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);
//            String status = item.getStatus();//0待审核 1通过 2未通过
//            if (TextUtils.equals(status, "0")) {
//                tv_status.setVisibility(View.VISIBLE);
//                tv_status.setText("待审核");
//                setDrawableLeft(R.mipmap.weishenhe,tv_status);
//            } else if(TextUtils.equals(status, "1")){
//                tv_status.setVisibility(View.VISIBLE);
//                tv_status.setText("审核通过");
//                setDrawableLeft(R.mipmap.shenhetongguo,tv_status);
//            } else if(TextUtils.equals(status, "2")){
//                tv_status.setVisibility(View.VISIBLE);
//                setDrawableLeft(R.mipmap.weitongguo,tv_status);
//                tv_status.setText("未通过");
//            } else{
//                tv_status.setVisibility(View.GONE);
//            }
        }


        String topicType = item.getTopicType();
        if ("1".equals(topicType)) {//1是话题2是问答
            if (!StringUtil.isBlank(item.getTitle())) {
                title_Tv.setVisibility(View.VISIBLE);
                title_Tv.setText(item.getTitle());
            }else{
                title_Tv.setVisibility(View.GONE);
            }
            handleCommonData(item.getAvatar(),item.getNickname(),item.getContent(),item.getRole());
            handleImageList(item, mNineView);
            ll_container_answer.setVisibility(View.GONE);
        }else{
            title_Tv.setVisibility(View.GONE);
            List<OwnPublishBean.DetailDTO> details = item.getDetail();
            if (!ListUtils.isEmpty(details)) {
                ll_container_answer.setVisibility(View.VISIBLE);
                OwnPublishBean.DetailDTO answerObj = details.get(0);
                handleCommonData(answerObj.getAvatar(),answerObj.getNickname(),answerObj.getAnswerContent(),answerObj.getRole());
                createQuestionList(ll_container_answer,item);
            }else{
                handleCommonData(item.getAvatar(),item.getNickname(),item.getContent(),item.getRole());
                handleImageList(item, mNineView);
                ll_container_answer.setVisibility(View.GONE);
            }
        }
    }


    private void handleCommonData(String headImg,String headName,String content,String role) {
        GlideUtils.loadCircleImage(mContext, person_Img, headImg);
        person_Name.setText(headName);
        GlideUtils.loadCircleImage(mContext, title_Img, headImg);
        titleNickName_Tv.setText(headName);

        if (!StringUtil.isBlank(content)) {
            content_Tv.setVisibility(View.VISIBLE);
            content_Tv.setText(content);
        }else{
            content_Tv.setVisibility(View.GONE);
        }

        if ("1".equals(role)) {//1管理员2合伙人0一般用户3群主
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("管理员");
            person_Name.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else if("2".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("合伙人");
            person_Name.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else if("3".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("群主");
            person_Name.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else{
            tv_role.setVisibility(View.GONE);
            person_Name.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_444444));
        }
    }


    /**
     * 提问
     */
    private void createQuestionList(LinearLayout ll_container_answer, OwnPublishBean item ) {
        ll_container_answer.removeAllViews();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_answer, null);
        TextView tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
        TextView tv_answer_content = itemView.findViewById(R.id.tv_answer_content);
        RecyclerView mNineView = itemView.findViewById(R.id.nine_grid_answer);
        tv_answer_name.setText(item.getNickname());
        tv_answer_content.setText("提问："+item.getContent());

        handleImageList(item, mNineView);
        ll_container_answer.addView(itemView);
    }

    private void handleImageList(OwnPublishBean item, RecyclerView mNineView) {
        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        List<String> strings = new ArrayList<>();
        mNineView.setLayoutManager(new NineGridLayoutManager(mContext));
        mNineView.setAdapter(nineGridAdapter);
        //多张图片
        if (!TextUtils.isEmpty(item.getImages())) {
            mNineView.setVisibility(View.VISIBLE);
            String[] imgS = item.getImages().split(",");
            strings.addAll(Arrays.asList(imgS));
            nineGridAdapter.setNewData(strings);
            nineGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    jumpToPhotoShowActivity(position, strings);
                }
            });
        } else {
            mNineView.setVisibility(View.GONE);
            nineGridAdapter.setNewData(null);
        }
    }


    private void setDrawableLeft(int mipmap,TextView status) {
        Drawable drawableLeft = mContext.getResources().getDrawable(
                mipmap);
        status.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }





    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//弹起键盘不遮挡布局，背景布局不会顶起
    }

    @Override
    protected void initEvent() {
        follow_Tv.setOnClickListener(this);
        follow_Img.setOnClickListener(this);
        titleFollow_Tv.setOnClickListener(this);
        appLayout.addOnOffsetChangedListener(new appLayoutListener());
        mes_Img.setOnClickListener(this);
        release_Tv.setOnClickListener(this);
        back_Tv.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getUserDetail(false);
    }

    @Override
    public void onClick(View v) {
        if (homeFoucsDetail_bean == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.back_Tv:
                finish();
                break;
            case R.id.follow_Tv:
                String authorId = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                postFollowData(authorId, follow_Tv, send_Mes);
                break;
            case R.id.follow_Img:
                String likeId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
                postLikedData(likeId);
                break;
            case R.id.titleFollow_Tv:
                String authorId1 = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                postFollowData(authorId1, titleFollow_Tv, titleSend_Mes);
                break;
            case R.id.mes_Img:
                showInput(sendComment_Et, false);
                sendComment_Et.setHint("");
                break;
            case R.id.release_Tv:
                String s = sendComment_Et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    T.s("评论不能为空", 2000);
                    return;
                }
                if (TextUtils.isEmpty(articleId) || TextUtils.isEmpty(type)) {
                    T.s("文章ID是空", 2000);
                    return;
                }
                if (isReply) {
                    ArticleDetail_Comment_Fragment o = (ArticleDetail_Comment_Fragment) tabFragmentList.get(0);
                    o.onClick(release_Tv);
                } else {
                    addComment(articleId, type, s);
                }
                break;
            case R.id.iv_delete:

                break;
        }
    }


    /**
     * 是否是自己
     * @param authorId2
     * @return
     */
    public boolean isMy(String authorId2){
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {//是自己不能评论
            return true;
        }
        return false;
    }

    /**
     * 点击弹出键盘
     *
     * @param editView
     * @param editView 是否回复评论  true是对评论回复
     */
    public void showInput(View editView, boolean isReply) {
        this.isReply = isReply;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
    }

    private List<Object> tabFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    private void initTab() {
        List<String> tabsList = new ArrayList<>();
        tabsList.add("评论");
        tabsList.add("赞");
        card_Tab.setTabMode(TabLayout.MODE_AUTO);
        card_Tab.setTabIndicatorFullWidth(false);//下标跟字一样宽
        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        card_Tab.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
//        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity,R.color.text_color_444444));
        //添加tab
        int sizes = tabsList.size();
        for (int i = 0; i < sizes; i++) {
            card_Tab.addTab(card_Tab.newTab().setText(tabsList.get(i)));
        }
        String articleId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
        String type = homeFoucsDetail_bean.getData().getDetail().getType();
        tabFragmentList.add(ArticleDetail_Comment_Fragment.newInstance(articleId, type));
        tabFragmentList.add(ArticleDetail_Zan_Fragment.newInstance(articleId, type));

        card_ViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return (Fragment) tabFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return tabFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabsList.get(position);
            }
        });
        card_Tab.setupWithViewPager(card_ViewPager, false);
    }


    HomeFoucsDetail_Bean homeFoucsDetail_bean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        HttpSender httpSender = new HttpSender(HttpUrl.User_Article_Detail, "个人文章详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFoucsDetail_bean = GsonUtil.getInstance().json2Bean(json_root, HomeFoucsDetail_Bean.class);
                    if (homeFoucsDetail_bean != null) {
                        HomeFoucsDetail_Bean.DataDTO.DetailDTO detailData = homeFoucsDetail_bean.getData().getDetail();
                        isFollow(detailData.getFollowStatus(), follow_Tv, send_Mes);
                        follow_Count.setText(detailData.getLikedNum());
                        mes_Count.setText(detailData.getCommentNum());
                        isLike(detailData.getLikedStatus(), detailData.getLikedNum(), follow_Img, follow_Count);
                        initTab();
                        isChangeFold();
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void postFollowData(String otherUserId, TextView followView, View sendView) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    if (homeFoucsDetail_bean != null) {
                        Integer followStatus = followStateBena.getFollowStatus();
                        homeFoucsDetail_bean.getData().getDetail().setFollowStatus(followStatus);
                        isFollow(followStatus, follow_Tv, send_Mes);
                        isFollow(followStatus, titleFollow_Tv, titleSend_Mes);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void postLikedData(String likeId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    if (likedStateBena != null) {
                        //0取消点赞成功，1点赞成功
                        int nowLike;
                        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
                        if (likedStateBena.getLiked_status() == 0) {
                            nowLike -= 1;
                            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
                        } else if (likedStateBena.getLiked_status() == 1) {
                            follow_Img.setBackgroundResource(R.mipmap.dianzan);
                            nowLike += 1;
                        }
                        follow_Count.setText(nowLike + "");
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



    public void addComment(String articleId, String type, String comment) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "文章-添加评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    sendComment_Et.setText("");
                    sendComment_Et.setHint("");
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }

    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(nowLike + "");
    }

    /**
     * 大V标签
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
     * 处理详情折叠状态
     */
    private class appLayoutListener extends AppBarStateChangeListener {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.EXPANDED) {
                //展开状态
                title_Img.setVisibility(View.GONE);
                titleNickName_Tv.setVisibility(View.GONE);
                center_Tv.setVisibility(View.VISIBLE);
                titleFollow_Tv.setVisibility(View.GONE);
                titleSend_Mes.setVisibility(View.GONE);
                titleSeek_Img.setVisibility(View.VISIBLE);
            } else if (state == State.COLLAPSED) {
                //折叠状态
                title_Img.setVisibility(View.VISIBLE);
                titleNickName_Tv.setVisibility(View.VISIBLE);
                center_Tv.setVisibility(View.GONE);
                titleSeek_Img.setVisibility(View.GONE);
                if (homeFoucsDetail_bean != null) {
                    Integer followStatus = homeFoucsDetail_bean.getData().getDetail().getFollowStatus();
                    isFollow(followStatus, titleFollow_Tv, titleSend_Mes);
                }
            } else {
                //中间状态
            }
        }
    }

    /**
     * 收起整个折叠页
     */
    private void isChangeFold() {
        if (TextUtils.equals(isShowTop, "1")) {
            appLayout.setExpanded(false);
        }
    }


}