package com.qingbo.monk.home.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipPersonDetail_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.CommentListBean;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.qingbo.monk.home.adapter.Combination_Adapter;
import com.qingbo.monk.home.adapter.CommentDetail_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 文章——评论回复列表
 */
public class ArticleDetali_CommentList_Activity extends BaseRecyclerViewSplitActivity {

    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    private ArticleCommentBean item;


    /**
     * @param context
     */
    public static void startActivity(Context context, ArticleCommentBean item) {
        Intent intent = new Intent(context, ArticleDetali_CommentList_Activity.class);
        intent.putExtra("item", (Serializable) item);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detali_comment_list;
    }

    @Override
    protected void initLocalData() {
        item = (ArticleCommentBean) getIntent().getSerializableExtra("item");
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", true);
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
    protected void getServerData() {
        if (item != null) {
            getListData(true, item.getId());
        }
    }

    private void getListData(boolean isShow, String commentId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", commentId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.CommentChildren_List, "文章-回复评论列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CommentListBean commentListBean = GsonUtil.getInstance().json2Bean(json_data, CommentListBean.class);
                    if (commentListBean != null) {
                        handleSplitListData(commentListBean, mAdapter, limit);
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
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CommentDetail_Adapter();
        addHeadView();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
//            String articleId = item.getArticleId();
//            String type = item.getType();
//            ArticleDetail_Activity.startActivity(this, articleId, "0", type);
        });
    }

    private void addHeadView(){
        View myView = LayoutInflater.from(this).inflate(R.layout.commentlist_top, null);
        ImageView head_Img = myView.findViewById(R.id.head_Img);
        TextView nickName_Tv = myView.findViewById(R.id.nickName_Tv);
        TextView content_Tv = myView.findViewById(R.id.content_Tv);
        TextView time_Tv = myView.findViewById(R.id.time_Tv);
        TextView follow_Count = myView.findViewById(R.id.follow_Count);
        TextView mes_Count = myView.findViewById(R.id.mes_Count);
        ImageView follow_Img = myView.findViewById(R.id.follow_Img);
        ImageView mes_Img = myView.findViewById(R.id.mes_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
//        String is_anonymous = item.getIsAnonymous();//1是匿名
//        if (TextUtils.equals(is_anonymous, "1")) {
//            nickName_Tv.setText("匿名用户");
//            head_Img.setBackgroundResource(R.mipmap.icon_logo);
//            head_Img.setEnabled(false);
//        } else {
//            GlideUtils.loadCircleImage(mContext, head_Img, item.getAvatar(), R.mipmap.icon_logo);
//            nickName_Tv.setText(item.getAuthorName());
//        }
//        content_Tv.setText(item.getComment());
//        String userDate = DateUtil.getUserDate(item.getCreateTime());
//        time_Tv.setText(userDate);
//        follow_Count.setText(item.getLikedNum());
//        mes_Count.setText(item.getChildrenNum());
//        isLike(item.getLikedStatus(), item.getLikedNum(), follow_Img, follow_Count);


        mAdapter.addHeaderView(myView);
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



}