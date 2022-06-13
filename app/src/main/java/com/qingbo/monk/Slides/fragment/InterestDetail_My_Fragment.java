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
import com.qingbo.monk.Slides.activity.InterestCrate_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;
import com.qingbo.monk.bean.MyDynamic_More_ListBean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.person.adapter.My_MoreItem_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 兴趣组-我的发布
 */
public class InterestDetail_My_Fragment extends BaseRecyclerViewSplitFragment {
    private String id;

    public static InterestDetail_My_Fragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        InterestDetail_My_Fragment fragment = new InterestDetail_My_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_list;
    }


    @Override
    protected void initLocalData() {
        id = getArguments().getString("id");
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("您还未发布任何话题", 0, true);
        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if (event.type == FinishEvent.PUBLISH_QUESTION) {
            page = 1;
            getSquareList(false);
        }
    }

    @Override
    protected void loadData() {
        getSquareList(true);
    }


    MyDynamic_More_ListBean myDynamicListBean = new MyDynamic_More_ListBean();

    /**
     * 默认是1 1是社群,2是兴趣组 3是问答广场
     *
     * @param isShowAnimal
     */
    private void getSquareList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("action", "2");
        requestMap.put("id", id);

        HttpSender httpSender = new HttpSender(HttpUrl.getOwnPublishList, "兴趣组-我的发布", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamic_More_ListBean.class);
//                    BaseOwnPublishBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseOwnPublishBean.class);
                    handleSplitListData(myDynamicListBean, mAdapter, limit);
                }
            }
        }, isShowAnimal);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
//        mAdapter = new QuestionListAdapterMy();
        mAdapter = new My_MoreItem_Adapter(myDynamicListBean.getList(),"true");
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            OwnPublishBean mQuestionBean = (OwnPublishBean) adapter.getItem(position);
//            if (mQuestionBean == null) {
//                return;
//            }
//            String type = mQuestionBean.getTopicType();
//            ArticleDetail_Activity.startActivity(requireActivity(), mQuestionBean.getArticleId(), "0", type);
            MyDynamic_MoreItem_Bean item = (MyDynamic_MoreItem_Bean) adapter.getItem(position);
            toDetail(item);
        });

    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                OwnPublishBean mQuestionBean = (OwnPublishBean) adapter.getItem(position);
                MyDynamic_MoreItem_Bean item = (MyDynamic_MoreItem_Bean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    case R.id.follow_Img:
                        String likeId = item.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.more_Img:
                        ImageView more_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.more_Img);
                        showPopMenu(more_Img, item, position);
                        break;
                    case R.id.mes_Img:
                        String type = item.getTopicType();
                        ArticleDetail_Activity.startActivity(requireActivity(), item.getArticleId(), "1", type);
                        break;
                    case R.id.group_Img:
                        String id = item.getAuthorId();
                        MyAndOther_Card.actionStart(mActivity, id);
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


//        ((QuestionListAdapterMy) mAdapter).setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
//            @Override
//            public void OnItemImgClickLister(int position, List<String> strings) {
//                jumpToPhotoShowActivity(position, strings);
//            }
//        });
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


    private void showPopMenu(ImageView more_Img, MyDynamic_MoreItem_Bean mQuestionBean, int position) {
        String status = mQuestionBean.getStatus();//0待审核 1通过 2未通过
        boolean haveEdit = false;
        if (TextUtils.equals(status, "2")) {//审核未通过才能删除
            haveEdit = true;
        }
        MyPopWindow morePopWindow = new MyPopWindow(mActivity, haveEdit, new MyPopWindow.OnPopWindowClickListener() {
            @Override
            public void onClickEdit() {
                String shequnId = mQuestionBean.getShequnId();
                InterestCrate_Activity.actionStart(mActivity, shequnId, mQuestionBean, true);
            }

            @Override
            public void onClickDelete() {
                showDeleteDialog(mQuestionBean.getArticleId(), position);
            }

        });
        morePopWindow.showPopupWindow(more_Img);
    }

    private void showDeleteDialog(String mQuestionId, int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity, "确定删除此问答？", "取消", "确定",
                new TwoButtonDialogBlue.ConfirmListener() {

                    @Override
                    public void onClickRight() {
                        deleteQuestion(mQuestionId, position);
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
        requestMap.put("id", mQuestionId + "");
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
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", collectType);
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();

                        TextView collect_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.collect_Tv);
                        isCollect(collect_status + "",collect_Tv);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 收藏/取消收藏
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
        InterestCrate_Activity.actionStart(mActivity, id);
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
