package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.CollectStateBean;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章分享弹出框
 *
 * @author Administrator 欧阳
 */
public class InfoOrArticleShare_Dialog extends Dialog implements OnClickListener {
    private static final int THUMB_SIZE = 120;
    private  boolean isStockOrFund;
    private Context context;
    private String articleId;
    private String pageUrl, title, text, imageUrl, dialog_title;
    private List<Map<Object, String>> platformList = new ArrayList<>();
    private String appId;
    private IWXAPI api;
    public static String[] SHARE_PLATFORM_LIST = {"转发动态", "微信好友", "朋友圈"};//分享平台列表
    public static int[] SHARE_IMG_LIST = {R.mipmap.zhuanfa, R.mipmap.weixin, R.mipmap.pengyouquan};//分享平台列表
    private TextView wechat_Tv, quan_Tv,sina_Tv,qq_Tv;

    public void setDynamicAndCollect_clickLister(dynamicAndCollect_ClickLister dynamicAndCollect_clickLister) {
        this.dynamicAndCollect_clickLister = dynamicAndCollect_clickLister;
    }

    private dynamicAndCollect_ClickLister dynamicAndCollect_clickLister;


    //个人分享
    public InfoOrArticleShare_Dialog(Context context, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
        this.pageUrl = pageUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.text = text;
        this.dialog_title = dialog_title;
    }

    /**
     * 文章分享
     * @param context 上下文
     * @param articleId 转发 分享使用 文章ID
     * @param isStockOrFund 资讯传入此参数true 代表是资讯
     * @param pageUrl 分享的 app下载地址  使用应用宝地址
     * @param imageUrl 头像地址
     * @param title 分享标题
     * @param text 内容
     * @param dialog_title 弹出框标题
     */
    public InfoOrArticleShare_Dialog(Context context, String articleId,boolean isStockOrFund, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
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
        setContentView(R.layout.infoshare_dialog);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);

        appId = Constants.WECHAT_APPID; // 填应用AppId
        api = WXAPIFactory.createWXAPI(context, appId, false);

        initPlatformList();
        initEventAndView();
    }


    private void initPlatformList() {
        platformList.clear();
        List<String> strings = Arrays.asList(SHARE_PLATFORM_LIST);
        for (int i = 0; i < strings.size(); i++) {
            Map<Object, String> oneData = new HashMap<>();
            oneData.put("name", strings.get(i));
            oneData.put("imgUrl", SHARE_IMG_LIST[i] + "");
            platformList.add(oneData);
        }
//        platformList.addAll(Arrays.asList(SHARE_PLATFORM_LIST));
    }

    private void initEventAndView() {
        TextView dynamic_Tv = findViewById(R.id.dynamic_Tv);
        TextView collect_Tv = findViewById(R.id.collect_Tv);
        wechat_Tv = findViewById(R.id.wechat_Tv);
        quan_Tv = findViewById(R.id.quan_Tv);
        sina_Tv = findViewById(R.id.sina_Tv);
        qq_Tv = findViewById(R.id.qq_Tv);

        dynamic_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
        wechat_Tv.setOnClickListener(this);
        quan_Tv.setOnClickListener(this);
        sina_Tv.setOnClickListener(this);
        qq_Tv.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(dialog_title);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    private void startShare(String shareName, Bitmap mBitmap) {
        shareToWechat(shareName, mBitmap);//分享至微信好友/朋友圈/收藏
        dismiss();
    }


    private Bitmap getThumbBmp(Bitmap mSourceBitmap) {
        if (mSourceBitmap == null) {
            mSourceBitmap = getDefaultBitmap();
        }

        Bitmap thumbBmp;
        if (mSourceBitmap.getWidth() == THUMB_SIZE && mSourceBitmap.getHeight() == THUMB_SIZE) {
            thumbBmp = mSourceBitmap;
        } else {
            thumbBmp = Bitmap.createScaledBitmap(mSourceBitmap, THUMB_SIZE, THUMB_SIZE, true);
            mSourceBitmap.recycle();
        }
        return thumbBmp;
    }


    /**
     * 分享网页至微信好友/朋友圈/收藏
     *
     * @param shareName
     */
    private void shareToWechat(String shareName, Bitmap mSourceBitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = pageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = text;

        Bitmap thumbBmp = getThumbBmp(mSourceBitmap);
        msg.thumbData = bitmapBytes(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;

        if (("微信").equals(shareName)) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if ("朋友圈".equals(shareName)) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }


    /**
     * 获取默认图片
     *
     * @return
     */
    private Bitmap getDefaultBitmap() {
        return BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_share);
    }


    public static byte[] bitmapBytes(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }


    private String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dynamic_Tv:
                dismiss();
//                dynamicAndCollect_clickLister.dynamicClick();
                postForwardingData(articleId);
                break;
            case R.id.collect_Tv:
//                dynamicAndCollect_clickLister.collectClick();
                dismiss();
                postCollectData(articleId);
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.wechat_Tv:
                wechatShare(wechat_Tv);
                break;
            case R.id.quan_Tv:
                wechatShare(quan_Tv);
                break;
            case R.id.sina_Tv:
                dismiss();
                T.ss("暂无微博分享");
                break;
            case R.id.qq_Tv:
                dismiss();
                T.ss("暂无QQ分享");
                break;

        }
    }

    /**
     * 微信分享
     *
     * @param tV
     */
    private void wechatShare(TextView tV) {
        String shareName = tV.getText().toString();
        if (StringUtil.isBlank(imageUrl)) {
            startShare(shareName, null);
        } else {
            Glide.with(context).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap thumbBmp;
                    if (resource.getHeight() > 100 && resource.getWidth() > 100) {
                        thumbBmp = Bitmap.createScaledBitmap(resource, 100, 100, true);
                    } else {
                        thumbBmp = resource;
                    }
                    if (thumbBmp != null) {
                        startShare(shareName, thumbBmp);
                    } else {
                        T.ss("获取分享图片失败");
                    }
                }
            });
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
     * 转发
     *
     * @param articleId
     */
    private void postForwardingData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        String type = "0";
        if (isStockOrFund) {
            type = "1";
        }
        requestMap.put("id", articleId);
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Repeat_Article, "转发动态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                }
            }
        }, true);
        httpSender.setContext(getContext());
        httpSender.sendPost();
    }

    /**
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
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
