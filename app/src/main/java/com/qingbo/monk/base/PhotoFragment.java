package com.qingbo.monk.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.eventbus.ClickImageFinishEvent;
import org.greenrobot.eventbus.EventBus;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片查看
 */

public class PhotoFragment extends BaseFragment implements PhotoViewAttacher.OnPhotoTapListener {

    private String img_url;
    private PhotoView mPhotoView;

    /**
     * 获取这个fragment需要展示图片的url
     * @param url
     * @return
     */
    public static PhotoFragment newInstance(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img_url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img, container, false);
        mPhotoView = view.findViewById(R.id.photoview);
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER);
        mPhotoView.setOnPhotoTapListener(this);
        Glide.with(mActivity)
                .load(img_url)
                .into(mPhotoView);
        return view;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        EventBus.getDefault().post(new ClickImageFinishEvent(ClickImageFinishEvent.CLICK_IMAGE));//点击图片返回
    }
}
