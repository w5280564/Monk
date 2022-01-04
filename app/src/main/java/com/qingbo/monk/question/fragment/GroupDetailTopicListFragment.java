package com.qingbo.monk.question.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseOwnPublishBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.qingbo.monk.question.adapter.GroupDetailTopicListAdapter;
import com.qingbo.monk.question.adapter.QuestionListAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.MyPopWindow;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.List;

/**
 * 社群详情话题列表
 */
public class GroupDetailTopicListFragment extends BaseRecyclerViewSplitFragment {
    private int type;//0全部 1我的发布
    private String id,requestUrl;

    public static GroupDetailTopicListFragment NewInstance(int type, String id) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        GroupDetailTopicListFragment fragment = new GroupDetailTopicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.refresh_recyclerview_layout;
    }


    @Override
    protected void initLocalData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            type = mBundle.getInt("type", 0);
            id = mBundle.getString("id");
            if (type==0) {
                requestUrl = HttpUrl.groupDetailAllTab;
            }else if(type==1){
                requestUrl = HttpUrl.getOwnPublishList;
            }
        }

        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if(event.type == FinishEvent.PUBLISH_TOPIC){
            page = 1;
            getQuestionList();
        }
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无话题",R.mipmap.zhuti, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailTopicListAdapter(type);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OwnPublishBean mQuestionBean = (OwnPublishBean) adapter.getItem(position);

                if (mQuestionBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Img:
                        String likeId = mQuestionBean.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String type = mQuestionBean.getTopicType();
                        ArticleDetail_Activity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "1",type);
                        break;
                    case R.id.more_Img:
                        ImageView more_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.more_Img);
                        showPopMenu(more_Img,mQuestionBean,position);
                        break;
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OwnPublishBean mQuestionBean = (OwnPublishBean) adapter.getItem(position);

                if (mQuestionBean == null) {
                    return;
                }

                String type = mQuestionBean.getTopicType();
                ArticleDetail_Activity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "0",type);
            }
        });


        ((GroupDetailTopicListAdapter) mAdapter).setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
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


    private void showPopMenu(ImageView more_Img,OwnPublishBean mQuestionBean,int position){
        String status = mQuestionBean.getStatus();//0待审核 1通过 2未通过
        boolean haveEdit = false;
        if(TextUtils.equals(status, "2")){//审核未通过才能删除
            haveEdit = true;
        }
        MyPopWindow morePopWindow = new MyPopWindow(mActivity, haveEdit, new MyPopWindow.OnPopWindowClickListener() {
            @Override
            public void onClickEdit() {
                PublisherQuestionActivity.actionStart(mActivity,mQuestionBean,true);
            }

            @Override
            public void onClickDelete() {
//                showDeleteDialog(mQuestionBean.getId(),position);
            }

        });
        morePopWindow.showPopupWindow(more_Img);
    }


    @Override
    protected void onRefreshData() {
        page = 1;
        getQuestionList();
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getQuestionList();
    }

    @Override
    protected void loadData() {
        getQuestionList();
    }

    private void getQuestionList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("action", "1");
        map.put("page", page + "");
        map.put("limit", limit + "");
        HttpSender sender = new HttpSender(requestUrl, "社群话题列表", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseOwnPublishBean obj = GsonUtil.getInstance().json2Bean(data, BaseOwnPublishBean.class);
                            handleSplitListData(obj, mAdapter, limit);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }



}
