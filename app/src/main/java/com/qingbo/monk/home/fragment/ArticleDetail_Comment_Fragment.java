package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.ArticleDetali_CommentList_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_CommentList_Activity;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.CustomLoadMoreView;

import java.util.HashMap;
import java.util.List;

/**
 * 文章详情-评论
 */
public class ArticleDetail_Comment_Fragment extends BaseRecyclerViewSplitFragment implements View.OnClickListener {
    public String articleId, type;
    TabLayout tab;
    private EditText sendComment_Et;
//    private View release_Tv;

    public static ArticleDetail_Comment_Fragment newInstance(String articleId, String type) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
        ArticleDetail_Comment_Fragment fragment = new ArticleDetail_Comment_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        tab = requireActivity().findViewById(R.id.card_Tab);
        sendComment_Et = requireActivity().findViewById(R.id.sendComment_Et);
//        release_Tv = requireActivity().findViewById(R.id.release_Tv);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
//        initSwipeRefreshLayoutAndAdapter("暂无评论", 0,false);
    }

    @Override
    protected void initLocalData() {
        articleId = getArguments().getString("articleId");
        type = getArguments().getString("type");
    }


    @Override
    protected void loadData() {
        getListData(true);
    }

    ArticleCommentListBean articleCommentListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Article_CommentList, "获取文章评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    articleCommentListBean = GsonUtil.getInstance().json2Bean(json_data, ArticleCommentListBean.class);
                    if (articleCommentListBean != null) {
                        handleSplitListData(articleCommentListBean, mAdapter, limit);
                        String count = String.format("评论(%1$s)", articleCommentListBean.getComment_total());
                        tab.getTabAt(0).setText(count);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false);
    }


//    public void initRecyclerView() {
//        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
//        mMangaer.setOrientation(RecyclerView.VERTICAL);
//        mRecyclerView.setLayoutManager(mMangaer);
//        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mRecyclerView.setHasFixedSize(true);
//        mAdapter = new ArticleComment_Adapter(articleId,type);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener((adapter, view, position) -> {
////            skipAnotherActivity(ArticleDetail_Activity.class);
////            ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
////            String articleId = item.getArticleId();
////            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0");
//        });
//
//    }

    ArticleComment_Adapter mAdapter;
    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ArticleComment_Adapter(articleId,type);
//        mAdapter.setEmptyView(addEmptyView(emptyToastText, emptyViewImgResource));
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickLister(new ArticleComment_Adapter.OnClickLister() {
            @Override
            public void onItemClick(View view, int postion) {
                ArticleCommentBean item = (ArticleCommentBean) mAdapter.getItem(postion);
                ArticleDetali_CommentList_Activity.startActivity(requireActivity(), item,articleId,type);
            }
        });
    }



    ArticleCommentBean item;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void initEvent() {
//        release_Tv.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            item = (ArticleCommentBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = item.getId();
                    postLikedData(likeId, position);
                    break;
                case R.id.commentMore_Tv:
                    String id = item.getId();
                    ArticleDetali_CommentList_Activity.startActivity(requireActivity(), item,articleId,type);
                    break;
                case R.id.mes_Img:
                    ((ArticleDetail_Activity) requireActivity()).showInput(sendComment_Et, true);
                    setHint(item, sendComment_Et);
//                    ((ArticleDetail_Activity)requireActivity()).addComment();
                    break;
            }
        });

        ((ArticleComment_Adapter) mAdapter).setOnItemImgClickLister(this::jumpToPhotoShowActivity);
    }

    /**
     * 回复谁的评论
     *
     * @param item
     * @param view
     */
    private void setHint(ArticleCommentBean item, EditText view) {
        String s = TextUtils.equals(item.getIsAnonymous(), "0") ? item.getAuthorName() : "匿名用户";
        String format = String.format("回复：%1$s", s);
        view.setHint(format);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                addComment(articleId, type, s, item);
                break;
        }
    }


    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Mes_Like, "评论 点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CommendLikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, CommendLikedStateBena.class);
                    changeLike(likedStateBena, position);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    public void addComment(String articleId, String type, String comment, ArticleCommentBean item) {
        if (item == null) {
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        requestMap.put("commentId", item.getId());
        requestMap.put("replyerId", item.getAuthorId());
        requestMap.put("replyerName", item.getAuthorName());
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "文章-回复评论", requestMap, new MyOnHttpResListener() {
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
     *   修改点赞状态
     * @param likedStateBena
     * @param position
     */
    private void changeLike(CommendLikedStateBena likedStateBena, int position) {
        ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Img);
        TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Count);
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


}
