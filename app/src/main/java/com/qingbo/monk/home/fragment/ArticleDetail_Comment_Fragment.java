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
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.home.activity.ArticleDetali_CommentList_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_CommentList_Activity;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.MyEditPopWindow;
import com.xunda.lib.common.dialog.MyPopWindow;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.view.CustomLoadMoreView;

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

    public static ArticleDetail_Comment_Fragment newInstance(String articleId, String type) {
        Bundle args = new Bundle();
        args.putString("articleId", articleId);
        args.putString("type", type);
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
    }


    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
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
        mAdapter = new ArticleComment_Adapter(articleId, type);
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
        if (isAll) {
            showPopMenu(view, item, pos, true, true);
        }
        if (isDel) {
            showPopMenu(view, item, pos, false, true);
        }
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
        if (isAll) {
            showPopMenu(view, item, pos, true, false);
        }
        if (isDel) {
            showPopMenu(view, item, pos, false, false);
        }
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
                    ArticleDetali_CommentList_Activity.startActivity(requireActivity(), item, articleId, type);
                    break;
                case R.id.mes_Img:
                    String authorId = item.getAuthorId();
                    boolean my = ((ArticleDetail_Activity) requireActivity()).isMy(authorId);
                    if (!my) {
                        ((ArticleDetail_Activity) requireActivity()).showInput(sendComment_Et, true);
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
            }
        });
    }

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
    private void showPopMenu(View more_Img, ArticleCommentBean data, int position, boolean haveEdit, boolean parentOrChildren) {
        MyEditPopWindow morePopWindow = new MyEditPopWindow(mActivity, haveEdit, new MyEditPopWindow.OnPopWindowClickListener() {
            @Override
            public void onClickEdit() {
                editMes(data, position, parentOrChildren);
            }

            @Override
            public void onClickDelete() {
                String id = "";
                if (parentOrChildren) {
                   id =  data.getId();
                }else {
                    id = data.getChildrens().get(position).getCommentId();
                }
                showDeleteDialog(id, position);
            }

        });
        morePopWindow.showPopupWindow(more_Img);
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
        ((ArticleDetail_Activity) requireActivity()).showInput(sendComment_Et, true);//弹出键盘
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


}
