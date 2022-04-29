package com.qingbo.monk.home.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.ForWardBean;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.dialog.CommentShare_Dialog;
import com.qingbo.monk.dialog.MesMore_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.ArticleDetali_CommentList_Activity;
import com.qingbo.monk.home.activity.Article_Forward;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.activity.GroupTopicDetailActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 文章详情-评论
 */
public class ArticleDetail_Comment_Fragment extends BaseRecyclerViewSplitFragment implements View.OnClickListener {
    public String articleId, type;
    TabLayout tab;
    private EditText sendComment_Et;
    //    private TextView release_Tv;
    private boolean haveEditMes = false;
    private String commentId;
    boolean isStockOrFund;
    private boolean isGroup;

    public static ArticleDetail_Comment_Fragment newInstance(String articleId, String type) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
        ArticleDetail_Comment_Fragment fragment = new ArticleDetail_Comment_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleDetail_Comment_Fragment newInstance(String articleId, String type, boolean isStockOrFund) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
        args.putBoolean("isStockOrFund", isStockOrFund);
        ArticleDetail_Comment_Fragment fragment = new ArticleDetail_Comment_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param articleId
     * @param type
     * @param isStockOrFund
     * @param isGroup       是否是社群 社群不能转发评论
     * @return
     */
    public static ArticleDetail_Comment_Fragment newInstance(String articleId, String type, boolean isStockOrFund, boolean isGroup) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
        args.putBoolean("isStockOrFund", isStockOrFund);
        args.putBoolean("isGroup", isGroup);
        ArticleDetail_Comment_Fragment fragment = new ArticleDetail_Comment_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initView(View mView) {
        tab = requireActivity().findViewById(R.id.card_Tab);
        sendComment_Et = requireActivity().findViewById(R.id.sendComment_Et);
//        release_Tv = requireActivity().findViewById(R.id.release_Tv);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无评论", R.mipmap.wupinglun, true);
    }

