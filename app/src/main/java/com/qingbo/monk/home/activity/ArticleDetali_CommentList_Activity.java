package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.CommendLikedStateBena;
import com.qingbo.monk.bean.CommentBean;
import com.qingbo.monk.bean.CommentListBean;
import com.qingbo.monk.bean.ForWardBean;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.dialog.CommentShare_Dialog;
import com.qingbo.monk.dialog.MesMore_Dialog;
import com.qingbo.monk.home.adapter.ArticleComment_Adapter;
import com.qingbo.monk.home.adapter.CommentDetail_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * ??????????????????????????????
 */
public class ArticleDetali_CommentList_Activity extends BaseRecyclerViewSplitActivity implements View.OnClickListener {

    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @BindView(R.id.sendComment_Et)
    EditText sendComment_Et;
    @BindView(R.id.release_Tv)
    TextView release_Tv;
    private ArticleCommentBean item;
    private ImageView topFollow_Img;
    private TextView topFollow_Count;
    private ImageView topMes_Img;
    private String articleId, type;
    private String commentId;
    private boolean haveEditMes = false;

    boolean isTopComment = false;//??????????????????????????????????????? true???????????????
    private boolean isStockOrFund;
    private boolean isGroup;
    private TextView topCollect_Tv;

    /**
     * @param context
     */
    public static void startActivity(Context context, ArticleCommentBean item, String articleId, String type, boolean isStockOrFund, boolean isGroup) {
        Intent intent = new Intent(context, ArticleDetali_CommentList_Activity.class);
        intent.putExtra("item", (Serializable) item);
        intent.putExtra("articleId", articleId);
        intent.putExtra("type", type);
        intent.putExtra("isStockOrFund", isStockOrFund);
        intent.putExtra("isGroup", isGroup);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detali_comment_list;
    }

    @Override
    protected void initLocalData() {
        item = (ArticleCommentBean) getIntent().getSerializableExtra("item");
        articleId = getIntent().getStringExtra("articleId");
        type = getIntent().getStringExtra("type");
        isStockOrFund = getIntent().getBooleanExtra("isStockOrFund", false);
        isGroup = getIntent().getBooleanExtra("isGroup", false);
    }

