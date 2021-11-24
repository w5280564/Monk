package com.xunda.lib.common.common.imgloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 图片加载中间类封装
 * @author ouyang
 *
 */
public class ImgLoader{
	public final static String CROP_CENTER = "center_corp";
	public final static String FIT_CENTER = "fit_center";

	private static ImgLoader loader = new ImgLoader();


	/**
	 * 私有构造函数
	 */
	private ImgLoader(){}

	public static ImgLoader getInstance(){
		return loader;
	}

	/**
	 * 加载图片到指定view
	 * defImgs为占位图资源，第一个为placeHolder，第二个为errorHolder；只有一个时place和error共用
	 */
	private <T> void display(Object context, ImageView view, T t, String cropType, int...defImgs){
		RequestBuilder<Drawable> builder = createBuilder(context, t, cropType);
		if(builder!=null){
			if(defImgs != null && defImgs.length > 0){
				int place = 0;
				int error = 0;
				if(defImgs.length > 1){
					place = defImgs[0];
					error = defImgs[1];
				}else{
					place = defImgs[0];
					error = defImgs[0];
				}
				builder.placeholder(place);
				builder.error(error);
			}
			builder.into(view);
		}

	}

	/**
	 * 加载图片到指定view
	 * 默认使用crop_center
	 */
	public <T> void displayCrop(Object context, ImageView view, T t, int...defImgs){
		display(context, view, t, CROP_CENTER, defImgs);
	}

	/**
	 * 加载图片到指定view
	 * 默认使用FIT_CENTER
	 */
	public <T> void displayFit(Object context, ImageView view, T t, int...defImgs){
		display(context, view, t, FIT_CENTER, defImgs);
	}


	/**
	 * 加载图片，并直接返回bitmap
	 */
	public <T> void load(Object context, T t, BitmapCallback callback){
		GlideBitmapTarget target = new GlideBitmapTarget(callback);
		initWithContext(context).asBitmap().load(t).into(target);
	}

	/**
	 * 根据上下文初始化初始化
	 * @return
	 */
	private RequestManager initWithContext(Object context){
		RequestManager mgr = null;
		if(context instanceof Fragment){
			if (context != null && ((Fragment) context).getActivity() != null) {
				mgr = Glide.with((Fragment)context);
			}
		}else if(context instanceof android.app.Fragment){
			if (context != null && ((android.app.Fragment) context).getActivity() != null) {
				mgr = Glide.with((android.app.Fragment)context);
			}
		}else if(context instanceof FragmentActivity){
			if (!((FragmentActivity) context).isDestroyed()) {
				mgr = Glide.with((FragmentActivity)context);
			}

		}else if(context instanceof Activity){
			if (!((Activity) context).isDestroyed()) {
				mgr = Glide.with((Activity)context);
			}

		}else{
			if (context != null) {
				mgr = Glide.with((Context)context);
			}

		}
		return mgr;
	}

	/**
	 * 创建builder
	 */
	private <T> RequestBuilder<Drawable> createBuilder(Object context, T t, String cropType){
		RequestManager mgr = initWithContext(context);
		if(mgr == null){
			return null;
		}
		RequestBuilder<Drawable> builder = mgr.load(t);
		// 设置缓存所有尺寸的图片
		builder.diskCacheStrategy(DiskCacheStrategy.ALL);
		if(CROP_CENTER.equals(cropType) ){
			builder.centerCrop();
		}else if(FIT_CENTER.equals(cropType)){
			builder.fitCenter();
		}
		builder.dontAnimate();
		return builder;
	}

}
