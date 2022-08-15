package com.qingbo.monk.question.fragment;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipPersonAndFund_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.BaseQuestionBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.QuestionBean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.qingbo.monk.question.adapter.QuestionListAdapterAll;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;

/**
 * 问答列表（全部）
 */
public class QuestionListFragmentAll extends BaseRecyclerViewSplitFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_list;
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无话题", 0,true);
    }


    @Override
    protected void loadData() {
//        getSquareList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSquareList(true);
    }

    private void getSquareList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.getSquareListAll, "问答广场(所有)", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    BaseQuestionBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseQuestionBean.class);
                    handleSplitListData(obj, mAdapter, limit);
                }
            }
        }, isShowAnimal);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new QuestionListAdapterAll();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            QuestionBean mQuestionBean = (QuestionBean) adapter.getItem(position);

            if (mQuestionBean == null) {
                return;
            }

            String type = mQuestionBean.getType();
            ArticleDetail_Activity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "0",type);
        });

    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                QuestionBean mQuestionBean = (QuestionBean) adapter.getItem(position);

                if (mQuestionBean == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = mQuestionBean.getAuthorId();
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId = mQuestionBean.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String type = mQuestionBean.getType();
                        ArticleDetail_Activity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "1",type);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, mQuestionBean.getAuthorId(), mQuestionBean.getNickname(), mQuestionBean.getAvatar());
                        break;
                    case R.id.group_Img:
                        startPerson(mQuestionBean);
                        break;
                    case R.id.share_Img:
                        showShareDialog(mQuestionBean);
                        break;
                }
            }
        });


        ((QuestionListAdapterAll) mAdapter).setOnItemImgClickLister(new QuestionListAdapterAll.OnItemImgClickLister() {
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
    private void startPerson(QuestionBean item) {
        String data_source = item.getData_source();//1是虚拟人物,为0是注册用户
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getNickname();
            String id = item.getAuthorId();
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
                    ((QuestionListAdapterAll) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
//                    if (followStateBena.getFollowStatus() == 0) {
//                        mAdapter.getData().remove(position);
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


    @Override
    protected void onRefreshData() {
        page = 1;
        getSquareList(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getSquareList(false);
    }


    @OnClick(R.id.iv_bianji)
    public void onClick() {
        skipAnotherActivity(PublisherQuestionActivity.class);
    }

    /**
     * 资讯分享
     */
    private void showShareDialog(QuestionBean item) {
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
