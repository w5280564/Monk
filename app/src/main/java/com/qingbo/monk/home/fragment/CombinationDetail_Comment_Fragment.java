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

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitFragment;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.ArticleCommentListBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.dialog.MesMore_Dialog;
import com.qingbo.monk.home.activity.Article_Forward;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_CommentList_Activity;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

/**
 * 仓位组合详情-评论列表
 */
public class CombinationDetail_Comment_Fragment extends BaseRecyclerViewSplitFragment implements View.OnClickListener {
    public String id;
    TabLayout tab;
    private EditText sendComment_Et;

    private boolean haveEditMes = false;
    private String commentId;
    private String combinationName;

    public static CombinationDetail_Comment_Fragment newInstance(String id, String combinationName) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("combinationName", combinationName);
        CombinationDetail_Comment_Fragment fragment = new CombinationDetail_Comment_Fragment();
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
        mRecyclerView = mView.findViewById(R.id.card_Recycler);
        mSwipeRefreshLayout = mView.findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无评论", R.mipmap.wupinglun, true);
    }

    @Override
    protected void initLocalData() {
        id = getArguments().getString("id");
        combinationName = getArguments().getString("combinationName");
    }


    @Override
    protected void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getListData(false);
    }

    ArticleCommentListBean articleCommentListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Show_Comment_List, "获取仓位组合评论", requestMap, new MyOnHttpResListener() {
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

//    ArticleComment_Adapter mAdapter;

    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ArticleComment_Adapter(id, "");
//        mAdapter.setEmptyView(addEmptyView("暂无评论", 0));
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            ArticleCommentBean item = (ArticleCommentBean) adapter.getItem(position);
            editAndDelMesParent(view, item, position);
            return false;
        });

        ((ArticleComment_Adapter) mAdapter).setOnItemClick((view, pos, data) -> {
//            String id = data.getId();
            CombinationDetail_CommentList_Activity.startActivity(requireActivity(), data, id,combinationName);
        });

        ((ArticleComment_Adapter) mAdapter).setOnClickLister((view, pos, data) -> editAndDelMesChildren(view, data, pos));

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
            showPopMenu(view, item, pos, true, true, true, true);
        } else if (isDel) {
            showPopMenu(view, item, pos, true, false, true, true);
        } else {
            showPopMenu(view, item, pos, true, false, false, true);
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
            showPopMenu(view, item, pos, true, true, true, false);
        } else if (isDel) {
            showPopMenu(view, item, pos, true, false, true, false);
        } else {
            showPopMenu(view, item, pos, true, false, false, false);
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
//                    String id = item.getId();
                    CombinationDetail_CommentList_Activity.startActivity(requireActivity(), item, id,combinationName);
                    break;
                case R.id.mes_Img:
                    String authorId = item.getAuthorId();
                    boolean my = ((CombinationDetail_Activity) requireActivity()).isMy(authorId);
                    if (!my) {
                        ((CombinationDetail_Activity) requireActivity()).showInput(sendComment_Et, true);
                        setHint(item, sendComment_Et);
                        haveEditMes = false;
                    }
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
                if (TextUtils.isEmpty(id)) {
                    T.s("文章ID是空", 2000);
                    return;
                }

                if (haveEditMes) {
                    editMesData(commentId, s);
                } else {
                    addComment(id, s, item);
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

    public void addComment(String id, String comment, ArticleCommentBean item) {
        if (item == null) {
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("comment", comment);
        requestMap.put("is_anonymous", "0");
        requestMap.put("commentId", item.getId());
        requestMap.put("replyerId", item.getAuthorId());
        requestMap.put("replyerName", item.getAuthorName());
        HttpSender httpSender = new HttpSender(HttpUrl.Combination_AddComment, "仓位组合-添加评论", requestMap, new MyOnHttpResListener() {
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
        MesMore_Dialog mesMore_dialog = new MesMore_Dialog(mActivity, haveForWard, haveEdit, haveDele, id);
        mesMore_dialog.setCollectType("1");

        String authorId = "";
        if (parentOrChildren) {
            authorId = data.getAuthorId();
        } else {
            authorId = data.getChildrens().get(position).getAuthorId();
        }
        mesMore_dialog.setAuthorId(authorId);

        mesMore_dialog.setMoreClickLister(new MesMore_Dialog.moreClickLister() {
            @Override
            public void onClickForWard() {
                startForWard(parentOrChildren, data, position);
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
                String id = "";
                if (parentOrChildren) {
                    id = data.getId();
                } else {
                    id = data.getChildrens().get(position).getCommentId();
                }
                showDeleteDialog(id, position);
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
    private void startForWard(boolean parentOrChildren, ArticleCommentBean data, int position) {
        boolean forWard = isForWard(parentOrChildren, data, position);
        if (forWard) {
            return;
        }
        String id = "";
        String name = "";
        String comment = "";
        String imgurl = "";
        if (parentOrChildren) {
            id = data.getId();
            name = data.getAuthorName();
            comment = data.getComment();
        } else {
            id = data.getChildrens().get(position).getCommentId();
            name = data.getChildrens().get(position).getAuthorName();
            comment = data.getChildrens().get(position).getComment();
        }
        String type = "3";
        String title = combinationName;
        String content = "";
        Article_Forward.startActivity(mActivity, data.getId(), type, id, name, comment, title, content, imgurl);
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
        ((CombinationDetail_Activity) requireActivity()).showInput(sendComment_Et, true);//弹出键盘
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


}
