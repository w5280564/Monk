package com.qingbo.monk.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.eventbus.ClickImageFinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片查看
 */

public class PhotoFragment extends BaseFragment implements PhotoViewAttacher.OnPhotoTapListener {
    @BindView(R.id.photoview)
    PhotoView mPhotoView;

    private String img_url;

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
    protected void initLocalData() {
        getDataFromArguments();
    }

    @Override
    protected void initView() {
        mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mPhotoView.setOnPhotoTapListener(this);
        GlideUtils.loadImage(mActivity,mPhotoView,img_url,R.mipmap.img_pic_none_square);
    }

    private void getDataFromArguments() {
        Bundle b = getArguments();
        if (b!=null) {
            img_url = getArguments().getString("url");
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_img;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        EventBus.getDefault().post(new ClickImageFinishEvent(ClickImageFinishEvent.CLICK_IMAGE));//点击图片返回
    }
}
