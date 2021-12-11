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
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.adapter.ArticleZan_Adapter;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 文章详情-赞列表
 */
public class ArticleDetail_Zan_Fragment extends BaseRecyclerViewSplitFragment {
    private String articleId, type;

    public static ArticleDetail_Zan_Fragment newInstance(String articleId, String type) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
        ArticleDetail_Zan_Fragment fragment = new ArticleDetail_Zan_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
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
        mAdapter = new ArticleZan_Adapter();

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
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.follow_Tv:
//                        String otherUserId = homeFllowBean.getList().get(position).getAuthorId();
                        String likeId = item.getAuthorId();
                        postFollowData(likeId, position);
                        break;
                }
            }
        });


        ((ArticleZan_Adapter) mAdapter).setOnItemImgClickLister(new ArticleZan_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
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
                    ((ArticleZan_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                    if (followStateBena.getFollowStatus() == 0) {
                        mAdapter.getData().remove(position);
                        mAdapter.notifyItemChanged(position);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



}
