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
import com.qingbo.monk.Slides.activity.InterestCrate_Activity;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.FollowListBean;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamicListBean;
import com.qingbo.monk.bean.MyDynamic_Bean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.person.adapter.MyDynamic_Adapter;
import com.qingbo.monk.question.adapter.QuestionListAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

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


    MyDynamicListBean myDynamicListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.UserCenter_Article, "社交主页-我/他的动态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamicListBean.class);
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
        mAdapter = new MyDynamic_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);
            String articleId = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(mActivity, articleId, "0", type);
        });

        ((MyDynamic_Adapter) mAdapter).setOnItemImgClickLister(new MyDynamic_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);

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
                }
            }
        });
    }

    private void showPopMenu(ImageView more_Img, MyDynamic_Bean mQuestionBean, int position) {
        String status = mQuestionBean.getStatus();//0待审核 1通过 2未通过
        boolean haveEdit = false;
        if (TextUtils.equals(status, "2")) {//审核未通过才能删除
            haveEdit = true;
        }
        MyPopWindow morePopWindow = new MyPopWindow(mActivity, haveEdit, new MyPopWindow.OnPopWindowClickListener() {
            @Override
            public void onClickEdit() {
//                String shequnId = mQuestionBean.getShequnId();
//                InterestCrate_Activity.actionStart(mActivity,shequnId, mQuestionBean, true);
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


}