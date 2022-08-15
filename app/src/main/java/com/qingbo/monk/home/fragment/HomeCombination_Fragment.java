package com.qingbo.monk.home.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.MyConstant;
import com.qingbo.monk.base.livedatas.LiveDataBus;
import com.qingbo.monk.base.status.ArticleDataChange;
import com.qingbo.monk.base.status.OnSheQuDataChangeImpl;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.CombinationListBean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.UpdateDataBean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.home.adapter.Combination_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 首页滑动tab页--仓位组合
 */
public class HomeCombination_Fragment extends BaseRecyclerViewSplitFragment {

    public static HomeCombination_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        HomeCombination_Fragment fragment = new HomeCombination_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
        getComStatusData();
    }

    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getListData(false);
    }

    /**
     * 刷新 点赞 收藏状态
     */
    private void getComStatusData() {
        LiveDataBus.get().with(MyConstant.UPDATE_DATA, UpdateDataBean.class).observe(this, new Observer<UpdateDataBean>() {
            @Override
            public void onChanged(UpdateDataBean updateDataBean) {
               changeList(updateDataBean);
            }
        });
    }

    /**
     * 根据ID 获取修改的POS
     * @param updateDataBean
     * @return
     */
    private int setData(UpdateDataBean updateDataBean) {
        if (mAdapter == null) {
            return 0;
        }
        String id = updateDataBean.getId();
        List<HomeCombinationBean> data =  mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            HomeCombinationBean homeCombinationBean = data.get(i);
            String itemID = homeCombinationBean.getId();
            if (TextUtils.equals(id,itemID)){
                return i;
            }
        }
        return 0;
    }

    /**
     * 修改列表点赞 、点赞数量、收藏 状态
     * @param updateDataBean
     */
    private void changeList(UpdateDataBean updateDataBean){
        int pos = setData(updateDataBean);
        HomeCombinationBean item = (HomeCombinationBean) mAdapter.getItem(pos);
        item.setLike(updateDataBean.getZanState());
        item.setLikecount(updateDataBean.getZanCount());
        item.setIs_collect(updateDataBean.getIsCollect());
        mAdapter.setData(pos,item);
        mAdapter.notifyItemChanged(pos);
    }

    CombinationListBean combinationListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.getPosition_List, "首页-仓位组合", requestMap, new MyOnHttpResListener() {
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


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Combination_Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
                String id = item.getId();
                CombinationDetail_Activity.startActivity(requireActivity(), "0", id);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = item.getId();
                    postLikedData(likeId, position);
                    break;
                case R.id.mes_Img:
                    String id = item.getId();
                    CombinationDetail_Activity.startActivity(requireActivity(), "1", id);
                    break;
                case R.id.share_Img:
                    showShareDialog(item);
                    break;
                case R.id.collect_Tv:
                    String articleId = item.getId();
                    postCollectData(articleId, position);
                    break;
            }
        });
    }


    @Override
    protected void initEvent() {
        super.initEvent();

    }

    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.combination_Topic_Like, "首页-仓位组合点赞", requestMap, new MyOnHttpResListener() {
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
        }, false);
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
        requestMap.put("type", "2");
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();

                        TextView collect_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.collect_Tv);
                        ((Combination_Adapter) mAdapter).isCollect(collect_status + "", collect_Tv);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 仓位组合
     */
    private void showShareDialog(HomeCombinationBean item) {
        String imgUrl = "";
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getId();
        String title = item.getName();
        String content = "";
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(requireActivity(), articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id("");
        mShareDialog.setArticleType("3");
        mShareDialog.setCollectType("2");
        mShareDialog.setForGroupType("1");
        mShareDialog.show();
    }


}