    @Override
    protected void initLocalData() {
        articleId = getArguments().getString("articleId");
        type = getArguments().getString("type");
        isStockOrFund = getArguments().getBoolean("isStockOrFund", false);
        isGroup = getArguments().getBoolean("isGroup", false);
    }


    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
        getUserDetail(false);
    }

    ArticleCommentListBean articleCommentListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Article_CommentList, "获取文章评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    articleCommentListBean = GsonUtil.getInstance().json2Bean(json_data, ArticleCommentListBean.class);
                    if (articleCommentListBean != null) {
                        handleSplitListData(articleCommentListBean, mAdapter, limit);
                        String count = String.format("评论(%1$s)", articleCommentListBean.getComment_total());
                        tab.getTabAt(0).setText(count);
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
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new ArticleComment_Adapter(articleId, type, isStockOrFund, isGroup);
//        mAdapter.setEmptyView(addEmptyView("暂无点赞", R.mipmap.wupinglun));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
                editAndDelMesParent(view, item, position);
                return false;
            }
        });
        ((ArticleComment_Adapter) mAdapter).setOnItemClick(new ArticleComment_Adapter.onItemLister() {
            @Override
            public void onItemClick(View view, int pos, ArticleCommentBean data) {
                ArticleDetali_CommentList_Activity.startActivity(requireActivity(), data, articleId, type, isStockOrFund, isGroup);
            }
        });

        ((ArticleComment_Adapter) mAdapter).setOnClickLister(new ArticleComment_Adapter.OnClickLister() {
            @Override
            public void onLongClick(View view, int pos, ArticleCommentBean data) {
                editAndDelMesChildren(view, data, pos);
            }
        });

    }

    /**
     * 编辑删除一级评论
     *
     * @param view
     * @param item
     * @param pos
     */
    private void editAndDelMesParent(View view, ArticleCommentBean item, int pos) {
        String del = item.getDel();
        String edit = item.getEdit();
        boolean isAll = TextUtils.equals(del, "1") && TextUtils.equals(edit, "1");//可编辑 可删除
        boolean isDel = TextUtils.equals(del, "1") && TextUtils.equals(edit, "0");//可删除
        boolean haveForWard = true;
        if (isGroup) {
            haveForWard = false;
        }
        if (isAll) {
            showPopMenu(view, item, pos, haveForWard, true, true, true);
        } else if (isDel) {
            showPopMenu(view, item, pos, haveForWard, false, true, true);
        }
//        else {
//            if (isGroup) {
//                return;
//            }
//            showPopMenu(view, item, pos, haveForWard, false, false, true);
//        }

    }


    /**
     * 编辑删除子评论
     *
     * @param view
     * @param item
     * @param pos
     */
    private void editAndDelMesChildren(View view, ArticleCommentBean item, int pos) {
        String del = item.getChildrens().get(pos).getDel();
        String edit = item.getChildrens().get(pos).getEdit();
        boolean isAll = TextUtils.equals(del, "1") && TextUtils.equals(edit, "1");//可编辑 可删除
        boolean isDel = TextUtils.equals(del, "1") && TextUtils.equals(edit, "0");//可删除
        boolean haveForWard = true;
        if (isGroup) {
            haveForWard = false;
        }
        if (isAll) {
            showPopMenu(view, item, pos, haveForWard, true, true, false);
        } else if (isDel) {
            showPopMenu(view, item, pos, haveForWard, false, true, false);
        }
//        else {
//            if (isGroup) {
//                return;
//            }
//            showPopMenu(view, item, pos, haveForWard, false, false, false);
//        }


    }


    ArticleCommentBean item;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void initEvent() {
//        release_Tv.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            item = (ArticleCommentBean) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = item.getId();
                    postLikedData(likeId, position);
                    break;
                case R.id.commentMore_Tv:
                    String id = item.getId();
                    ArticleDetali_CommentList_Activity.startActivity(requireActivity(), item, articleId, type, isStockOrFund, isGroup);
                    break;
                case R.id.mes_Img:
                    String authorId = item.getAuthorId();
                    boolean my = IsMe.isMy(authorId);
                    if (!my) {
                        if (requireActivity() instanceof ArticleDetail_Activity) {
                            showActicleActivity();
                        }
                        if (requireActivity() instanceof GroupTopicDetailActivity) {
                            showGroupActivity();
                        }
                        setHint(item, sendComment_Et);
                        haveEditMes = false;
                    }
//                    ((ArticleDetail_Activity)requireActivity()).addComment();
                    break;
                case R.id.head_Img:
                    String authorId1 = item.getAuthorId();
                    String id1 = PrefUtil.getUser().getId();
                    if (!TextUtils.equals(authorId1, id1)) {
                        MyAndOther_Card.actionStart(mActivity, authorId1);
                    }
                    break;
                case R.id.share_Tv:
                    showShareDialog(item);
                    break;
            }
        });
    }

