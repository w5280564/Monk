package com.qingbo.monk.person.activity;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;
import com.qingbo.monk.bean.MyDynamic_More_ListBean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.person.adapter.My_MoreItem_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的动态
 */
public class MyDynamic_Activity extends BaseRecyclerViewSplitActivity implements View.OnClickListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.back_Btn)
    Button back_Btn;
    @BindView(R.id.iv_bianji)
    ImageView iv_bianji;
    @BindView(R.id.atrCount_Tv)
    TextView atrCount_Tv;
    @BindView(R.id.readCount_Tv)
    TextView readCount_Tv;
    @BindView(R.id.CommentCount_Tv)
    TextView CommentCount_Tv;
    @BindView(R.id.focusCount_Tv)
    TextView focusCount_Tv;


    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_dynamic;
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
    protected void initEvent() {
        back_Btn.setOnClickListener(this);
        iv_bianji.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(back_Btn, 50);

        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setRefreshing(true);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListData(true);
    }


    MyDynamic_More_ListBean myDynamicListBean = new MyDynamic_More_ListBean();

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("trends", "1");
        HttpSender httpSender = new HttpSender(HttpUrl.UserCenter_Article, "社交主页-我/他的动态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamic_More_ListBean.class);
                    if (myDynamicListBean != null) {
                        originalValue(myDynamicListBean.getCount(), "0","", atrCount_Tv);
                        originalValue(myDynamicListBean.getReadTotal(), "0","", readCount_Tv);
                        originalValue(myDynamicListBean.getCommentTotal(), "0","", CommentCount_Tv);
                        originalValue(myDynamicListBean.getLikedTotal(), "0","", focusCount_Tv);

                        handleSplitListData(myDynamicListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new My_MoreItem_Adapter(myDynamicListBean.getList());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyDynamic_MoreItem_Bean item = (MyDynamic_MoreItem_Bean) adapter.getItem(position);
            toDetail(item);
        });

//        ((MyDynamic_Adapter) mAdapter).setOnItemImgClickLister(new MyDynamic_Adapter.OnItemImgClickLister() {
//            @Override
//            public void OnItemImgClickLister(int position, List<String> strings) {
//                jumpToPhotoShowActivity(position, strings);
//            }
//        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
                        String type = item.getType();
                        ArticleDetail_Activity.startActivity(mActivity, item.getArticleId(), "1", type);
                        break;
                    case R.id.share_Img:
                        showShareDialog(item);
                        break;
                }
            }
        });
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
            ArticleDetail_Activity.startActivity(this, articleId, "0", type);
        } else {
            String reprintType = item.getReprintType();//0-文章 1-资讯 3-转发评论 4-是仓位组合
            if (reprintType.equals("4")) {
                CombinationDetail_Activity.startActivity(this, "0", articleId);
            } else if (reprintType.equals("1")) {
                ArticleDetail_Activity.startActivity(this, articleId, true, true);
            } else if (reprintType.equals("0")) {
                ArticleDetail_Activity.startActivity(this, articleId, "0", type);
            } else if (reprintType.equals("3")) {

                String source_type = item.getSource_type(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
                if (TextUtils.equals(source_type, "4")) {
                    CombinationDetail_Activity.startActivity(this, "0", articleId);
                } else if (TextUtils.equals(source_type, "5")) {
                    ArticleDetail_Activity.startActivity(this, articleId, true, true);
                } else {
                    ArticleDetail_Activity.startActivity(this, articleId, "0", type);
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
                OwnPublishBean ownPublishBean = new OwnPublishBean();
                ownPublishBean.setId(mQuestionBean.getArticleId());
                ownPublishBean.setTitle(mQuestionBean.getTitle());
                ownPublishBean.setContent(mQuestionBean.getContent());
                ownPublishBean.setImages(mQuestionBean.getImages());
                ownPublishBean.setCreateTime(mQuestionBean.getCreateTime());
                ownPublishBean.setIsAnonymous(mQuestionBean.getIsAnonymous());
                MyCrateArticle_Avtivity.actionStart(mActivity, ownPublishBean, true);

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
                    onResume();
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_Btn:
                finish();
                break;
            case R.id.iv_bianji:
//                String id = PrefUtil.getUser().getId();
//                MyDynamicCrate_Activity.actionStart(mActivity, id);
                String isOriginator = PrefUtil.getUser().getIsOriginator();
                MyCrateArticle_Avtivity.actionStart(mActivity,isOriginator);
                break;

        }
    }

    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + (CharSequence) value);
        }
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
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(this, articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id(item.getAuthorId());
        mShareDialog.show();
    }


}