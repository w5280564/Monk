package com.qingbo.monk.dialog;

import static com.xunda.lib.common.common.Constants.QQ_APP_ID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 分享弹出框
 *
 * @author Administrator 欧阳
 */
public class MShareDialog extends Dialog implements OnClickListener, BaseQuickAdapter.OnItemClickListener, WbShareCallback {
    private static final int THUMB_SIZE = 120;
    private Context context;
    private String pageUrl, title, text, imageUrl, dialog_title;
    private List<String> platformList = new ArrayList<>();
    private RecyclerView mRecycleView;
    private String appId;
    private IWXAPI api;

    public static String[] SHARE_PLATFORM_LIST = {"微信好友", "朋友圈", "微博", "QQ"};//分享平台列表
    public static int[] SHARE_IMG_LIST = {R.mipmap.weixin, R.mipmap.pengyouquan, R.mipmap.weibo, R.mipmap.qq};//分享平台列表
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
    private Tencent mTencent;



    public MShareDialog(Context context, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
        this.pageUrl = pageUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.text = text;
        this.dialog_title = dialog_title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog);
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
        platformList.addAll(Arrays.asList(SHARE_PLATFORM_LIST));
    }

    private void initEventAndView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(dialog_title);
        mRecycleView = findViewById(R.id.recycleView);
        GridLayoutManager layoutManager = new GridLayoutManager(context, platformList.size());
        mRecycleView.setLayoutManager(layoutManager);
        ShareAdapter mAdapter = new ShareAdapter(platformList);
        mRecycleView.setAdapter(mAdapter);
        findViewById(R.id.cancel).setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
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

        if (("微信好友").equals(shareName)) {
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
        dismiss();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final String shareName = (String) adapter.getItem(position);
        if (TextUtils.equals(shareName, "微博")) {
            dismiss();
            doWeiboShare();
        } else if (TextUtils.equals(shareName, "QQ")) {
            dismiss();
            shareToQQ(QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {

            if (StringUtil.isBlank(imageUrl)) {
                startShare(shareName, null);
            } else {
                Glide.with(context).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap thumbBmp = null;
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
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), com.qingbo.monk.R.drawable.app_logo);
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
        mTencent.shareToQQ((Activity) context, shareParams, new MShareDialog.BaseUiListener());
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


    class ShareAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


        public ShareAdapter(List<String> data) {
            super(R.layout.item_share, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

            ImageView iv_logo = helper.getView(R.id.iv_logo);
            TextView tv_name = helper.getView(R.id.tv_name);
            tv_name.setText(item);
            if (item.equals("微信好友")) {
                iv_logo.setImageResource(R.mipmap.weixin);
            } else if (item.equals("朋友圈")) {
                iv_logo.setImageResource(R.mipmap.pengyouquan);
            } else if (item.equals("微博")) {
                iv_logo.setImageResource(R.mipmap.weibo);
            } else if (item.equals("QQ")) {
                iv_logo.setImageResource(R.mipmap.qq);
            }


        }
    }

}