//    /**
//     * 点击弹出键盘
//     *
//     * @param editView
//     * @param editView 是否回复评论  true是对评论回复
//     */
//    public void showInput(View editView, boolean isReply) {
//        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        editView.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
//    }

    /**
     * 回复谁的评论
     *
     * @param item
     * @param view
     */
    private void setHint(ArticleCommentBean item, EditText view) {
        String s = TextUtils.equals(item.getIsAnonymous(), "0") ? item.getAuthorName() : "匿名用户";
        String format = String.format("回复：%1$s", s);
        view.setHint(format);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_Tv:
                String s = sendComment_Et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    T.s("评论不能为空", 2000);
                    return;
                }
                if (TextUtils.isEmpty(articleId) || TextUtils.isEmpty(type)) {
                    T.s("文章ID是空", 2000);
                    return;
                }
                if (haveEditMes) {
                    editMesData(commentId, s);
                } else {
                    addComment(articleId, type, s, item);
                }
                break;
        }
    }

    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Mes_Like, "评论 点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CommendLikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, CommendLikedStateBena.class);
                    changeLike(likedStateBena, position);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    public void addComment(String articleId, String type, String comment, ArticleCommentBean item) {
        if (item == null) {
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        requestMap.put("commentId", item.getId());
        requestMap.put("replyerId", item.getAuthorId());
        requestMap.put("replyerName", item.getAuthorName());
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "文章-回复评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    sendComment_Et.setText("");
                    sendComment_Et.setHint("");
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 修改点赞状态
     *
     * @param likedStateBena
     * @param position
     */
    private void changeLike(CommendLikedStateBena likedStateBena, int position) {
        ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Img);
        TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Count);
        if (likedStateBena != null) {
            //0取消点赞成功，1点赞成功
            int nowLike;
            nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
            if (likedStateBena.getLike_status() == 0) {
                nowLike -= 1;
                follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
            } else if (likedStateBena.getLike_status() == 1) {
                follow_Img.setBackgroundResource(R.mipmap.dianzan);
                nowLike += 1;
            }
            follow_Count.setText(nowLike + "");
        }
    }


    /**
     * 编辑删除弹窗
     *
     * @param more_Img
     * @param data
     * @param position
     * @param haveEdit
     * @param parentOrChildren true是一级评论 false 是子评论
     */
    private void showPopMenu(View more_Img, ArticleCommentBean data, int position, boolean haveForWard, boolean haveEdit, boolean haveDele, boolean parentOrChildren) {
        String id = "";
        if (parentOrChildren) {
            id = data.getId();
        } else {
            id = data.getChildrens().get(position).getCommentId();
        }
        String finalId = id;
        MesMore_Dialog mesMore_dialog = new MesMore_Dialog(mActivity, haveForWard, haveEdit, haveDele, id);
        mesMore_dialog.setCollectType("1");
        mesMore_dialog.setMoreClickLister(new MesMore_Dialog.moreClickLister() {
            @Override
            public void onClickForWard() {
                ForWardBean forWardBean = startForWard(parentOrChildren, data, position);
                Article_Forward.startActivity(mActivity, forWardBean);
//                Article_Forward.startActivity(mActivity, articleId, type, id, name, comment, title, content, imgurl);
            }

            @Override
            public void onClickCollect() {

            }

            @Override
            public void onClickEdit() {
                editMes(data, position, parentOrChildren);
            }

            @Override
            public void onClickDelete() {

                showDeleteDialog(finalId, position);
            }
        });
        mesMore_dialog.show();
    }

    /**
     * 打开转发页面
     *
     * @param parentOrChildren
     * @param data
     * @param position
     */
    private ForWardBean startForWard(boolean parentOrChildren, ArticleCommentBean data, int position) {
//        boolean forWard = isForWard(parentOrChildren, data, position);
//        if (forWard) {
//            return null;
//        }
        if (homeFoucsDetail_bean != null) {
            HomeFoucsDetail_Bean.DataDTO.DetailDTO detail = homeFoucsDetail_bean.getData().getDetail();
            String id = "";
            String name = "";
            String comment = "";
            String imgurl = "";
            String commentAuthorId = "";
            if (parentOrChildren) {
                commentAuthorId = data.getAuthorId();
                id = data.getId();
                name = data.getAuthorName();
                comment = data.getComment();
            } else {
                commentAuthorId = data.getChildrens().get(position).getAuthorId();
                id = data.getChildrens().get(position).getCommentId();
                name = data.getChildrens().get(position).getAuthorName();
                comment = data.getChildrens().get(position).getComment();
            }
            type = "0";
            if (isStockOrFund) {
                type = "1";
            }
            String title = detail.getTitle();
            String content = detail.getContent();
            if (!TextUtils.isEmpty(detail.getImages())) {
                List<String> strings = Arrays.asList(detail.getImages().split(","));
                if (!ListUtils.isEmpty(strings)) {
                    imgurl = strings.get(0);
                }
            }
            ForWardBean forWardBean = new ForWardBean();
            forWardBean.setArtOrComID(articleId);
            forWardBean.setCommentAuthorId(commentAuthorId);
            forWardBean.setCommentId(id);
            forWardBean.setName(name);
            forWardBean.setComment(comment);
            forWardBean.setType(type);
            forWardBean.setStockOrFund(isStockOrFund);
            forWardBean.setTitle(title);
            forWardBean.setContent(content);
            forWardBean.setImgurl(imgurl);
            return forWardBean;
//            Article_Forward.startActivity(mActivity, articleId, type, id, name, comment, title, content, imgurl);
        }
        return null;
    }


    private void showDeleteDialog(String commentId, int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity, "确定删除此评论？", "取消", "确定",
                new TwoButtonDialogBlue.ConfirmListener() {
                    @Override
                    public void onClickRight() {
                        delMesData(commentId);
                    }

                    @Override
                    public void onClickLeft() {

                    }
                });

        mDialog.show();
    }


    private void editMes(ArticleCommentBean data, int pos, boolean parentOrChildren) {
        String id = "";
        String comment;
        int length;
        if (parentOrChildren) {
            id = data.getId();
            comment = data.getComment();
        } else {
            id = data.getChildrens().get(pos).getCommentId();
            comment = data.getChildrens().get(pos).getComment();
        }
        length = comment.length();
        sendComment_Et.setText(comment);
        sendComment_Et.setSelection(length);
        haveEditMes = true;
        commentId = id;
    }

    /**
     * 文章详情 弹出键盘
     */
    private void showActicleActivity() {
        ArticleDetail_Activity articleDetail_activity = (ArticleDetail_Activity) requireActivity();
        if (articleDetail_activity != null) {
            articleDetail_activity.showInput(sendComment_Et, true);
        }
    }

    /**
     * 群详情 弹出键盘
     */
    private void showGroupActivity() {
        GroupTopicDetailActivity groupTopicDetailActivity = (GroupTopicDetailActivity) requireActivity();
        if (groupTopicDetailActivity != null) {
            groupTopicDetailActivity.showInput(sendComment_Et, true);
        }
    }

    /**
     * 修改评论
     *
     * @param id
     * @param content
     */
    private void editMesData(String id, String content) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        requestMap.put("content", content);
        HttpSender httpSender = new HttpSender(HttpUrl.Comment_Edit, "文章—修改评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    sendComment_Et.setText("");
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 删除评论
     *
     * @param id
     */
    private void delMesData(String id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Comment_Del, "文章—删除评论", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    loadData();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 自己不能转发
     *
     * @param parentOrChildren
     * @param data
     */
    private boolean isForWard(boolean parentOrChildren, ArticleCommentBean data, int position) {
        String authorId = "";
        if (parentOrChildren) {
            authorId = data.getAuthorId();
        } else {
            authorId = data.getChildrens().get(position).getAuthorId();
        }
        if (IsMe.isMy(authorId)) {
            T.s("不能转发自己评论", 3000);
            return true;
        }
        return false;
    }

    public HomeFoucsDetail_Bean homeFoucsDetail_bean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        if (isStockOrFund) {
            requestMap.put("type", "1");
        }
        HttpSender httpSender = new HttpSender(HttpUrl.User_Article_Detail, "个人文章详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFoucsDetail_bean = GsonUtil.getInstance().json2Bean(json_root, HomeFoucsDetail_Bean.class);
                    if (homeFoucsDetail_bean != null) {
                        HomeFoucsDetail_Bean.DataDTO.DetailDTO detailData = homeFoucsDetail_bean.getData().getDetail();
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 分享
     */
    private void showShareDialog(ArticleCommentBean item) {
        ForWardBean forWardBean = startForWard(true, item, 0);
//        String id = item.getId();
        forWardBean.setGroup(isGroup);
        CommentShare_Dialog mShareDialog = new CommentShare_Dialog(requireActivity(), articleId, isGroup, "分享");
        mShareDialog.setForWardBean(forWardBean);
        mShareDialog.setAuthor_id(item.getAuthorId());
        mShareDialog.setForGroupType("2");
        mShareDialog.setCollectType("1");
        if (isStockOrFund){
            mShareDialog.setArticleType("1");
            mShareDialog.setCollectType("3");
        }
        mShareDialog.show();
    }


}
