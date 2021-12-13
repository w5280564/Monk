package com.qingbo.monk.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.glide.GlideUtils;
import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片查看
 */

public class PhotoFragment extends BaseFragment implements PhotoViewAttacher.OnViewTapListener {
    @BindView(R.id.photoview)
    PhotoView mPhotoView;
    private String img_url;

    //监听回调
    FragmentCallBack mFragmentCallBack;

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
        mPhotoView.setOnViewTapListener(this);
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
    public void onViewTap(View view, float x, float y) {
        //消息回调到 Activity
        if (mFragmentCallBack != null) {
            mFragmentCallBack.onViewClick();
        }
    }




    ///onAttach 当 Fragment 与 Activity 绑定时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ///获取绑定的监听
        if (context instanceof FragmentCallBack) {
            mFragmentCallBack = (FragmentCallBack) context;
        }
    }



    ///onDetach 当 Fragment 与 Activity 解除绑定时调用
    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentCallBack = null;
    }


    public  interface FragmentCallBack {
        void onViewClick();
    }
}
