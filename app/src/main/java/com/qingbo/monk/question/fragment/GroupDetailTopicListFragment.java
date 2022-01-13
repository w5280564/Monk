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
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.question.activity.GroupTopicDetailActivity;
import com.qingbo.monk.question.activity.PublisherGroupTopicActivity;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.qingbo.monk.question.adapter.GroupDetailTopicListAdapter;
import com.qingbo.monk.question.adapter.QuestionListAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.List;

/**
 * 社群详情话题列表
 */
public class GroupDetailTopicListFragment extends BaseRecyclerViewSplitFragment {
    private int topicType;//0全部 1我的发布
    private String id,role,requestUrl;

    /**
     *
     * @param type 0全部 1我的发布
     * @param id 社群/兴趣圈 id
     * @param role 0是待审核
     * @return
     */
    public static GroupDetailTopicListFragment NewInstance(int type, String id,String role) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        args.putString("role", role);
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
            topicType = mBundle.getInt("type", 0);
            id = mBundle.getString("id");
            role = mBundle.getString("role");
            if (topicType==0) {
                requestUrl = HttpUrl.groupDetailAllTab;
            }else if(topicType==1){
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
        initSwipeRefreshLayoutAndAdapter("暂无话题",R.mipmap.icon_no_date, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new GroupDetailTopicListAdapter(topicType,role);
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
                        GroupTopicDetailActivity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "1",type,mQuestionBean,topicType,role);
                        break;
                    case R.id.follow_Tv:
                        if ("1".equals(mQuestionBean.getTopicType())) {//1是话题2是问答
                            String otherUserId = mQuestionBean.getAuthorId();
                            postFollowData(otherUserId, position);
                        }else{
                            List<OwnPublishBean.DetailDTO> details = mQuestionBean.getDetail();
                            if (!ListUtils.isEmpty(details)) {
                                OwnPublishBean.DetailDTO answerObj = details.get(0);
                                String otherUserId = answerObj.getAuthorId();
                                postFollowData(otherUserId, position);
                            }else{
                                String otherUserId = mQuestionBean.getAuthorId();
                                postFollowData(otherUserId, position);
                            }
                        }
                        break;
                    case R.id.send_Mes:
                        if ("1".equals(mQuestionBean.getTopicType())) {//1是话题2是问答
                            ChatActivity.actionStart(mActivity, mQuestionBean.getAuthorId(), mQuestionBean.getNickname(), mQuestionBean.getAvatar());
                        }else{
                            List<OwnPublishBean.DetailDTO> details = mQuestionBean.getDetail();
                            if (!ListUtils.isEmpty(details)) {
                                OwnPublishBean.DetailDTO answerObj = details.get(0);
                                ChatActivity.actionStart(mActivity, answerObj.getAuthorId(), answerObj.getNickname(), answerObj.getAvatar());
                            }else{
                                ChatActivity.actionStart(mActivity, mQuestionBean.getAuthorId(), mQuestionBean.getNickname(), mQuestionBean.getAvatar());
                            }
                        }
                        break;
                    case R.id.iv_delete:
                        showDeleteDialog(mQuestionBean.getArticleId(),position);
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
                GroupTopicDetailActivity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "0",type,mQuestionBean,topicType,role);
            }
        });


        ((GroupDetailTopicListAdapter) mAdapter).setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
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
                PublisherGroupTopicActivity.actionStart(mActivity,mQuestionBean,true);
            }

            @Override
            public void onClickDelete() {
                showDeleteDialog(mQuestionBean.getArticleId(),position);
            }

        });
        morePopWindow.showPopupWindow(more_Img);
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
                    if(followStateBena!=null){
                        ((GroupDetailTopicListAdapter) mAdapter).getData().get(position).setStatusNum(followStateBena.getFollowStatus());
                        TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
                        TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
                        ((GroupDetailTopicListAdapter)mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



    private void showDeleteDialog(String id,int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity,"确定删除该条主题吗？","取消","确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                deleteQuestion(id,position);
            }

            @Override
            public void onClickLeft() {
            }
        });
        mDialog.show();
    }


    /**
     * 删除话题
     * @param mQuestionId
     */
    private void deleteQuestion(String mQuestionId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", mQuestionId);
        HttpSender httpSender = new HttpSender(HttpUrl.deleteTopic, "删除话题", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    mAdapter.remove(position);
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
