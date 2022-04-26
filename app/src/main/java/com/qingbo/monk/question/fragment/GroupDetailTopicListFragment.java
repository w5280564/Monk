package com.qingbo.monk.question.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseOwnPublishBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.question.activity.GroupTopicDetailActivity;
import com.qingbo.monk.question.activity.PublisherGroupTopicActivity;
import com.qingbo.monk.question.adapter.Group_MoreItem;
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
    private int fragmentType;//0全部 1我的发布
    private String id, role, shequnName, requestUrl;

    /**
     * @param type 0全部 1我的发布
     * @param id   社群/兴趣组 id
     * @param role 0是待审核
     * @return
     */
    public static GroupDetailTopicListFragment NewInstance(int type, String id, String role, String shequnName) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("id", id);
        args.putString("shequnName", shequnName);
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
            fragmentType = mBundle.getInt("type", 0);
            id = mBundle.getString("id");
            shequnName = mBundle.getString("shequnName");
            role = mBundle.getString("role");
            if (fragmentType == 0) {
                requestUrl = HttpUrl.groupDetailAllTab;
            } else if (fragmentType == 1) {
                requestUrl = HttpUrl.getOwnPublishList;
            }
        }
        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if (event.type == FinishEvent.PUBLISH_TOPIC) {
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
        initSwipeRefreshLayoutAndAdapter("暂无话题", R.mipmap.icon_no_date, true);
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Group_MoreItem(baseOwnPublishBean.getList(), fragmentType, role);
//        mAdapter = new GroupDetailTopicListAdapter(fragmentType, role);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
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
                    GroupTopicDetailActivity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "1", type, mQuestionBean, fragmentType, role);
                    break;
                case R.id.follow_Tv:
                    if ("1".equals(mQuestionBean.getTopicType())) {//1是话题2是问答
                        String otherUserId = mQuestionBean.getAuthorId();
                        postFollowData(otherUserId, position);
                    } else {
                        List<OwnPublishBean.DetailDTO> details = mQuestionBean.getDetail();
                        if (!ListUtils.isEmpty(details)) {
                            OwnPublishBean.DetailDTO answerObj = details.get(0);
                            String otherUserId = answerObj.getAuthorId();
                            postFollowData(otherUserId, position);
                        } else {
                            String otherUserId = mQuestionBean.getAuthorId();
                            postFollowData(otherUserId, position);
                        }
                    }
                    break;
                case R.id.send_Mes:
                    if ("1".equals(mQuestionBean.getTopicType())) {//1是话题2是问答
                        ChatActivity.actionStart(mActivity, mQuestionBean.getAuthorId(), mQuestionBean.getNickname(), mQuestionBean.getAvatar());
                    } else {
                        List<OwnPublishBean.DetailDTO> details = mQuestionBean.getDetail();
                        if (!ListUtils.isEmpty(details)) {
                            OwnPublishBean.DetailDTO answerObj = details.get(0);
                            ChatActivity.actionStart(mActivity, answerObj.getAuthorId(), answerObj.getNickname(), answerObj.getAvatar());
                        } else {
                            ChatActivity.actionStart(mActivity, mQuestionBean.getAuthorId(), mQuestionBean.getNickname(), mQuestionBean.getAvatar());
                        }
                    }
                    break;
                case R.id.iv_delete:
                    showDeleteDialog(mQuestionBean.getArticleId(), position);
                    break;
                case R.id.more_Img:
                    ImageView more_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.more_Img);
                    showPopMenu(more_Img, mQuestionBean, position);
                    break;
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            OwnPublishBean mQuestionBean = (OwnPublishBean) adapter.getItem(position);
            if (mQuestionBean == null) {
                return;
            }
//            String type = mQuestionBean.getTopicType();
//            GroupTopicDetailActivity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "0", type, mQuestionBean, fragmentType, role);

            toDetail(mQuestionBean);
        });


//        ((GroupDetailTopicListAdapter) mAdapter).setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
//            @Override
//            public void OnItemImgClickLister(int position, List<String> strings) {
//                jumpToPhotoShowActivity(position, strings);
//            }
//        });
    }


    private void showPopMenu(ImageView more_Img, OwnPublishBean mQuestionBean, int position) {
        String status = mQuestionBean.getStatus();//0待审核 1通过 2未通过
        boolean haveEdit = false;
        if (TextUtils.equals(status, "2")) {//审核未通过才能删除
            haveEdit = true;
        }
        MyPopWindow morePopWindow = new MyPopWindow(mActivity, haveEdit, new MyPopWindow.OnPopWindowClickListener() {
            @Override
            public void onClickEdit() {
                PublisherGroupTopicActivity.actionStart(mActivity, mQuestionBean, true, id, shequnName);
            }

            @Override
            public void onClickDelete() {
                showDeleteDialog(mQuestionBean.getArticleId(), position);
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
                    if (followStateBena != null) {
                        getQuestionList();
//                        ((GroupDetailTopicListAdapter) mAdapter).getData().get(position).setStatusNum(followStateBena.getFollowStatus());
//                        TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
//                        TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
//                        ((GroupDetailTopicListAdapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    private void showDeleteDialog(String id, int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity, "确定删除该条主题吗？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                deleteQuestion(id, position);
            }

            @Override
            public void onClickLeft() {
            }
        });
        mDialog.show();
    }


    /**
     * 删除话题
     *
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

    BaseOwnPublishBean baseOwnPublishBean = new BaseOwnPublishBean();

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
                             baseOwnPublishBean = GsonUtil.getInstance().json2Bean(data, BaseOwnPublishBean.class);
                            handleSplitListData(baseOwnPublishBean, mAdapter, limit);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    /**
     * 详情 转发与原创用的 文章ID不一样
     *
     * @param item
     */
    private void toDetail(OwnPublishBean item) {
        String isReprint = item.getIsReprint();//0-原创 1-转发
        String articleId;
        if (TextUtils.equals(isReprint, "0")) {
            articleId = item.getArticleId();
        } else {
            articleId = item.getPreArticleId();
        }
        String type = item.getType();
        if (TextUtils.equals(isReprint, "0")) {
            GroupTopicDetailActivity.startActivity(requireActivity(), articleId, "0", type, item, fragmentType, role);
        } else {
            String reprintType = item.getReprintType();//0-文章 1-资讯 3-转发评论 4-是仓位组合
            if (reprintType.equals("4")) {
                CombinationDetail_Activity.startActivity(requireActivity(), "0", articleId);
            } else if (reprintType.equals("1")) {
                ArticleDetail_Activity.startActivity(requireActivity(), articleId, true, true);
            } else if (reprintType.equals("0")) {
                ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);
            } else if (reprintType.equals("3")) {

                String source_type = item.getSourceType(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
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



}
