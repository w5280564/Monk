package com.qingbo.monk.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.qingbo.monk.R;
import com.qingbo.monk.bean.UploadImageFileBean;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.permission.PermissionManager;
import com.xunda.lib.common.common.utils.CompressUtils;
import com.xunda.lib.common.common.utils.FileUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.dialog.PermissionApplyDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 * 多个图片上传的基类Activity
 *
 */

public abstract class BaseCameraAndGalleryActivity_More extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private static final int APP_SETTINGS_PHOTO = 100;
    private int photo_number = 1;






    /**
     * 从相册获取
     */
    private void gallery() {
        Matisse.from(this)//必须填this
                .choose(MimeType.ofImage())//照片视频全部显示
                .showSingleMediaType(true)
                .countable(true)//有序选择图片
                .capture(true)
                .maxSelectable(photo_number)//最大选择数量
//                .gridExpectedSize(240)//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Dracula)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine())//加载方式
                .capture(true)//设置是否可以拍照
                .captureStrategy(new CaptureStrategy(true, getPackageName()+ ".fileProvider"))//存储到哪里，这里的authority要和Manifest当中保持一致
                .forResult(Constants.PHOTO_REQUEST_GALLERY);//
    }




    /**
     * 检查读写权限
     */
    protected void checkGalleryPermission(int photo_number) {
        this.photo_number = photo_number;
        boolean result = PermissionManager.checkPermission(this, Constants.PERMS_WRITE_READ_CAMERA);
        if (result) {
            gallery();
        }else{
            PermissionManager.requestPermission(this, getString(R.string.permission_write_tip), Constants.WRITE_PERMISSION_CODE, Constants.PERMS_WRITE_READ_CAMERA);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch(requestCode){
            case Constants.WRITE_PERMISSION_CODE:
                if (perms.size() == 3) {
                    gallery();
                }
                break;
        }
    }

    /**
     * 请求权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        /**
         * 若是在权限弹窗中，用户勾选了'NEVER ASK AGAIN.'或者'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (perms.size() > 0) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

                String content = null;
                int request_code = 0;
                switch(requestCode){
                    case Constants.WRITE_PERMISSION_CODE:
                        content = getString(R.string.permission_write_denied);
                        request_code = APP_SETTINGS_PHOTO;
                        break;
                }

                showPermissionApplyDialog(content,request_code,true);
            }else{
                switch(requestCode){
                    case Constants.WRITE_PERMISSION_CODE:
                        showPermissionApplyDialog(getString(R.string.permission_apply_photo_reason),APP_SETTINGS_PHOTO,false);
                        break;
                }
            }
        }

    }




    /**
     * 被拒绝后的权限申请引导弹框
     */
    private void showPermissionApplyDialog(String content, final int request_code, final boolean isNotAskAgain) {
        PermissionApplyDialog mPermissionApplyDialog = new PermissionApplyDialog(this, content, isNotAskAgain,new PermissionApplyDialog.OnJumpToSettingListener() {
            @Override
            public void clickRightButton() {
                if(isNotAskAgain){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult( intent,request_code);
                }else{
                    if(request_code==APP_SETTINGS_PHOTO){
                        checkGalleryPermission(photo_number);
                    }
                }
            }
        });

        mPermissionApplyDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case Constants.PHOTO_REQUEST_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        List<String> mSelectedImagePath = Matisse.obtainPathResult(data);//图片集合
                        if (!ListUtils.isEmpty(mSelectedImagePath)) {
                            List<File> files = new ArrayList<>();
                            for (int i = 0; i < mSelectedImagePath.size(); i++) {
                                File mFile = new File(mSelectedImagePath.get(i));
                                L.e("原图的大小是"+FileUtil.getReadableFileSize(mFile.length()));
                                if (mFile!=null) {
                                    files.add(mFile);
                                }
                            }
                            uploadImage(files);
                        }
                    }
                }
                break;
            case APP_SETTINGS_PHOTO:
                checkGalleryPermission(photo_number);
                break;
        }
    }


    /**
     * 多文件上传
     */
    private void uploadImage(List<File> files) {
        HashMap<String, String> baseMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.uploadFiles, "多文件上传", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            UploadImageFileBean mUploadImageFileBean = GsonUtil.getInstance().json2Bean(data, UploadImageFileBean.class);
                            if (mUploadImageFileBean!=null) {
                                onUploadSuccess(mUploadImageFileBean.getUrlList(),files);
                            }
                        }else{
                            onUploadFailure(description);
                        }
                    }
                },true);
        sender.setContext(mActivity);
        sender.sendPostImages(files);
    }




    /**
     * 上传图片成功的回调
     */
    protected abstract void onUploadSuccess(List<String> urlList,List<File> fileList);


    /**
     *  上传图片失败的回调
     */
    protected abstract void onUploadFailure(String error_info);
}
