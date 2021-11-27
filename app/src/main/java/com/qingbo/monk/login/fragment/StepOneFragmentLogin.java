package com.qingbo.monk.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryFragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.CompressUtils;
import com.xunda.lib.common.common.utils.FileUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第1步
 */
public class StepOneFragmentLogin extends BaseCameraAndGalleryFragment {
    @BindView(R.id.iv_header)
    ImageView iv_header;
    private boolean haveUploadImg;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment_login;
    }




    @Override
    protected void getServerData() {
        getIndustryListList();
    }


    /**
     * 获取行业列表
     */
    private void getIndustryListList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.industryList, "获取行业列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {

                        } else {
                            T.ss(msg);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    @OnClick({R.id.iv_header,R.id.arrowItemView_industry,R.id.arrowItemView_year,R.id.arrowItemView_city,R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_header:
                checkGalleryPermission(1);
                break;
            case R.id.arrowItemView_industry:

                break;
            case R.id.arrowItemView_year:

                break;
            case R.id.arrowItemView_city:

                break;
            case R.id.tv_next:
                if(!haveUploadImg){
                    T.ss("请上传照片");
                    return;
                }

                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE));
                break;

        }
    }

    /**
     * 单文件上传
     */
    private void uploadImage(final File mFile) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("file", "file");
        HttpSender sender = new HttpSender(HttpUrl.uploadFile, "单文件上传", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            haveUploadImg = true;
                            Uri uri = Uri.fromFile(mFile);
                            Glide.with(mActivity).load(uri).into(iv_header);
                        } else {
                            T.ss(description);
                        }

                    }
                },true);
        sender.setContext(mActivity);
        sender.sendPostImage(mFile);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<Uri> mSelected = Matisse.obtainResult(data);//图片集合
                        if (!ListUtils.isEmpty(mSelected)) {
                            try {
                                File mFile = FileUtil.getTempFile(mActivity, mSelected.get(0));
                                uploadImage(mFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }
}
