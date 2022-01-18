package com.qingbo.monk.Slides.fragment;

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
import com.qingbo.monk.Slides.adapter.InterestDetail_All_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.InterestDetailAll_Bean;
import com.qingbo.monk.bean.InterestDetailAll_ListBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 兴趣圈-全部
 */
public class InterestDetail_All_Fragment extends BaseRecyclerViewSplitFragment {
    private String id;
    public static InterestDetail_All_Fragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        InterestDetail_All_Fragment fragment = new InterestDetail_All_Fragment();
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
        initSwipeRefreshLayoutAndAdapter("暂无话题", 0,false);
    }

    @Override
    protected void initLocalData() {
        id = getArguments().getString("id");
    }

    @Override
    protected void loadData() {
        getListData(true);
    }

    InterestDetailAll_ListBean interestDetailAll_listBean;

    /**
     *   action 1是社群 2是兴趣圈 3是问答广场
     * @param isShow
     */
    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("action",  "2");
        requestMap.put("id", id + "");
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.groupDetailAllTab, "兴趣圈详情-全部", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                     interestDetailAll_listBean = GsonUtil.getInstance().json2Bean(json_data, InterestDetailAll_ListBean.class);
                    if (interestDetailAll_listBean != null) {
                        handleSplitListData(interestDetailAll_listBean, mAdapter, limit);
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
        mAdapter = new InterestDetail_All_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            InterestDetailAll_Bean item = (InterestDetailAll_Bean) adapter.getItem(position);
            String articleId = item.getArticleId();
//            String type = item.getType();
            String type = "2";//1是社群 2是兴趣圈 3是个人
            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0",type);
        });
    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (interestDetailAll_listBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = interestDetailAll_listBean.getList().get(position).getAuthorId();
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId = interestDetailAll_listBean.getList().get(position).getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        InterestDetailAll_Bean item = (InterestDetailAll_Bean) adapter.getItem(position);
                        String articleId = item.getArticleId();
                        String type = item.getTopicType();
                        ArticleDetail_Activity.startActivity(requireActivity(), articleId, "1",type);
                        break;
                }
            }
        });


        ((InterestDetail_All_Adapter) mAdapter).setOnItemImgClickLister(new InterestDetail_All_Adapter.OnItemImgClickLister() {
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
                    ((InterestDetail_All_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
//                    if (followStateBena.getFollowStatus() == 0) {
//                        mAdapter.remove(position);
//                        mAdapter.notifyItemChanged(position);
//                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Img);
                    TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Count);
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

}
