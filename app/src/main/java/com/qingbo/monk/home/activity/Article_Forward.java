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
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

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
    String id = "";
    private String type;
    private String artOrComID;
    private String commentId;
    private String authorName;
    private String comment;
    private String titleName;
    private String content;
    private String imgUrl;

    /**
     *
     * @param context
     * @param artOrComID 文章或者仓位组合ID
     * @param type 默认0是文章 1是资讯 2是评论 3：仓位组合【id：仓位组合ID】
     * @param commentId
     * @param authorName
     * @param comment
     * @param title
     * @param content
     * @param imgUrl
     */
    public static void startActivity(Context context, String artOrComID, String type, String commentId, String authorName, String comment, String title, String content, String imgUrl) {
        Intent intent = new Intent(context, Article_Forward.class);
        intent.putExtra("artOrComID", artOrComID);
        intent.putExtra("type", type);
        intent.putExtra("commentId", commentId);
        intent.putExtra("authorName", authorName);
        intent.putExtra("comment", comment);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("imgUrl", imgUrl);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_forward;
    }

    @Override
    protected void initLocalData() {
        artOrComID = getIntent().getStringExtra("artOrComID");
        type = getIntent().getStringExtra("type");
        commentId = getIntent().getStringExtra("commentId");
        authorName = getIntent().getStringExtra("authorName");
        comment = getIntent().getStringExtra("comment");
        titleName = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        imgUrl = getIntent().getStringExtra("imgUrl");
    }

    @Override
    protected void initView() {
        addData();
    }

    /**
     * 文章详情 转发评论
     */
    private void addData() {
        String format = String.format("转发评论//@%1$s：%2$s", authorName, comment);
        int startLength = "转发评论//".length();
        int endLength = (String.format("转发评论//@%1$s：", authorName)).length();
        setName(format, startLength, startLength, endLength, et_content);
        if (!TextUtils.isEmpty(imgUrl)) {
            GlideUtils.loadRoundImage(mActivity, article_Img, imgUrl, 9);
        } else {
            article_Img.setImageResource(R.mipmap.img_pic_none_square);
        }
        artName_tv.setText(titleName);
        artContent_Tv.setText(content);
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
            id = artOrComID;
        }else{
            type = "2";
            id = commentId;
        }
        postForwardingData(id);
    }


    /**
     * 转发
     *
     * @param id
     * @param //type 默认0是文章 1是资讯 2是评论  3仓位组合
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