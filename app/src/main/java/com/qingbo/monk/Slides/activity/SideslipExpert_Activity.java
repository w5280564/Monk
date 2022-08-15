package com.qingbo.monk.Slides.activity;

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
import com.qingbo.monk.Slides.adapter.Expert_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.base.status.ArticleDataChange;
import com.qingbo.monk.base.status.OnSheQuDataChangeImpl;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.Follow_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;
import java.util.List;

/**
 * 侧滑专家
 */
public class SideslipExpert_Activity extends BaseRecyclerViewSplitActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_expert;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }


    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getExpertList(false);
    }



    @Override
    protected void onRefreshData() {
        page = 1;
        getExpertList(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getExpertList(false);
    }

    FollowListBean homeFllowBean;

    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Expert_List, "侧滑—专家", requestMap,
                new MyOnHttpResListener() {
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

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void initRecyclerView() {
        mAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Expert_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        updateData();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(this, articleId, "0", type, true);
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeFllowBean item = (HomeFllowBean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Img:
                        String likeId = item.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.group_Img:
                        startPerson(item);
                        break;
                    case R.id.share_Img:
                        showShareDialog(item);
                        break;
                    case R.id.collect_Tv:
                        String articleId1 = item.getArticleId();
                        postCollectData(articleId1,position);
                        break;
                }
            }
        });

        ((Expert_Adapter) mAdapter).setOnItemImgClickLister(new Expert_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    /**
     * 更新列表状态
     */
    private void updateData() {
        OnSheQuDataChangeImpl onSheQuDataChange = new OnSheQuDataChangeImpl(mActivity, mAdapter);
        ArticleDataChange.ins().setArticleDataChangeListener(onSheQuDataChange);
    }

    /**
     * 人物跳转
     *
     * @param item
     */
    private void startPerson(HomeFllowBean item) {
        String data_source = item.getData_source();//1是虚拟人物,为0是注册用户
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getAuthorName();
            String id = item.getAuthorId();
            SideslipPersonAndFund_Activity.startActivity(mActivity, nickname, id, "0");
        } else {
            String id = item.getAuthorId();
            MyAndOther_Card.actionStart(mActivity, id, true);
        }
    }

    /**
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", "0");
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();

                        TextView collect_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.collect_Tv);
                        ((Expert_Adapter)mAdapter).isCollect(collect_status + "",collect_Tv);
                    }
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
                    likeCount(position, likedStateBena);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void likeCount(int position, LikedStateBena likedStateBena) {
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
            if (nowLike < 0) {
                nowLike = 0;
            }
            follow_Count.setText(nowLike + "");
        }
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
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(this, articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id(item.getAuthorId());
        mShareDialog.show();
    }


}