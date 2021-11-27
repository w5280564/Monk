package com.qingbo.monk.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qingbo.monk.R;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.permission.PermissionManager;
import com.xunda.lib.common.dialog.ChoosePhotoDialog;
import com.xunda.lib.common.dialog.PermissionApplyDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 * 带图片上传的基类Fragment
 *
 */

public abstract class BaseCameraAndGalleryFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    private static final int APP_SETTINGS_PHOTO = 100;
    private static final int APP_SETTINGS_CAMERA = 101;
    private int photo_number = 1;
    protected  String mCurrentPhotoPath ;//图片路径
    private ChoosePhotoDialog mDialog;


    /**
     * 弹出选择照片的dialog
     */
    protected void showChoosePhotoDialog(int photo_number) {
        this.photo_number = photo_number;

        if (mDialog == null) {
            mDialog = new ChoosePhotoDialog(mActivity,
                    new ChoosePhotoDialog.ChoosePhotoListener() {
                        @Override
                        public void takePhoto() {// 拍照
                            checkCameraPermission();
                        }

                        @Override
                        public void choosePhoto() {// 从相册
                            checkGalleryPermission();
                        }
                    });
        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }




    /**
     * 从拍照获取
     */
    private  void camera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            Uri fileUri = FileProvider7.getUriForFile(mActivity, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, Constants.PHOTO_REQUEST_CAMERA);
        }
    }



    /**
     * 从相册获取
     */
    private void gallery() {
        Matisse.from(this)//必须填this
                .choose(MimeType.ofImage())//照片视频全部显示
                .showSingleMediaType(true)
                .countable(true)//有序选择图片
                .maxSelectable(photo_number)//最大选择数量
//                .gridExpectedSize(240)//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)//图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new GlideEngine())//加载方式
                .capture(false)//设置是否可以拍照
                .captureStrategy(new CaptureStrategy(true, mActivity.getPackageName()+ ".fileProvider"))//存储到哪里，这里的authority要和Manifest当中保持一致
                .forResult(Constants.PHOTO_REQUEST_GALLERY);//
    }




    /**
     * 检查读写权限
     */
    protected void checkGalleryPermission() {
        boolean result = PermissionManager.checkPermission(mActivity, Constants.PERMS_WRITE_READ);
        if (result) {
            gallery();
        }else{
            PermissionManager.requestPermission(mActivity, getString(R.string.permission_write_tip), Constants.WRITE_PERMISSION_CODE, Constants.PERMS_WRITE_READ);
        }
    }



    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        boolean result = PermissionManager.checkPermission(mActivity, Constants.PERMS_CAMERA);
        if (result) {
            camera();
        }else{
            PermissionManager.requestPermission(mActivity, getString(R.string.permission_camera_tip), Constants.PERMISSION_CAMERA_CODE, Constants.PERMS_CAMERA);
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
                if (perms.size() == 2) {
                    gallery();
                }
                break;
            case Constants.PERMISSION_CAMERA_CODE:
                if (perms.size() == 3) {
                    camera();
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
                    case Constants.PERMISSION_CAMERA_CODE:
                        content = getString(R.string.permission_camera_denied);
                        request_code = APP_SETTINGS_CAMERA;
                        break;
                }

                showPermissionApplyDialog(content,request_code,true);
            }else{
                switch(requestCode){
                    case Constants.WRITE_PERMISSION_CODE:
                        showPermissionApplyDialog(getString(R.string.permission_apply_photo_reason),APP_SETTINGS_PHOTO,false);
                        break;
                    case Constants.PERMISSION_CAMERA_CODE:
                        showPermissionApplyDialog(getString(R.string.permission_apply_camera_reason),APP_SETTINGS_CAMERA,false);
                        break;
                }
            }
        }

    }




    /**
     * 被拒绝后的权限申请引导弹框
     */
    private void showPermissionApplyDialog(String content, final int request_code, final boolean isNotAskAgain) {
        PermissionApplyDialog mPermissionApplyDialog = new PermissionApplyDialog(mActivity, content, isNotAskAgain,new PermissionApplyDialog.OnJumpToSettingListener() {
            @Override
            public void clickRightButton() {
                if(isNotAskAgain){
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.fromParts("package", mActivity.getPackageName(), null));
                    startActivityForResult( intent,request_code);
                }else{
                    if(request_code==APP_SETTINGS_PHOTO){
                        checkGalleryPermission();
                    }else if(request_code==APP_SETTINGS_CAMERA){
                        checkCameraPermission();
                    }
                }
            }
        });

        mPermissionApplyDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==APP_SETTINGS_PHOTO){
            checkGalleryPermission();
        }else if(requestCode==APP_SETTINGS_CAMERA){
            checkCameraPermission();
        }
    }
}
