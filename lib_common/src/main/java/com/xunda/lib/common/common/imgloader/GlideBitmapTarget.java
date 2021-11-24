package com.xunda.lib.common.common.imgloader;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideBitmapTarget extends SimpleTarget<Bitmap> {
	private BitmapCallback callback;

	public GlideBitmapTarget(BitmapCallback callback){
		this.callback = callback;
	}


	@Override
	public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
		if(callback != null && resource != null){
			callback.onBitmap(resource);
		}
	}
}
