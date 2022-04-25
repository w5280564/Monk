package com.qingbo.monk.home.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.ForWardBean;
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
    private ForWardBean forWardBean;
    private String op_type;


    public static void startActivity(Context context, ForWardBean forWardBean) {
        Intent intent = new Intent(context, Article_Forward.class);
        intent.putExtra("forWardBean", forWardBean);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ForWardBean forWardBean, String op_type) {
        Intent intent = new Intent(context, Article_Forward.class);
        intent.putExtra("forWardBean", forWardBean);
        intent.putExtra("op_type", op_type);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_forward;
    }

    @Override
    protected void initLocalData() {
        forWardBean = (ForWardBean) getIntent().getSerializableExtra("forWardBean");
        op_type = getIntent().getStringExtra("op_type");
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);//自动弹出键盘
        addData();
    }

    /**
     * 文章详情 转发评论
     */
    private void addData() {
        if (forWardBean != null) {
            String format = String.format("转发评论//@%1$s：%2$s", forWardBean.getName(), forWardBean.getComment());
            int startLength = "转发评论//".length();
            int endLength = (String.format("转发评论//@%1$s：", forWardBean.getName())).length();
            setName(format, startLength, startLength, endLength, et_content);
            showInput(et_content);
            et_content.setSelection(0);

            if (!TextUtils.isEmpty(forWardBean.getImgurl())) {
                GlideUtils.loadRoundImage(mActivity, article_Img, forWardBean.getImgurl(), 9);
            } else {
                article_Img.setImageResource(R.mipmap.img_pic_none_square);
            }
            artName_tv.setText(forWardBean.getTitle());
            artContent_Tv.setText(forWardBean.getContent());
        }
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
        if (forWardBean != null) {
            if (TextUtils.isEmpty(et_content.getText())) {
                id = forWardBean.getArtOrComID();
            } else {
                forWardBean.setType("2");
                id = forWardBean.getCommentId();
            }
        }
        if (TextUtils.equals(op_type, "2")) {
            postForwardingData(id,forWardBean.getGroupId());
        } else {
            postForwardingData(id);
        }
    }

    /**
     * 转发 到动态
     *
     * @param id
     * @param //type 默认0是文章 1是资讯 2是评论  3仓位组合
     */
    private void postForwardingData(String id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        if (forWardBean != null) {
            requestMap.put("type", forWardBean.getType());
            if (TextUtils.equals(forWardBean.getType(), "2")) {
                requestMap.put("content", et_content.getText().toString());
            }
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


    /**
     * 转发到 社群/兴趣组
     *
     * @param articleId
     * @param //type    1社群 2兴趣组
     */
    private void postForwardingData(String articleId, String shequn_id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("biz_id", articleId);
        requestMap.put("shequn_id", shequn_id);
        requestMap.put("type", forWardBean.getGroupOrInterest());
        requestMap.put("op_type", op_type);
        requestMap.put("content", et_content.getText().toString());
        HttpSender httpSender = new HttpSender(HttpUrl.ForWard_Group, "转发动态_社群/兴趣组", requestMap, new MyOnHttpResListener() {
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

    /**
     * 点击弹出键盘
     *
     * @param editView
     */
    public void showInput(View editView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
    }


}