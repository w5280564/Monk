package com.xunda.lib.common.dialog;

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
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
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
public class ShareArticle_Dialog extends Dialog implements OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private static final int THUMB_SIZE = 120;
    private Context context;
    private String pageUrl, title, text, imageUrl, dialog_title;
    private List<Map<Object, String>> platformList = new ArrayList<>();
    private RecyclerView mRecycleView;
    private String appId;
    private IWXAPI api;
    public static String[] SHARE_PLATFORM_LIST = {"转发动态", "微信好友", "朋友圈", "微博", "QQ"};//分享平台列表
    public static int[] SHARE_IMG_LIST = {R.mipmap.zhuanfa, R.mipmap.weixin, R.mipmap.pengyouquan, R.mipmap.weibo, R.mipmap.qq};//分享平台列表
    private dynamicClickLister dynamicClickLister;


    public ShareArticle_Dialog(Context context, String pageUrl, String imageUrl, String title, String text, String dialog_title) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
        this.pageUrl = pageUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.text = text;
        this.dialog_title = dialog_title;
    }

    public void setDynamicClickLister(ShareArticle_Dialog.dynamicClickLister dynamicClickLister) {
        this.dynamicClickLister = dynamicClickLister;
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
        Map<Object, String> item = (Map<Object, String>) adapter.getItem(position);
        String shareName = item.get("name");
        if (TextUtils.equals(shareName, "转发动态")) {
            dynamicClickLister.dynamicClick();
        } else if (TextUtils.equals(shareName, "微信好友") || TextUtils.equals(shareName, "朋友圈")) {
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


    static class ShareAdapter extends BaseQuickAdapter<Map<Object, String>, BaseViewHolder> {


        public ShareAdapter(List<Map<Object, String>> data) {
            super(R.layout.item_share, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Map<Object, String> item) {
            ImageView iv_logo = helper.getView(R.id.iv_logo);
            TextView tv_name = helper.getView(R.id.tv_name);
            tv_name.setText(item.get("name"));
            iv_logo.setImageResource(Integer.parseInt(item.get("imgUrl")));

        }
    }

    public interface dynamicClickLister {
        void dynamicClick();
    }

}
