package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.ForWardBean;
import com.qingbo.monk.home.activity.Article_Forward;
import com.qingbo.monk.home.activity.ForWardGroup_Activity;
import com.qingbo.monk.home.activity.ForWardInterest_Activity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.HashMap;

/**
 * 评论转发弹出框
 *
 * @author
 */
public class CommentShare_Dialog extends Dialog implements OnClickListener {
    private boolean isStockOrFund;
    private Context context;
    private String articleId;
    private String pageUrl, title, text, imageUrl, dialog_title;
    private String author_id;
    private String articleType;
    private String collectType;
    private String forGroupType = "0";
    private ForWardBean forWardBean;

    /**
     * 转发到我的动态
     * 文章类型 默认0是文章 1是资讯 2是评论 3：仓位组合【id：仓位组合ID】
     *
     * @param articleType
     */
    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    /**
     * 收藏 type  0:文章 1：评论 2：仓位组合 3：资讯
     *
     * @param collectType
     */
    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    /**
     * 转发到社群/兴趣圈
     *
     * @param forGroupType 转发操作类型 0：文章类【默认】 1：仓位组合
     */
    public void setForGroupType(String forGroupType) {
        this.forGroupType = forGroupType;
    }

    /**
     * 转发的内容
     *
     * @param
     */
    public void setForWardBean(ForWardBean forWardBean) {
        this.forWardBean = forWardBean;
    }

    /**
     *
     *
     * @param author_id
     */
    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setDynamicAndCollect_clickLister(dynamicAndCollect_ClickLister dynamicAndCollect_clickLister) {
        this.dynamicAndCollect_clickLister = dynamicAndCollect_clickLister;
    }

    private dynamicAndCollect_ClickLister dynamicAndCollect_clickLister;


    //个人分享
    public CommentShare_Dialog(Context context, String articleId, boolean isStockOrFund, String dialog_title) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
        this.articleId = articleId;
        this.isStockOrFund = isStockOrFund;
        this.dialog_title = dialog_title;
    }

    /**
     * 文章分享
     *
     * @param context       上下文
     * @param articleId     转发 分享使用 文章ID
     * @param isStockOrFund 资讯传入此参数true 代表是资讯
     * @param pageUrl       分享的 app下载地址  使用应用宝地址
     * @param imageUrl      头像地址
     * @param title         分享标题
     * @param text          内容
     * @param dialog_title  弹出框标题
     */
    public CommentShare_Dialog(Context context, String articleId, boolean isStockOrFund, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
        this.articleId = articleId;
        this.isStockOrFund = isStockOrFund;
        this.pageUrl = pageUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.text = text;
        this.dialog_title = dialog_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentshare_dialog);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);

        initEventAndView();
    }

    private void initEventAndView() {
        TextView dynamic_Tv = findViewById(R.id.dynamic_Tv);
        TextView collect_Tv = findViewById(R.id.collect_Tv);
        TextView group_Tv = findViewById(R.id.group_Tv);
        TextView interest_Tv = findViewById(R.id.interest_Tv);
        dynamic_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
        group_Tv.setOnClickListener(this);
        interest_Tv.setOnClickListener(this);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(dialog_title);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dynamic_Tv:
                dismiss();
                if (isStockOrFund){
                    T.ss("社群不能转发评论");
                    return;
                }
                if (forWardBean != null) {
                    if (IsMe.isMy(forWardBean.getCommentAuthorId())) {
                        T.ss("不能转发自己的评论");
                        return;
                    }
                }
                Article_Forward.startActivity(context, forWardBean);
                break;
            case R.id.collect_Tv:
//              dynamicAndCollect_clickLister.collectClick();
                dismiss();
                String commentId = forWardBean.getCommentId();
                postCollectData(commentId);
                break;
            case R.id.group_Tv:
                dismiss();
                if (isStockOrFund){
                    T.ss("社群不能转发评论");
                    return;
                }
                if (forWardBean != null) {
                    if (IsMe.isMy(forWardBean.getCommentAuthorId())) {
                        T.ss("不能转发自己的评论");
                        return;
                    }
                }
                String id = PrefUtil.getUser().getId();
                forWardBean.setGroupOrInterest("1");
                ForWardGroup_Activity.actionStart(context, id, articleId, forGroupType,forWardBean);
                break;
            case R.id.interest_Tv:
                dismiss();
                if (isStockOrFund){
                    T.ss("社群不能转发评论");
                    return;
                }
                if (forWardBean != null) {
                    if (IsMe.isMy(forWardBean.getCommentAuthorId())) {
                        T.ss("不能转发自己的评论");
                        return;
                    }
                }
                String id1 = PrefUtil.getUser().getId();
                forWardBean.setGroupOrInterest("2");
                ForWardInterest_Activity.actionStart(context, id1, articleId, forGroupType,forWardBean);
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    /**
     * 添加转发 收藏接口
     */
    public interface dynamicAndCollect_ClickLister {
        void dynamicClick();

        void collectClick();
    }

    /**
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        if (!TextUtils.isEmpty(collectType)) {
            requestMap.put("type", collectType);
        }
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();
//                        isCollect(collect_status + "", collect_Tv);
                        if (TextUtils.equals(collect_status + "", "1")) {
                            T.ss("已收藏");
                        } else {
                            T.ss("取消收藏");
                        }
                    }
                }
            }
        }, true);
        httpSender.setContext(getContext());
        httpSender.sendPost();
    }


}
