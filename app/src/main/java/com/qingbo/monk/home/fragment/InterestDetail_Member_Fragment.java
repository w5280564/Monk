package com.qingbo.monk.home.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.InterestMember_Bean;
import com.qingbo.monk.bean.InterestMember_ListBean;
import com.qingbo.monk.home.adapter.InterestMember_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 兴趣组详情-成员
 */
public class InterestDetail_Member_Fragment extends BaseRecyclerViewSplitFragment {
    private String id;
    private TabLayout tab;

    /**
     * @param id 兴趣组ID
     * @return
     */
    public static InterestDetail_Member_Fragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        InterestDetail_Member_Fragment fragment = new InterestDetail_Member_Fragment();
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
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无评论", 0, true);
    }

    @Override
    protected void initLocalData() {
        id = getArguments().getString("id");
    }


    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }

    InterestMember_ListBean interestMember_listBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_AllMember, "兴趣组成员", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    interestMember_listBean = GsonUtil.getInstance().json2Bean(json_data, InterestMember_ListBean.class);
                    if (interestMember_listBean != null) {
                        handleSplitListData(interestMember_listBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {
        page = 1;
        getListData(false);
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
        mAdapter = new InterestMember_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            skipAnotherActivity(ArticleDetail_Activity.class);
//            ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
//            String articleId = item.getArticleId();
//            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0");
        });

    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new InterestMember_Adapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                InterestMember_Bean item = (InterestMember_Bean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String likeId = item.getId();
                        postFollowData(likeId, position);
                        break;
                    case R.id.head_Img:
                        String id = item.getId();
                        MyAndOther_Card.actionStart(mActivity, id);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, item.getId(), item.getNickname(), item.getAvatar());
                        break;
                }
            }
        });


        ((InterestMember_Adapter) mAdapter).setOnItemImgClickLister(new InterestMember_Adapter.OnItemImgClickLister() {
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
                    TextView content_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.content_Tv);
                    Integer followStatus = followStateBena.getFollowStatus();
                    ((InterestMember_Adapter) mAdapter).isFollow(followStatus + "", follow_Tv, send_Mes);

                    changeFans(position, followStatus, content_Tv);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 关注人的粉丝数量
     * @param pos
     * @param followStatus
     * @param textView
     */
    private void changeFans(int pos, int followStatus, TextView textView) {
        if (interestMember_listBean != null) {
            int nowLike;
            String fansNum = interestMember_listBean.getList().get(pos).getFansNum();
            nowLike = TextUtils.isEmpty(fansNum) ? 0 : Integer.parseInt(fansNum);
            if (followStatus == 0 || followStatus == 3) {
                nowLike -= 1;
                interestMember_listBean.getList().get(pos).setFansNum(nowLike + "");
            } else if (followStatus == 2 || followStatus == 4) {
                nowLike += 1;
                interestMember_listBean.getList().get(pos).setFansNum(nowLike + "");
            }
            String followerNum = interestMember_listBean.getList().get(pos).getFollowerNum();
            String format = String.format("关注%1$s人，粉丝%2$s人",  followerNum, nowLike);
            textView.setText(format);
        }
    }


}
