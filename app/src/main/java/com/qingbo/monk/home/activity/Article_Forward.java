package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.CommentListBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class Article_Forward extends BaseActivity {
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.article_Img)
    ImageView article_Img;
    @BindView(R.id.artName_tv)
    TextView artName_tv;
    @BindView(R.id.artContent_Tv)
    TextView artContent_Tv;
    private ArticleCommentBean data;
    private String articleId;
    private boolean parentOrChildren;
    private boolean isStockOrFund;
    private int position;
    String id = "";
    private String type;
    private CommentListBean commentBean;

    /**
     * 文章页面 评论转发  使用数据ArticleCommentBean
     *
     * @param context
     * @param articleId        文章ID
     * @param data             转发的评论数据
     * @param parentOrChildren 转发一级还是二级评论
     * @param position         二级评论 的定位
     * @param isStockOrFund    是否是文章
     */
    public static void startActivity(Context context, String articleId, ArticleCommentBean data, boolean parentOrChildren, int position, boolean isStockOrFund) {
        Intent intent = new Intent(context, Article_Forward.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("data", data);
        intent.putExtra("parentOrChildren", parentOrChildren);
        intent.putExtra("position", position);
        intent.putExtra("isStockOrFund", isStockOrFund);
        context.startActivity(intent);
    }

    /**
     * 评论详情 转发   使用数据CommentListBean
     *
     * @param context
     * @param articleId
     * @param commentBean
     * @param parentOrChildren
     * @param position
     * @param isStockOrFund
     */
    public static void startActivity(Context context, String articleId, CommentListBean commentBean, boolean parentOrChildren, int position, boolean isStockOrFund) {
        Intent intent = new Intent(context, Article_Forward.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("commentBean", commentBean);
        intent.putExtra("parentOrChildren", parentOrChildren);
        intent.putExtra("position", position);
        intent.putExtra("isStockOrFund", isStockOrFund);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_forward;
    }

    @Override
    protected void initLocalData() {
        articleId = getIntent().getStringExtra("articleId");
        data = (ArticleCommentBean) getIntent().getSerializableExtra("data");
        parentOrChildren = getIntent().getBooleanExtra("parentOrChildren", false);
        position = getIntent().getIntExtra("position", 0);
        isStockOrFund = getIntent().getBooleanExtra("isStockOrFund", false);
        commentBean = (CommentListBean) getIntent().getSerializableExtra("commentBean");
    }

    @Override
    protected void initView() {
        if (data != null) {
            addData();
        } else {
            addCommentBean();
        }
    }


    String name = "";
    String comment = "";

    /**
     * 文章详情 转发评论
     */
    private void addData() {
        if (parentOrChildren) {
            name = data.getAuthorName();
            comment = data.getComment();
        } else {
            name = data.getChildrens().get(position).getAuthorName();
            comment = data.getChildrens().get(position).getComment();
        }
        String format = String.format("转发评论//@%1$s：%2$s", name, comment);

        int startLength = "转发评论//".length();
        int endLength = (String.format("转发评论//@%1$s：", name)).length();
        setName(format, startLength, startLength, endLength, et_content);

        if (!TextUtils.isEmpty(data.getImages())) {
            List<String> strings = Arrays.asList(data.getImages().split(","));
            if (!ListUtils.isEmpty(strings)) {
                GlideUtils.loadRoundImage(mActivity, article_Img, strings.get(0), 9);
            }
        } else {
            article_Img.setImageResource(R.mipmap.img_pic_none_square);
        }
        artName_tv.setText(data.getTitle());
        artContent_Tv.setText(data.getContent());
    }

    /**
     * 评论详情 转发评论
     */
    private void addCommentBean() {
        if (parentOrChildren) {
            name = commentBean.getCommentData().getAuthorName();
            comment = commentBean.getCommentData().getComment();
        } else {
            name = commentBean.getList().get(position).getAuthorName();
            comment = commentBean.getList().get(position).getComment();
        }
        String format = String.format("转发评论//@%1$s：%2$s", name, comment);

        int startLength = "转发评论//".length();
        int endLength = (String.format("转发评论//@%1$s：", name)).length();
        setName(format, startLength, startLength, endLength, et_content);

        if (!TextUtils.isEmpty(commentBean.getImages())) {
            List<String> strings = Arrays.asList(commentBean.getImages().split(","));
            if (!ListUtils.isEmpty(strings)) {
                GlideUtils.loadRoundImage(mActivity, article_Img, strings.get(0), 9);
            }
        } else {
            article_Img.setImageResource(R.mipmap.img_pic_none_square);
        }
        artName_tv.setText(commentBean.getTitle());
        artContent_Tv.setText(commentBean.getContent());
    }

    /**
     * @param name        要显示的数据
     * @param nameLength  要改颜色的字体长度
     * @param startLength 改色起始位置
     * @param endLength   改色结束位置
     * @param viewName
     */
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }

    @Override
    public void onRightClick() {
        if (TextUtils.isEmpty(et_content.getText())) {
            id = articleId;
            type = "0";
            if (isStockOrFund) {
                type = "1";
            }
        } else {
            type = "2";
            if (parentOrChildren) {
                id = data.getId();
            } else {
                id = data.getChildrens().get(position).getCommentId();
            }
        }

        postForwardingData(id);
    }


    /**
     * 转发
     *
     * @param id
     * @param //type 默认0是文章 1是资讯 2是评论
     */
    private void postForwardingData(String id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", type);
        if (TextUtils.equals(type, "2")) {
            requestMap.put("content", et_content.getText().toString());
        }
        HttpSender httpSender = new HttpSender(HttpUrl.Repeat_Article, "转发动态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    finish();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}