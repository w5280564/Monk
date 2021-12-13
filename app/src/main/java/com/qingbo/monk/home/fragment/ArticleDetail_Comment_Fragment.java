package com.qingbo.monk.home.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;
import java.util.List;

/**
 * 文章详情-评论
 */
public class ArticleDetail_Comment_Fragment extends BaseRecyclerViewSplitFragment {
    private String articleId, type;
    TabLayout tab;

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
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无评论", false);
    }

    @Override
    protected void initLocalData() {
        articleId = getArguments().getString("articleId");
        type = getArguments().getString("type");
    }


    @Override
    protected void getServerData() {
        getListData(true);
    }

    ArticleCommentListBean articleCommentListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", 1 + "");
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Article_CommentList, "获取文章评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    articleCommentListBean = GsonUtil.getInstance().json2Bean(json_data, ArticleCommentListBean.class);
                    if (articleCommentListBean != null) {
                        handleSplitListData(articleCommentListBean, mAdapter, limit);
                        String count = String.format("评论(%1$s)", articleCommentListBean.getCount());
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


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ArticleComment_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            skipAnotherActivity(HomeFocus_Activity.class);
//            ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
//            String articleId = item.getArticleId();
//            HomeFocus_Activity.startActivity(requireActivity(), articleId, "0");
        });

    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = item.getId();
                    postLikedData(likeId, position);
                    break;
                case R.id.commentMore_Tv:
                    T.s("点击查看更多回复", 1000);
                    break;
            }
        });


        ((ArticleComment_Adapter) mAdapter).setOnItemImgClickLister((position, strings) -> jumpToPhotoShowActivity(position, strings));
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
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

}
