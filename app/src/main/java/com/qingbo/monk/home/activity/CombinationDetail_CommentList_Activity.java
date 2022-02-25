package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.CommentBean;
import com.qingbo.monk.bean.CommentListBean;
import com.qingbo.monk.home.adapter.CommentDetail_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
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

import butterknife.BindView;

/**
 * 仓位组合——评论回复详情
 */
public class CombinationDetail_CommentList_Activity extends BaseRecyclerViewSplitActivity implements View.OnClickListener {

    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @BindView(R.id.sendComment_Et)
    EditText sendComment_Et;
    @BindView(R.id.release_Tv)
    TextView release_Tv;
    private ArticleCommentBean item;
    private ImageView topFollow_Img;
    private TextView topFollow_Count;
    private ImageView topMes_Img;

    boolean isTopComment = false;//回复头部评论，或者列表评论 true是回复列表
    private String id;

    /**
     * @param context
     * @param item
     * @param id      策略ID 用于 回复评论
     */
    public static void startActivity(Context context, ArticleCommentBean item, String id) {
        Intent intent = new Intent(context, CombinationDetail_CommentList_Activity.class);
        intent.putExtra("item", (Serializable) item);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detali_comment_list;
    }

    @Override
    protected void initLocalData() {
        item = (ArticleCommentBean) getIntent().getSerializableExtra("item");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//弹起键盘不遮挡布局，背景布局不会顶起
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getServerData();
    }

    @Override
    protected void initEvent() {
        release_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        if (item != null) {
            getListData(true, item.getId());
        }
    }

    CommentListBean commentListBean;

