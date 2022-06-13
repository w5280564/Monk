package com.qingbo.monk.Slides.fragment;

import android.graphics.drawable.Drawable;
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
import com.qingbo.monk.Slides.adapter.Interest_MoreItem;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;
import com.qingbo.monk.bean.MyDynamic_More_ListBean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 兴趣组-全部
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
    protected void onRefreshData() {
        page = 1;
        getListData(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false);
    }


    @Override
    protected void initView(View mView) {
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无话题", 0, true);
    }

    @Override
    protected void initLocalData() {
        id = getArguments().getString("id");
    }

    @Override
    protected void loadData() {
        getListData(true);
    }

    //    InterestDetailAll_ListBean interestDetailAll_listBean;
    MyDynamic_More_ListBean myDynamicListBean = new MyDynamic_More_ListBean();

    /**
     * action 1是社群 2是兴趣组 3是问答广场
     *
     * @param isShow
     */
    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("action", "2");
        requestMap.put("id", id + "");
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.groupDetailAllTab, "兴趣组详情-全部", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
//                    interestDetailAll_listBean = GsonUtil.getInstance().json2Bean(json_data, InterestDetailAll_ListBean.class);
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamic_More_ListBean.class);
                    if (myDynamicListBean != null) {
                        handleSplitListData(myDynamicListBean, mAdapter, limit);
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
//        mAdapter = new InterestDetail_All_Adapter();
        mAdapter = new Interest_MoreItem(myDynamicListBean.getList());
//        mAdapter = new My_MoreItem_Adapter(myDynamicListBean.getList());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            InterestDetailAll_Bean item = (InterestDetailAll_Bean) adapter.getItem(position);
//            String articleId = item.getArticleId();
////            String type = item.getType();
//            String type = "2";//1是社群 2是兴趣组 3是个人
//            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);

            MyDynamic_MoreItem_Bean item = (MyDynamic_MoreItem_Bean) adapter.getItem(position);
            toDetail(item);
        });
    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                InterestDetailAll_Bean item = (InterestDetailAll_Bean) adapter.getItem(position);
                MyDynamic_MoreItem_Bean item = (MyDynamic_MoreItem_Bean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = item.getAuthorId();
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId = item.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String articleId = item.getArticleId();
                        String type = item.getTopicType();
                        ArticleDetail_Activity.startActivity(requireActivity(), articleId, "1", type);
                        break;
                    case R.id.group_Img:
                        String id = item.getAuthorId();
                        MyAndOther_Card.actionStart(mActivity, id);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, item.getAuthorId(), item.getNickname(), item.getAvatar());
                        break;
                    case R.id.share_Img:
                        showShareDialog(item);
                        break;
                    case R.id.collect_Tv:
                      collect(item,position);
                        break;
                }
            }
        });


//        ((InterestDetail_All_Adapter) mAdapter).setOnItemImgClickLister(new InterestDetail_All_Adapter.OnItemImgClickLister() {
//            @Override
//            public void OnItemImgClickLister(int position, List<String> strings) {
//                jumpToPhotoShowActivity(position, strings);
//            }
//        });
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
//                    TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
//                    TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
//                    ((InterestDetail_All_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
//                    updateAdapter(otherUserId, followStateBena.getFollowStatus());
                    getListData(false);
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


    /**
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", collectType);//0:文章 1：评论 2：仓位组合 3：资讯
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();

                        TextView collect_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.collect_Tv);
                        isCollect(collect_status + "", collect_Tv);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 收藏/取消收藏
     *
     * @param status
     * @param collect_Tv
     */
    public void isCollect(String status, TextView collect_Tv) {
        int mipmap = R.mipmap.shoucang;
        if (TextUtils.equals(status, "1")) {
            mipmap = R.mipmap.shoucang_select;
        }
        collect_Tv.setBackgroundResource(mipmap);
    }

    /**
     * 更新数据源
     *
     * @param otherUserId
     * @param status
     */
    private void updateAdapter(String otherUserId, int status) {
        if (mAdapter == null) {
            return;
        }
        List<MyDynamic_MoreItem_Bean> data = mAdapter.getData();
        int index = 0;
        for (MyDynamic_MoreItem_Bean s : data) {
            if (TextUtils.equals(otherUserId, s.getAuthorId())) {
                s.setFollowStatus(status);
                mAdapter.notifyItemChanged(index);
            }
            index++;
        }
    }

    /**
     * 详情 转发与原创用的 文章ID不一样
     *
     * @param item
     */
    private void toDetail(MyDynamic_MoreItem_Bean item) {
        String isReprint = item.getIsReprint();//0-原创 1-转发
        String articleId;
        if (TextUtils.equals(isReprint, "0")) {
            articleId = item.getArticleId();
        } else {
            articleId = item.getPreArticleId();
        }
        String type = item.getType();
        if (TextUtils.equals(isReprint, "0")) {
            type = "2";//1是社群 2是兴趣组 3是个人
            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);
        } else {
            String reprintType = item.getReprintType();//0-文章 1-资讯 3-转发评论 4-是仓位组合
            if (reprintType.equals("4")) {
                CombinationDetail_Activity.startActivity(requireActivity(), "0", articleId);
            } else if (reprintType.equals("1")) {
                ArticleDetail_Activity.startActivity(requireActivity(), articleId, true, true);
            } else if (reprintType.equals("0")) {
                ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);
            } else if (reprintType.equals("3")) {

                String source_type = item.getSource_type(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
                if (TextUtils.equals(source_type, "4")) {
                    CombinationDetail_Activity.startActivity(requireActivity(), "0", articleId);
                } else if (TextUtils.equals(source_type, "5")) {
                    ArticleDetail_Activity.startActivity(requireActivity(), articleId, true, true);
                } else {
                    ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);
                }
            }
        }
    }

    private String collectType;

    private void collect(MyDynamic_MoreItem_Bean item,int position) {
        String isReprint = item.getIsReprint();//0-原创 1-转发
        String articleId;
        collectType = "0";
        if (TextUtils.equals(isReprint, "0")) {
            articleId = item.getArticleId();
        } else {
            articleId = item.getPreArticleId();
            String reprintType = item.getReprintType();//0-文章 1-资讯 3-转发评论 4-是仓位组合
            if (reprintType.equals("4")) {
                collectType = "2";
            } else if (reprintType.equals("3")) {
                collectType = "1";
            } else if (reprintType.equals("1")) {
                collectType = "3";
            }
        }
        postCollectData(articleId, position);
    }

    /**
     * 资讯分享
     */
    private void showShareDialog(MyDynamic_MoreItem_Bean item) {
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
