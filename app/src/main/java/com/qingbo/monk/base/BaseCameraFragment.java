package com.qingbo.monk.base;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.permission.PermissionManager;
import com.qingbo.monk.R;
import com.xunda.lib.common.dialog.PermissionApplyDialog;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 *
 * 带相机的基类Fragment
 *
 */

public abstract class BaseCameraFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{

    private static final int APP_SETTINGS_CAMERA = 101;


    /**
     * 申请权限成功的回调
     */
    protected abstract void onApplyPermissionSuccess();



    /**
     * 检查相机权限
     */
    protected void checkCameraPermission() {
        boolean result = PermissionManager.checkPermission(mActivity, Constants.PERMS_CAMERA_SCAN_CODE);
        if (result) {
            onApplyPermissionSuccess();
        }else{
            PermissionManager.requestPermission(mActivity, getString(R.string.permission_scan_code_tip), Constants.PERMISSION_CAMERA_SCAN_CODE, Constants.PERMS_CAMERA_SCAN_CODE);
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
            case Constants.PERMISSION_CAMERA_SCAN_CODE:
                onApplyPermissionSuccess();
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
                    case Constants.PERMISSION_CAMERA_SCAN_CODE:
                        content = getString(R.string.permission_scan_code_denied);
                        request_code = APP_SETTINGS_CAMERA;
                        break;
                }

                showPermissionApplyDialog(content,request_code,true);
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
                }
            }
        });

        mPermissionApplyDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==APP_SETTINGS_CAMERA){
            checkCameraPermission();
        }
    }
}