    private void getListData(boolean isShow, String commentId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", commentId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.CommentChildren_List, "仓位组合-回复评论列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    commentListBean = GsonUtil.getInstance().json2Bean(json_data, CommentListBean.class);
                    if (commentListBean != null) {
                        handleSplitListData(commentListBean, mAdapter, limit);
                        if (page == 1) {
                            addHeadView();
                        }
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    CommentBean commentItem;

    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CommentDetail_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            commentItem = (CommentBean) adapter.getItem(position);
            if (this.commentItem == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = this.commentItem.getId();
                    position += 1;
                    postLikedData(likeId, position);
                    break;
                case R.id.head_Img:
                    String authorId1 = item.getAuthorId();
                    String id1 = PrefUtil.getUser().getId();
                    if (!TextUtils.equals(authorId1, id1)) {
                        MyAndOther_Card.actionStart(mActivity, authorId1);
                    }
                    break;
//                case R.id.mes_Img:
//                String authorId = commentItem.getAuthorId();
//                boolean my = isMy(authorId);
//                if (!my) {
//                    isTopComment = true;
//                    showInput(sendComment_Et);
//                    String isAnonymous = this.commentItem.getIsAnonymous();
//                    String authorName = this.commentItem.getAuthorName();
//                    setHint(isAnonymous, authorName, sendComment_Et);
//                }
//                    break;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_Img:
                String likeId = commentListBean.getCommentData().getId();
                postLikedData(likeId, -1);
                break;
            case R.id.topMes_Img:
                String authorId = commentListBean.getCommentData().getAuthorId();
                boolean my = isMy(authorId);
                if (!my) {
                    isTopComment = false;
                    showInput(sendComment_Et);
                    String isAnonymous = commentListBean.getCommentData().getIsAnonymous();
                    String authorName = commentListBean.getCommentData().getAuthorName();
                    setHint(isAnonymous, authorName, sendComment_Et);
                }
                break;
            case R.id.release_Tv:
                sendMes();
                break;
            case R.id.head_Img:
                String authorId1 = item.getAuthorId();
                String id1 = PrefUtil.getUser().getId();
                if (!TextUtils.equals(authorId1, id1)) {
                    MyAndOther_Card.actionStart(mActivity, authorId1);
                }
                break;
        }
    }

    /**
     * 发送评论
     */
    private void sendMes() {
        String s = sendComment_Et.getText().toString();
        if (TextUtils.isEmpty(s)) {
            T.s("评论不能为空", 2000);
            return;
        }
        if (TextUtils.isEmpty(id)) {
            T.s("文章ID是空", 2000);
            return;
        }
        String commentID, authorId, authorName1 = "";
//        if (isTopComment) {
//            commentID = commentItem.getId();
//            authorId = commentItem.getAuthorId();
//            authorName1 = commentItem.getAuthorName();
//        } else {
        CommentBean commentData = commentListBean.getCommentData();
        commentID = commentData.getId();
        authorId = commentData.getAuthorId();
        authorName1 = commentData.getAuthorName();
//        }
        addComment(id, s, commentID, authorId, authorName1);
    }

    private void addHeadView() {
        View myView = LayoutInflater.from(this).inflate(R.layout.commentlist_top, null);
        ImageView head_Img = myView.findViewById(R.id.head_Img);
        TextView nickName_Tv = myView.findViewById(R.id.nickName_Tv);
        TextView content_Tv = myView.findViewById(R.id.content_Tv);
        TextView time_Tv = myView.findViewById(R.id.time_Tv);
        topFollow_Count = myView.findViewById(R.id.follow_Count);
        TextView mes_Count = myView.findViewById(R.id.mes_Count);
        topFollow_Img = myView.findViewById(R.id.follow_Img);
        topMes_Img = myView.findViewById(R.id.topMes_Img);
        viewTouchDelegate.expandViewTouchDelegate(topFollow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(topMes_Img, 50);
        topFollow_Img.setOnClickListener(this);
        topMes_Img.setOnClickListener(this);
        head_Img.setOnClickListener(this);
        mAdapter.addHeaderView(myView);
        if (commentListBean != null) {
            CommentBean commentData = commentListBean.getCommentData();
            String is_anonymous = commentData.getIsAnonymous();//1是匿名
            if (TextUtils.equals(is_anonymous, "1")) {
                nickName_Tv.setText("匿名用户");
                head_Img.setBackgroundResource(R.mipmap.icon_logo);
                head_Img.setEnabled(false);
            } else {
                GlideUtils.loadCircleImage(mContext, head_Img, commentData.getAvatar(), R.mipmap.icon_logo);
                nickName_Tv.setText(commentData.getAuthorName());
            }
            content_Tv.setText(commentData.getComment());
            String userDate = DateUtil.getUserDate(commentData.getCreateTime());
            time_Tv.setText(userDate);
            topFollow_Count.setText(commentData.getLikedNum());
            mes_Count.setText(commentListBean.getCount() + "");
            isLike(commentData.getLikedStatus(), commentData.getLikedNum(), topFollow_Img, topFollow_Count);
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


    private void postLikedData(String likeId, int pos) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Mes_Like, "评论列表 点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CommendLikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, CommendLikedStateBena.class);
                    if (pos == -1) {
                        changeLike(likedStateBena, topFollow_Img, topFollow_Count);
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
    private void changeLike(CommendLikedStateBena likedStateBena, ImageView follow_Img, TextView follow_Count) {
        if (likedStateBena != null) {
            //0取消点赞成功，1点赞成功
            int nowLike;
            nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
            if (likedStateBena.getLike_status() == 0) {
                nowLike -= 1;
                follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
            } else if (likedStateBena.getLike_status() == 1) {
                follow_Img.setBackgroundResource(R.mipmap.dianzan);
                nowLike += 1;
            }
            follow_Count.setText(nowLike + "");
        }
    }

    public void addComment(String id, String comment, String commentId, String replyerId, String replyerName) {
        if (item == null) {
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("comment", comment);
        requestMap.put("is_anonymous", "0");
        requestMap.put("commentId", commentId);
        requestMap.put("replyerId", replyerId);
        requestMap.put("replyerName", replyerName);
        HttpSender httpSender = new HttpSender(HttpUrl.Combination_AddComment, "仓位组合-添加评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    sendComment_Et.setText("");
                    sendComment_Et.setHint("");
                    getServerData();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
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
     */
    public void showInput(View editView) {
        String id = PrefUtil.getUser().getId();
        String authorId = commentListBean.getCommentData().getAuthorId();
        if (TextUtils.equals(id, authorId)) {//是自己不能评论
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
    }

    /**
     * 回复谁的评论提示语
     *
     * @param view
     */
    private void setHint(String isAnonymous, String authorName, EditText view) {
        String s = TextUtils.equals(isAnonymous, "0") ? authorName : "匿名用户";
        String format = String.format("回复：%1$s", s);
        view.setHint(format);
    }

}