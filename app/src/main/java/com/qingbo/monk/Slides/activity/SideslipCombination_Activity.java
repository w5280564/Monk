package com.qingbo.monk.Slides.activity;

import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.CombinationListBean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.home.adapter.Combination_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧滑 -仓位组合
 */
public class SideslipCombination_Activity extends BaseRecyclerViewSplitActivity {


    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_combination;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据",0, true);
        title_bar.setTitle("仓位组合");
        title_bar.showTitle();
        title_bar.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.black));
    }


    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_main_color)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }


    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
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

    CombinationListBean combinationListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.getPosition_List, "侧滑-仓位组合", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    combinationListBean = GsonUtil.getInstance().json2Bean(json_data, CombinationListBean.class);
                    if (combinationListBean != null) {
                        handleSplitListData(combinationListBean, mAdapter, limit);
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
        mAdapter = new Combination_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
            String id = item.getId();
            CombinationDetail_Activity.startActivity(this,"0",id);
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
            switch (view.getId()){
                case R.id.follow_Img:
                    String likeId = item.getId();
                    postLikedData(likeId, position);
                    break;
                case R.id.share_Img:
                    showShareDialog(item);
                    break;
            }
        });
    }

    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId+ "");
        HttpSender httpSender = new HttpSender(HttpUrl.combination_Topic_Like, "侧滑-仓位组合点赞", requestMap, new MyOnHttpResListener() {
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
    public void onRightClick() {
        skipAnotherActivity(HomeSeek_Activity.class);
    }

    /**
     * 仓位组合分享
     */
    private void showShareDialog(HomeCombinationBean item) {
        String imgUrl = "";
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getId();
        String title = item.getName();
        String content = "";
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(mActivity, articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id("");
        mShareDialog.setArticleType("3");
        mShareDialog.setCollectType("2");
        mShareDialog.setForGroupType("1");
        mShareDialog.show();
    }

}