    @Override
    protected void initView() {
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//??????????????????????????????????????????????????????
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("????????????", 0, true);
    }

    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getServerData();
    }

    @Override
    protected void initEvent() {
        release_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        if (item != null) {
            getListData(true, item.getId());
        }
        getUserDetail(false);
    }

    CommentListBean commentListBean;

    private void getListData(boolean isShow, String commentId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", commentId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.CommentChildren_List, "??????-??????????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    commentListBean = GsonUtil.getInstance().json2Bean(json_data, CommentListBean.class);
                    if (commentListBean != null) {
                        handleSplitListData(commentListBean, mAdapter, limit);
                        if (page == 1) {
                            addHeadView();
                        }
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    CommentBean commentItem;

    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //????????????????????????item????????????????????????????????????????????????????????????
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CommentDetail_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                CommentBean item = (CommentBean) adapter.getItem(position);
                editAndDelMesParent(view, item, position);
                return false;
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            commentItem = (CommentBean) adapter.getItem(position);
            if (this.commentItem == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.follow_Img:
                    String likeId = this.commentItem.getId();
                    position += 1;
                    postLikedData(likeId, position);
                    break;
                case R.id.mes_Img:
                    String authorId = commentItem.getAuthorId();
                    boolean my = isMy(authorId);
                    if (!my) {
                        isTopComment = true;
                        showInput(sendComment_Et);
                        String isAnonymous = this.commentItem.getIsAnonymous();
                        String authorName = this.commentItem.getAuthorName();
                        setHint(isAnonymous, authorName, sendComment_Et);
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
                case R.id.share_Tv:
                    showShareDialog(false, position);
                    break;
                case R.id.collect_Tv:
                    position += 1;
                    String id2 = item.getId();
                    postCollectData(id2, position);
                    break;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_Img:
                String likeId = commentListBean.getCommentData().getId();
                postLikedData(likeId, -1);
                break;
            case R.id.topMes_Img:
                String authorId = commentListBean.getCommentData().getAuthorId();
                boolean my = isMy(authorId);
                if (!my) {
                    isTopComment = false;
                    showInput(sendComment_Et);
                    String isAnonymous = commentListBean.getCommentData().getIsAnonymous();
                    String authorName = commentListBean.getCommentData().getAuthorName();
                    setHint(isAnonymous, authorName, sendComment_Et);
                    haveEditMes = false;
                }
                break;
            case R.id.release_Tv:
                sendMes();
                break;
            case R.id.head_Img:
                String authorId1 = item.getAuthorId();
                String id1 = PrefUtil.getUser().getId();
                if (!TextUtils.equals(authorId1, id1)) {
                    MyAndOther_Card.actionStart(mActivity, authorId1);
                }
                break;
            case R.id.share_Tv:
                showShareDialog(true, 0);
                break;
            case R.id.collect_Tv:
                String id2 = commentListBean.getCommentData().getId();
                postCollectData(id2, -1);
                break;
        }
    }

    /**
     * ????????????
     */
    private void sendMes() {
        String s = sendComment_Et.getText().toString();
        if (TextUtils.isEmpty(s)) {
            T.s("??????????????????", 2000);
            return;
        }
        if (TextUtils.isEmpty(articleId) || TextUtils.isEmpty(type)) {
            T.s("??????ID??????", 2000);
            return;
        }
        String id, authorId, authorName1 = "";
        if (isTopComment) {
            id = commentItem.getId();
            authorId = commentItem.getAuthorId();
            authorName1 = commentItem.getAuthorName();
        } else {
            CommentBean commentData = commentListBean.getCommentData();
            id = commentData.getId();
            authorId = commentData.getAuthorId();
            authorName1 = commentData.getAuthorName();
        }
        if (haveEditMes) {
            editMesData(commentId, s);
        } else {
            addComment(articleId, type, s, id, authorId, authorName1);
        }
    }

    private void addHeadView() {
        LinearLayout headerLayout = mAdapter.getHeaderLayout();
        if (headerLayout != null) {
            headerLayout.removeAllViews();
        }
        View myView = LayoutInflater.from(this).inflate(R.layout.commentlist_top, null);
        ImageView head_Img = myView.findViewById(R.id.head_Img);
        TextView share_Tv = myView.findViewById(R.id.share_Tv);
        TextView nickName_Tv = myView.findViewById(R.id.nickName_Tv);
        TextView content_Tv = myView.findViewById(R.id.content_Tv);
        TextView time_Tv = myView.findViewById(R.id.time_Tv);
        topFollow_Count = myView.findViewById(R.id.follow_Count);
        TextView mes_Count = myView.findViewById(R.id.mes_Count);
        topFollow_Img = myView.findViewById(R.id.follow_Img);
        topMes_Img = myView.findViewById(R.id.topMes_Img);
        topCollect_Tv = myView.findViewById(R.id.collect_Tv);
        viewTouchDelegate.expandViewTouchDelegate(topFollow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(topMes_Img, 50);
        topFollow_Img.setOnClickListener(this);
        topMes_Img.setOnClickListener(this);
        head_Img.setOnClickListener(this);
        share_Tv.setOnClickListener(this);
        topCollect_Tv.setOnClickListener(this);
        mAdapter.addHeaderView(myView);
        if (commentListBean != null) {
            CommentBean commentData = commentListBean.getCommentData();
            String is_anonymous = commentData.getIsAnonymous();//1?????????
            if (TextUtils.equals(is_anonymous, "1")) {
                nickName_Tv.setText("????????????");
                head_Img.setBackgroundResource(R.mipmap.icon_logo);
                head_Img.setEnabled(false);
            } else {
                GlideUtils.loadCircleImage(mContext, head_Img, commentData.getAvatar(), R.mipmap.icon_logo);
                nickName_Tv.setText(commentData.getAuthorName());
            }
            content_Tv.setText(commentData.getComment());
            String userDate = DateUtil.getUserDate(commentData.getCreateTime());
            time_Tv.setText(userDate);
            topFollow_Count.setText(commentData.getLikedNum());
            mes_Count.setText(commentListBean.getCount() + "");
            isLike(commentData.getLikedStatus(), commentData.getLikedNum(), topFollow_Img, topFollow_Count);
        }
//        myView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                editAndDelMesParent(v, commentListBean.getCommentData());
//                return false;
//            }
//        });
    }


    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(nowLike + "");
    }


    private void postLikedData(String likeId, int pos) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("commentId", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Mes_Like, "???????????? ??????/????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CommendLikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, CommendLikedStateBena.class);
                    if (pos == -1) {
                        changeLike(likedStateBena, topFollow_Img, topFollow_Count);
                    } else {
                        ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, pos, R.id.follow_Img);
                        TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, pos, R.id.follow_Count);
                        changeLike(likedStateBena, follow_Img, follow_Count);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * ??????????????????
     *
     * @param likedStateBena
     * @param follow_Img
     * @param follow_Count
     */
    private void changeLike(CommendLikedStateBena likedStateBena, ImageView follow_Img, TextView follow_Count) {
        if (likedStateBena != null) {
            //0?????????????????????1????????????
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

    public void addComment(String articleId, String type, String comment, String commentId, String replyerId, String replyerName) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        requestMap.put("commentId", commentId);
        requestMap.put("replyerId", replyerId);
        requestMap.put("replyerName", replyerName);
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "??????-????????????", requestMap, new MyOnHttpResListener() {
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
     * ????????????????????????
     *
     * @param view
     * @param item
     */
    private void editAndDelMesParent(View view, CommentBean item, int position) {
        String del = item.getDel();
        String edit = item.getEdit();
        boolean isAll = TextUtils.equals(del, "1") && TextUtils.equals(edit, "1");//????????? ?????????
        boolean isDel = TextUtils.equals(del, "1") && TextUtils.equals(edit, "0");//?????????

        boolean haveForWard = true;
        if (isGroup) {
            haveForWard = false;
        }
        if (isAll) {
            showPopMenu(view, item, position, haveForWard, true, true, true);
        } else if (isDel) {
            showPopMenu(view, item, position, haveForWard, false, true, true);
        }
//        else {
//            if (isGroup) {
//                return;
//            }
//            showPopMenu(view, item, position, haveForWard, false, false, true);
//        }


    }

    /**
     * ??????????????????
     *
     * @param more_Img
     * @param data
     * @param haveEdit
     * @param parentOrChildren true??????????????? false ????????????
     */
    private void showPopMenu(View more_Img, CommentBean data, int position, boolean haveForWard, boolean haveEdit, boolean haveDele, boolean parentOrChildren) {
        MesMore_Dialog mesMore_dialog = new MesMore_Dialog(mActivity, haveForWard, haveEdit, haveDele, articleId);
        mesMore_dialog.setCollectType("1");
        mesMore_dialog.setMoreClickLister(new MesMore_Dialog.moreClickLister() {
            @Override
            public void onClickForWard() {
                boolean forWard = isForWard(parentOrChildren, data);
                if (forWard) {
                    return;
                }
                ForWardBean forWardBean = startForWard(parentOrChildren, position);
                Article_Forward.startActivity(mActivity, forWardBean);
            }

            @Override
            public void onClickCollect() {

            }

            @Override
            public void onClickEdit() {
                editMes(data, parentOrChildren);
            }

            @Override
            public void onClickDelete() {
                String id = "";
                if (parentOrChildren) {
                    id = data.getId();
                } else {
                    CommentBean commentData = commentListBean.getCommentData();
                    id = commentData.getId();
                }
                showDeleteDialog(id);
            }
        });
        mesMore_dialog.show();
    }

    /**
     * ??????????????????
     *
     * @param parentOrChildren
     * @param position
     */
    private ForWardBean startForWard(boolean parentOrChildren, int position) {
//        boolean forWard = isForWard(parentOrChildren, data);
//        if (forWard) {
//            return;
//        }
        if (homeFoucsDetail_bean != null) {
            HomeFoucsDetail_Bean.DataDTO.DetailDTO detail = homeFoucsDetail_bean.getData().getDetail();
            String id = "";
            String name = "";
            String comment = "";
            String imgurl = "";
            String commentAuthorId = "";
            if (parentOrChildren) {
                commentAuthorId = commentListBean.getCommentData().getAuthorId();
                id = commentListBean.getCommentData().getId();
                name = commentListBean.getCommentData().getAuthorName();
                comment = commentListBean.getCommentData().getComment();
            } else {
                commentAuthorId = commentListBean.getList().get(position).getAuthorId();
                id = commentListBean.getList().get(position).getId();
                name = commentListBean.getList().get(position).getAuthorName();
                comment = commentListBean.getList().get(position).getComment();
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


    private void showDeleteDialog(String commentId) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity, "????????????????????????", "??????", "??????",
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


    private void editMes(CommentBean data, boolean parentOrChildren) {
        showInput(sendComment_Et);//????????????
        String id = "";
        String comment = "";
        int length;
        if (parentOrChildren) {
            id = data.getId();
            comment = data.getComment();
        } else {
            CommentBean commentData = commentListBean.getCommentData();
            id = commentData.getId();
            comment = commentData.getComment();
        }
        length = comment.length();
        sendComment_Et.setText(comment);
        sendComment_Et.setSelection(length);
        haveEditMes = true;
        commentId = id;
    }


    /**
     * ????????????
     *
     * @param id
     * @param content
     */
    private void editMesData(String id, String content) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        requestMap.put("content", content);
        HttpSender httpSender = new HttpSender(HttpUrl.Comment_Edit, "?????????????????????", requestMap, new MyOnHttpResListener() {
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
     * ????????????
     *
     * @param id
     */
    private void delMesData(String id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Comment_Del, "?????????????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
//                    loadData();
                    getServerData();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * ???????????????
     *
     * @param authorId2
     * @return
     */
    public boolean isMy(String authorId2) {
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {//?????????????????????
            return true;
        }
        return false;
    }


    /**
     * ??????????????????
     *
     * @param editView
     */
    public void showInput(View editView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus???????????? //addAddressRemarkInfo is EditText
    }

    /**
     * ???????????????????????????
     *
     * @param view
     */
    private void setHint(String isAnonymous, String authorName, EditText view) {
        String s = TextUtils.equals(isAnonymous, "0") ? authorName : "????????????";
        String format = String.format("?????????%1$s", s);
        view.setHint(format);
    }

    public HomeFoucsDetail_Bean homeFoucsDetail_bean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        if (isStockOrFund) {
            requestMap.put("type", "1");
        }
        HttpSender httpSender = new HttpSender(HttpUrl.User_Article_Detail, "??????????????????", requestMap, new MyOnHttpResListener() {
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
     * ??????
     *
     * @param articleId
     */
    private void postCollectData(String articleId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        if (isStockOrFund) {
            requestMap.put("type", "3");
        } else {
            requestMap.put("type", "1");
        }
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "??????/????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();
//                        isCollect(collect_status + "", collect_Tv);
                        if (position == -1) {
                            isCollect(collect_status + "", topCollect_Tv);
                        } else {
                            TextView collect_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.collect_Tv);
                            isCollect(collect_status + "", collect_Tv);
                        }
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * ??????/????????????
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

    /**
     * ??????????????????
     *
     * @param parentOrChildren
     * @param data
     */
    private boolean isForWard(boolean parentOrChildren, CommentBean data) {
        String authorId = "";
        if (parentOrChildren) {
            authorId = data.getAuthorId();
        } else {
            CommentBean commentData = commentListBean.getCommentData();
            authorId = commentData.getAuthorId();
        }
        if (IsMe.isMy(authorId)) {
            T.s("????????????????????????", 3000);
            return true;
        }
        return false;
    }

    /**
     * ??????
     *
     * @param
     */
    private void showShareDialog(boolean parentOrChildren, int position) {
        ForWardBean forWardBean = startForWard(parentOrChildren, position);
        forWardBean.setGroup(isGroup);
        CommentShare_Dialog mShareDialog = new CommentShare_Dialog(this, articleId, isGroup, "??????");
        mShareDialog.setForWardBean(forWardBean);
        mShareDialog.setAuthor_id(forWardBean.getCommentAuthorId());
        mShareDialog.setForGroupType("2");
        mShareDialog.setCollectType("1");
        if (isStockOrFund) {
            mShareDialog.setArticleType("1");
            mShareDialog.setCollectType("3");
        }
        mShareDialog.show();
    }


}