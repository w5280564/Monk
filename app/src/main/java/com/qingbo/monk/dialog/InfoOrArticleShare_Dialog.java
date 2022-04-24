package com.qingbo.monk.dialog;

import static com.xunda.lib.common.common.Constants.QQ_APP_ID;

import android.app.Activity;
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
import com.qingbo.monk.base.baseview.IsMe;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.home.activity.ForWardGroup_Activity;
import com.qingbo.monk.home.activity.ForWardInterest_Activity;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文章分享弹出框
 *
 * @author
 */
public class InfoOrArticleShare_Dialog extends Dialog implements OnClickListener, WbShareCallback {
    private static final int THUMB_SIZE = 120;
    private boolean isStockOrFund;
    private Context context;
    private String articleId;
    private String pageUrl, title, text, imageUrl, dialog_title;
    private List<Map<Object, String>> platformList = new ArrayList<>();
    private String appId;
    private IWXAPI api;
    public static String[] SHARE_PLATFORM_LIST = {"转发动态", "微信好友", "朋友圈"};//分享平台列表
    public static int[] SHARE_IMG_LIST = {R.mipmap.zhuanfa, R.mipmap.weixin, R.mipmap.pengyouquan};//分享平台列表
    private TextView wechat_Tv, quan_Tv, sina_Tv, qq_Tv;
    private String author_id;
    private Tencent mTencent;
    private String articleType;
    private String collectType;
    private String forGroupType = "0";

    //在微博开发平台为应用申请的App Key
    private static final String APP_KY = "887009180";
    //在微博开放平台设置的授权回调页
    private static final String REDIRECT_URL = "http://toptopv.com/";
    //在微博开放平台为应用申请的高级权限
    private static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private IWBAPI mWBAPI;


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
     * 文章ID
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
    public InfoOrArticleShare_Dialog(Context context, String articleId, boolean isStockOrFund, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
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
        regToQQ();
        regSina();

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
        TextView group_Tv = findViewById(R.id.group_Tv);
        TextView interest_Tv = findViewById(R.id.interest_Tv);
        wechat_Tv = findViewById(R.id.wechat_Tv);
        quan_Tv = findViewById(R.id.quan_Tv);
        sina_Tv = findViewById(R.id.sina_Tv);
        qq_Tv = findViewById(R.id.qq_Tv);

        dynamic_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
        group_Tv.setOnClickListener(this);
        interest_Tv.setOnClickListener(this);
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
                if (!isStockOrFund) {//不是资讯类型，判断是否自己发的文章
                    if (IsMe.isMy(author_id)) {
                        T.ss("不能转发自己的文章");
                        return;
                    }
                }
//              dynamicAndCollect_clickLister.dynamicClick();
                postForwardingData(articleId);
                break;
            case R.id.collect_Tv:
//              dynamicAndCollect_clickLister.collectClick();
                dismiss();
                postCollectData(articleId);
                break;
            case R.id.group_Tv:
                dismiss();
                if (!isStockOrFund) {//不是资讯类型，判断是否自己发的文章
                    if (IsMe.isMy(author_id)) {
                        T.ss("不能转发自己的文章");
                        return;
                    }
                }
                String id = PrefUtil.getUser().getId();
                ForWardGroup_Activity.actionStart(context, id, articleId, forGroupType);
                break;
            case R.id.interest_Tv:
                dismiss();
                if (!isStockOrFund) {//不是资讯类型，判断是否自己发的文章
                    if (IsMe.isMy(author_id)) {
                        T.ss("不能转发自己的文章");
                        return;
                    }
                }
                String id1 = PrefUtil.getUser().getId();
                ForWardInterest_Activity.actionStart(context, id1, articleId, forGroupType);
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
//                T.ss("暂无微博分享");
                doWeiboShare();
                break;
            case R.id.qq_Tv:
                dismiss();
//                T.ss("暂无QQ分享");
                shareToQQ(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
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

    private void regToQQ() {
        mTencent = Tencent.createInstance(QQ_APP_ID, context, "com.qingbo.monk.fileprovider");
    }


    private void shareToQQ(int QQType) {
        Bundle shareParams = new Bundle();
        shareParams.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        shareParams.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        shareParams.putString(QQShare.SHARE_TO_QQ_SUMMARY, text);
        shareParams.putString(QQShare.SHARE_TO_QQ_TARGET_URL, pageUrl);
//        shareParams.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, R.drawable.app_logo);
        shareParams.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl); //不传图片 默认使用applogo
        shareParams.putString(QQShare.SHARE_TO_QQ_APP_NAME, "");//修改返回按钮名字 不是修改分享app名
        shareParams.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQType);
        mTencent.shareToQQ((Activity) context, shareParams, new BaseUiListener());
    }

    @Override
    public void onComplete() {
        T.ss("分享成功");
    }

    @Override
    public void onError(com.sina.weibo.sdk.common.UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    private static class BaseUiListener implements IUiListener {
        protected void doComplete(Object values) {
        }

        @Override
        public void onComplete(Object o) {
            doComplete(o);
        }

        @Override
        public void onError(UiError e) {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWarning(int i) {

        }
    }

    private void regSina() {
        AuthInfo authInfo = new AuthInfo(context, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(context);
        mWBAPI.registerApp(context, authInfo);
        mWBAPI.setLoggerEnable(true);
    }

    private void doWeiboShare() {
        WeiboMultiMessage message = new WeiboMultiMessage();
        // 分享网页
        WebpageObject webObject = new WebpageObject();
        webObject.identify = UUID.randomUUID().toString();
        webObject.title = title;
        webObject.description = text;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            webObject.thumbData = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        webObject.actionUrl = pageUrl;
        webObject.defaultText = "分享网页";
        message.mediaObject = webObject;
        mWBAPI.shareMessage((Activity) context, message, true);
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
        if (!TextUtils.isEmpty(articleType)) {
            type = articleType;
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
