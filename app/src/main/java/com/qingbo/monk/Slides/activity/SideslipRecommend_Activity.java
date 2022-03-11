package com.qingbo.monk.Slides.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.CommentBean;
import com.qingbo.monk.bean.CommentListBean;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.HomeInterestBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.CommentDetail_Adapter;
import com.qingbo.monk.home.adapter.Follow_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.activity.GroupDetailActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 侧滑——推荐
 */
public class SideslipRecommend_Activity extends BaseRecyclerViewSplitActivity implements View.OnClickListener {
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private String type;
    private LinearLayout interest_Lin;
    private ImageView change_Img;


    /**
     * @param context
     */
    public static void startActivity(Context context, String type) {
        Intent intent = new Intent(context, SideslipRecommend_Activity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_recommend;
    }

    @Override
    protected void initLocalData() {
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        getServerData();
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getServerData();
    }


    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
        getInterestLab(false);
    }

    private void getInterestLab(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
//        requestMap.put("page", page + "");
        requestMap.put("limit", "3");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_Group, "推荐--兴趣组列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeInterestBean interestBean = new Gson().fromJson(json_root, HomeInterestBean.class);
                    if (interestBean != null) {
                        labelList(mActivity, interest_Lin, interestBean.getData());
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    FollowListBean homeFllowBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Recommend_List, "首页-推荐", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFllowBean = GsonUtil.getInstance().json2Bean(json_data, FollowListBean.class);
                    if (homeFllowBean != null) {
                        handleSplitListData(homeFllowBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Follow_Adapter();
        addHeadView();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
            String action = item.getAction();//1是社群 2是兴趣组 3是个人
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(mActivity, articleId, "0", type);
        });
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = item.getAuthorId();
                        position += 1;
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId = item.getArticleId();
                        position += 1;
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String articleId = item.getArticleId();
                        String type = item.getType();
                        ArticleDetail_Activity.startActivity(mActivity, articleId, "1", type);
                        break;
                    case R.id.group_Img:
                        jumpPage(item);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, item.getAuthorId(), item.getAuthorName(), item.getAvatar());
                        break;
                }
            }
        });


        ((Follow_Adapter) mAdapter).setOnItemImgClickLister(new Follow_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    //推荐各种类型跳转页面
    private void jumpPage(HomeFllowBean item) {
        String isAnonymous = item.getIsAnonymous();//1是匿名
        if (TextUtils.equals(isAnonymous, "1")) {
            return;
        }
        String action = item.getAction(); //1是社群 2是兴趣组 3是个人
        if (TextUtils.equals(action, "1")) {
            String shequnId = item.getShequnId();
            GroupDetailActivity.actionStart(mActivity, shequnId);
        } else if (TextUtils.equals(action, "2")) {
            String groupId = item.getGroupId();
            InterestDetail_Activity.startActivity(mActivity, "0", groupId);
        } else if (TextUtils.equals(action, "3")) {
            String authorId = item.getAuthorId();
            MyAndOther_Card.actionStart(mActivity, authorId);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_Tv:
                rotate(change_Img);
                getInterestLab(false);
                break;
        }
    }

    private void rotate(View view) {
        Long animateTime = 500L;
        RotateAnimation animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        //设置无线循环
        animation.restrictDuration(Animation.INFINITE);
        //设置匀速旋转
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(animateTime);
        view.startAnimation(animation);
    }


    private void addHeadView() {
        View myView = LayoutInflater.from(this).inflate(R.layout.sidesliprecommend_top, null);
        interest_Lin = myView.findViewById(R.id.interest_Lin);
        TextView change_Tv = myView.findViewById(R.id.change_Tv);
        change_Img = myView.findViewById(R.id.change_Img);
        change_Tv.setOnClickListener(this);
        mAdapter.addHeaderView(myView);
    }

    /**
     * 首页我的兴趣组
     *
     * @param context
     * @param myFlex
     * @param model
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void labelList(Context context, LinearLayout myFlex, HomeInterestBean.DataDTO model) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        } else {
            return;
        }
        int size = model.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.interest_lable, null);
            HomeInterestBean.DataDTO.ListDTO data = model.getList().get(i);
            ImageView head_Tv = myView.findViewById(R.id.head_Tv);
            TextView shares_Tv = myView.findViewById(R.id.shares_Tv);
            TextView add_Tv = myView.findViewById(R.id.add_Tv);
            TextView join_Tv = myView.findViewById(R.id.join_Tv);
            String headUrl = data.getGroupImage();
            GlideUtils.loadCircleImage(context, head_Tv, headUrl, R.mipmap.icon_logo);
            shares_Tv.setText(data.getGroupName());
            add_Tv.setText(data.getJoinNum() + "加入");
            int state = data.getJoinStatus();
            joinState(state, join_Tv);
            myView.setTag(i);
            join_Tv.setTag(i);
            myFlex.addView(myView);
            myView.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                String id = model.getList().get(tag).getId();
                InterestDetail_Activity.startActivity(context, "0", id);
            });
            join_Tv.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                int stateIndex = model.getList().get(tag).getJoinStatus();
                changeState(stateIndex, join_Tv, model);
                getJoin(model.getList().get(tag).getId());
            });
        }
    }

    //加入的状态
    private void joinState(int state, TextView joinTv) {
        if (state == 1) { //1已加入 其他都是未加入
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }

    //修改加入状态
    private void changeState(int state, TextView joinTv, HomeInterestBean.DataDTO model) {
        int tag = (Integer) joinTv.getTag();
        if (state == 1) {//1已加入 其他都是未加入
            model.getList().get(tag).setJoinStatus(0);
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        } else {
            model.getList().get(tag).setJoinStatus(1);
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        }
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
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
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

    private void postFollowData(String otherUserId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
                    TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
                    ((Follow_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);

                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    private void postLikedData(String likeId, int pos) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    if (pos == -1) {
//                        changeLike(likedStateBena, topFollow_Img, topFollow_Count);
                    } else {
                        ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, pos, R.id.follow_Img);
                        TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, pos, R.id.follow_Count);
                        changeLike(likedStateBena, follow_Img, follow_Count);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * 修改点赞状态
     *
     * @param likedStateBena
     * @param follow_Img
     * @param follow_Count
     */
    private void changeLike(LikedStateBena likedStateBena, ImageView follow_Img, TextView follow_Count) {
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