package com.xunda.lib.common.common.glide;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.DisplayUtil;


/**
 * ================================================
 *
 * @Description: 图片加载工具类
 * @Author: Zhangliangliang
 * @CreateDate: 2021/8/4 9:58
 * <p>
 * ================================================
 */
public class GlideUtils {

    /**
     * 加载图片
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadImage(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.img_pic_none_square)
                .placeholder(R.mipmap.img_pic_none_square)
                .into(iv);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorImg
     */
    public static void loadImage(Context context, ImageView iv, String url, @DrawableRes int errorImg) {
        Glide.with(context)
                .load(url)
                .error(errorImg)
                .placeholder(errorImg)
                .into(iv);
    }


    /**
     * 加载圆角图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorImg
     * @param radius
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int errorImg, int radius) {
        Glide.with(context)
                .load(url)
                .error(errorImg)
                .placeholder(errorImg)
                .transform(new RoundedCorners(radius)).into(iv);
    }


    /**
     * 加载圆角图片,有默认error图
     *
     * @param context
     * @param iv
     * @param url
     * @param radius
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int radius) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.img_pic_none_square)
                .placeholder(R.mipmap.img_pic_none_square)
                .transform(new RoundedCorners(radius)).into(iv);
    }


    /**
     * 加载部分圆角图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorImg
     * @param radius
     * @param isLeftTop
     * @param isRightTop
     * @param isLeftBottom
     * @param isRightBottom
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int errorImg, int radius,
                                      boolean isLeftTop, boolean isRightTop, boolean isLeftBottom, boolean isRightBottom) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, DisplayUtil.dip2px(context, radius));
        transform.setNeedCorner(isLeftTop, isRightTop, isLeftBottom, isRightBottom);
        Glide.with(context)
                .load(url)
                .error(errorImg)
                .placeholder(errorImg)
                .transform(transform).into(iv);
    }

    /**
     * 加载部分圆角图片,有默认error图
     *
     * @param context
     * @param iv
     * @param url
     * @param radius
     * @param isLeftTop
     * @param isRightTop
     * @param isLeftBottom
     * @param isRightBottom
     */
    public static void loadRoundImage(Context context, ImageView iv, String url, int radius,
                                      boolean isLeftTop, boolean isRightTop, boolean isLeftBottom, boolean isRightBottom) {
        RoundedCornersTransform transform = new RoundedCornersTransform(context, DisplayUtil.dip2px(context, radius));
        transform.setNeedCorner(isLeftTop, isRightTop, isLeftBottom, isRightBottom);
        Glide.with(context)
                .load(url)
                .error(R.mipmap.img_pic_none_square)
                .placeholder(R.mipmap.img_pic_none_square)
                .transform(transform).into(iv);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param iv
     * @param url
     * @param errorImg
     */
    public static void loadCircleImage(Context context, ImageView iv, String url, int errorImg) {
        Glide.with(context)
                .load(url)
                .error(errorImg)
                .placeholder(errorImg)
                .transform(new CircleCrop()).into(iv);
    }

    /**
     * 加载圆形图片,有默认error图
     *
     * @param context
     * @param iv
     * @param url
     */
    public static void loadCircleImage(Context context, ImageView iv, String url) {
        Glide.with(context)
                .load(url)
                .error(R.mipmap.icon_logo_round)
                .placeholder(R.mipmap.icon_logo_round)
                .transform(new CircleCrop()).into(iv);

    }

    /**
     * @param context
     * @param iv
     * @param url
     * @param errorImg        加载错误图片
     * @param loadAnimation   加载时动画
     * @param skipMemoryCache 内存缓存策略
     * @param strategy        存储缓存策略
     */
    public static void loadImage(Context context, ImageView iv, String url, @DrawableRes int errorImg, @DrawableRes int loadAnimation, boolean skipMemoryCache, DiskCacheStrategy strategy) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(strategy)
                .skipMemoryCache(skipMemoryCache)
                .error(errorImg)
//                .thumbnail(Glide.with(context).load(loadAnimation))
                .fitCenter()
                .into(iv);
    }

    /**
     * RoundedCornersTransform 加载图片圆角
     * @param context
     * @param iv
     * @param url
     * @param radius
     */
    public static void addRadius(Context context, ImageView iv, String url,int radius){
        RoundedCornersTransform transform = new RoundedCornersTransform(context, DisplayUtil.dip2px(context, radius));
        transform.setNeedCorner(true, true, true, true);
        Glide.with(context).load(url).placeholder(R.mipmap.img_pic_none_square).transforms(transform).into(iv);
    }
}
