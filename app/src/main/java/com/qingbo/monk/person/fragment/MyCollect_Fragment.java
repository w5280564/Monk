package com.qingbo.monk.person.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamicListBean;
import com.qingbo.monk.bean.MyDynamic_Bean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.home.adapter.Focus_Adapter;
import com.qingbo.monk.person.activity.MyCrateArticle_Avtivity;
import com.qingbo.monk.person.adapter.MyCollect_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;

/**
 * 社交主页-我的收藏
 */
public class MyCollect_Fragment extends BaseRecyclerViewSplitFragment {

    private String userID;
    private boolean isExpert;

    public static MyCollect_Fragment newInstance(String userID) {
        Bundle args = new Bundle();
        args.putString("userID", userID);
        MyCollect_Fragment fragment = new MyCollect_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MyCollect_Fragment newInstance(String userID, boolean isExpert) {
        Bundle args = new Bundle();
        args.putString("userID", userID);
        args.putBoolean("isExpert", isExpert);
        MyCollect_Fragment fragment = new MyCollect_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.mydynamic_fragment;
    }

    @Override
    protected void initView(View mView) {
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂未收藏", 0, false);
    }

    @Override
    protected void initLocalData() {
        userID = getArguments().getString("userID");
        isExpert = getArguments().getBoolean("isExpert", false);
    }

    @Override
    protected void loadData() {
    }


    @Override
    public void onResume() {
        super.onResume();
        getListData( true);
    }

    MyDynamicListBean myDynamicListBean;

    private void getListData( boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article_List, "收藏文章列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamicListBean.class);
                    if (myDynamicListBean != null) {
                        handleSplitListData(myDynamicListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData( false);
    }


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyCollect_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            skipAnotherActivity(ArticleDetail_Activity.class);
            MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);
//            String articleId = item.getArticleId();
//            String type = item.getType();
//            ArticleDetail_Activity.startActivity(requireActivity(), articleId, "0", type);

            toDetail(item);
        });

    }

    /**
     *
     * Collect_type 0：文章【默认】1：评论 2：仓位组合 3：资讯
     */
    private void toDetail(MyDynamic_Bean item) {
        String type = item.getType();
        String collect_type = item.getCollect_type();
        String biz_id = item.getBiz_id();
        if (collect_type.equals("2")) {
            CombinationDetail_Activity.startActivity(requireActivity(), "0", biz_id);
        } else if (collect_type.equals("3")){
            ArticleDetail_Activity.startActivity(mActivity, biz_id, true, true);
        }else if (collect_type.equals("0")){
            ArticleDetail_Activity.startActivity(requireActivity(), biz_id, "0", type);
        }else if (collect_type.equals("1")){

            String source_type = item.getSource_type(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
            if (TextUtils.equals(source_type,"4")){
                CombinationDetail_Activity.startActivity(requireActivity(), "0", biz_id);
            }  else if (TextUtils.equals(source_type,"5")) {
                ArticleDetail_Activity.startActivity(mActivity, biz_id, true, true);
            }else {
                ArticleDetail_Activity.startActivity(requireActivity(), biz_id, "0", type);
            }

        }
    }




    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);
            if (item == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.follow_Tv:
                    String otherUserId = item.getAuthorId();
                    postFollowData(otherUserId, position);
                    break;
                case R.id.follow_Img:
                    String likeId = item.getArticleId();
                    postLikedData(likeId, position);
                    break;
                case R.id.mes_Img:
                    String articleId = item.getArticleId();
                    String type = item.getType();
                    ArticleDetail_Activity.startActivity(requireActivity(), articleId, "1", type);
                    break;
                case R.id.more_Img:
                    String id = PrefUtil.getUser().getId();
                    String authorId = item.getAuthorId();
                    if (TextUtils.equals(authorId, id)) {
                        ImageView more_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.more_Img);
//                        showPopMenu(more_Img, item, position);
                    }
                    break;
                case R.id.share_Img:
                    showShareDialog(item);
                    break;
            }
        });


        ((MyCollect_Adapter) mAdapter).setOnItemImgClickLister(new MyCollect_Adapter.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    /**
     * 资讯分享
     */
    private void showShareDialog(MyDynamic_Bean item) {
        String imgUrl = item.getAvatar();
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getArticleId();
        String title = item.getTitle();
        String content = item.getContent();
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(requireActivity(), articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id(item.getAuthorId());
        mShareDialog.show();

        String collect_type = item.getCollect_type();
        if (collect_type.equals("2")) {
            mShareDialog.setArticleType("3");
            mShareDialog.setCollectType("2");
            mShareDialog.setForGroupType("1");
        } else if (collect_type.equals("3")){
            mShareDialog.setArticleType("1");
            mShareDialog.setCollectType("3");
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
                    ((Focus_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                    if (followStateBena.getFollowStatus() == 0) {
                        mAdapter.remove(position);
                        mAdapter.notifyItemChanged(position);
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

    @OnClick({R.id.iv_bianji})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.iv_bianji:
                String isOriginator = PrefUtil.getUser().getIsOriginator();
                MyCrateArticle_Avtivity.actionStart(requireActivity(),isOriginator);
                break;
        }
    }


}
