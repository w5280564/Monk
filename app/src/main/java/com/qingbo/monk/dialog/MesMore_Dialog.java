package com.qingbo.monk.dialog;

import android.app.Activity;
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
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论 转发 编辑 删除
 *
 * @author Administrator 欧阳
 */
public class MesMore_Dialog extends Dialog implements OnClickListener {
    private String articleId;
    private boolean haveForWard, haveEdit, haveDele;
    private Context context;
    private List<Map<Object, String>> platformList = new ArrayList<>();
    private moreClickLister moreClickLister;
    private String authorId = "";
    private String collectType;

    /**
     * 发布人ID
     *
     * @param authorId
     */
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * 收藏类型
     *
     * @param collectType 0:文章【默认】 1：评论 2：仓位组合
     */
    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public void setMoreClickLister(MesMore_Dialog.moreClickLister moreClickLister) {
        this.moreClickLister = moreClickLister;
    }


    public MesMore_Dialog(Context context) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
    }

    public MesMore_Dialog(Activity context, boolean haveEdit, boolean haveDele) {
        super(context, R.style.bottomrDialogStyle);
        this.haveForWard = haveForWard;
        this.haveEdit = haveEdit;
        this.haveDele = haveDele;
        this.context = context;
    }

    /**
     * @param context
     * @param haveForWard 是否能分享
     * @param haveEdit    是否能编辑
     * @param haveDele    是否能删除
     */
    public MesMore_Dialog(Activity context, boolean haveForWard, boolean haveEdit, boolean haveDele, String articleId) {
        super(context, R.style.bottomrDialogStyle);
        this.haveForWard = haveForWard;
        this.haveEdit = haveEdit;
        this.haveDele = haveDele;
        this.context = context;
        this.articleId = articleId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mes_more);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);

        TextView forward_Tv = findViewById(R.id.forward_Tv);
        TextView collect_Tv = findViewById(R.id.collect_Tv);
        View view11 = findViewById(R.id.view11);
        View view12 = findViewById(R.id.view12);
        TextView edit_Tv = findViewById(R.id.edit_Tv);
        TextView dele_Tv = findViewById(R.id.dele_Tv);
        TextView cancel_Tv = findViewById(R.id.cancel_Tv);

//        forward_Tv.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
//        collect_Tv.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
//        view11.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
//        view12.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
        forward_Tv.setVisibility(View.GONE);
        collect_Tv.setVisibility(View.GONE);
        view11.setVisibility(View.GONE);
        view12.setVisibility(View.GONE);

        edit_Tv.setVisibility(haveEdit ? View.VISIBLE : View.GONE);
        dele_Tv.setVisibility(haveDele ? View.VISIBLE : View.GONE);

        forward_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
        edit_Tv.setOnClickListener(this);
        dele_Tv.setOnClickListener(this);
        cancel_Tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_Tv:
                moreClickLister.onClickForWard();
                break;
            case R.id.collect_Tv:
//                moreClickLister.onClickCollect();
                dismiss();
                if (isForWard(authorId)) {
                    return;
                }
                postCollectData(articleId);
                break;
            case R.id.edit_Tv:
                moreClickLister.onClickEdit();
                break;
            case R.id.dele_Tv:
                moreClickLister.onClickDelete();
                break;
            case R.id.cancel_Tv:
                dismiss();
                break;
        }
        dismiss();
    }


    /**
     * 添加
     */
    public interface moreClickLister {
        void onClickForWard();

        void onClickCollect();

        void onClickEdit();

        void onClickDelete();
    }

    /**
     * 自己不能收藏
     *
     * @param authorId 发布者ID
     */
    private boolean isForWard(String authorId) {
        if (TextUtils.isEmpty(authorId)) {
            return true;
        }
        if (IsMe.isMy(authorId)) {
            T.s("不能收藏自己的评论", 3000);
            return true;
        }
        return false;
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
