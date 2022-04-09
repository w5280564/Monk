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
import com.qingbo.monk.Slides.activity.SideslipPersonAndFund_Activity;
import com.qingbo.monk.Slides.activity.SideslipPersonDetail_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;
import java.util.List;

/**
 * 首页/侧边栏  大佬列表-tab页面
 */
public class SideslipMogul_Fragment extends BaseRecyclerViewSplitFragment {


    private String tagId;

    public static SideslipMogul_Fragment newInstance(String tagId) {
        Bundle args = new Bundle();
        args.putString("tagId", tagId);
        SideslipMogul_Fragment fragment = new SideslipMogul_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("您还未关注用户", 0, true);
    }

    @Override
    protected void initLocalData() {
        tagId = getArguments().getString("tagId");
    }

    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }

    FollowListBean homeFllowBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("tagId", tagId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Leader_List, "侧滑—大佬tab页面", requestMap, new MyOnHttpResListener() {
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
        mAdapter = new Focus_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            skipAnotherActivity(ArticleDetail_Activity.class);
            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
            String action = item.getAction();//1是社群 2是兴趣组 3是个人
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);
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
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId =item.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String articleId = item.getArticleId();
                        String type = item.getType();
                        ArticleDetail_Activity.startActivity(requireActivity(), articleId, "1", type);
                        break;
                    case R.id.send_Mes:
                        HomeFllowBean item1 = (HomeFllowBean) adapter.getItem(position);
                        ChatActivity.actionStart(mActivity, item1.getAuthorId(), item1.getAuthorName(), item1.getAvatar());
                        break;
                    case R.id.group_Img:
                       startPerson(item);
                        break;
                    case R.id.share_Img:
                        showShareDialog(item);
                        break;
                }
            }
        });


        ((Focus_Adapter) mAdapter).setOnItemImgClickLister(new Focus_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    /**
     * 人物跳转
     * @param item
     */
    private void startPerson(HomeFllowBean item) {
        String data_source = item.getData_source();//1是虚拟人物,为0是注册用户
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getAuthorName();
            String id = item.getAuthorId();
//            SideslipPersonDetail_Activity.startActivity(mActivity, nickname, id, "0");
            SideslipPersonAndFund_Activity.startActivity(mActivity, nickname, id, "0");
        }else {
            String id = item.getAuthorId();
            MyAndOther_Card.actionStart(mActivity, id);
        }
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
                    ((Focus_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
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
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 资讯分享
     */
    private void showShareDialog(HomeFllowBean item) {
        String imgUrl = item.getAvatar();
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getArticleId();
        String title = item.getTitle();
        String content = item.getContent();
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(requireActivity(), articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id(item.getAuthorId());
        mShareDialog.show();
    }
}
