package com.qingbo.monk.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.bumptech.glide.Glide;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryFragment;
import com.qingbo.monk.login.activity.AreaCodeListActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.FileUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.PickStringDialog;
import com.xunda.lib.common.view.MyArrowItemView;
import com.zhihu.matisse.Matisse;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    @BindView(R.id.arrowItemView_year)
    MyArrowItemView arrowItemView_year;
    private boolean haveUploadImg;
    private ActivityResultLauncher mActivityResultLauncher;
    private String[] yearList = {"1-3年","3-5年","5-10年","10-15年"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment_login;
    }


    @Override
    protected void initView() {
        GlideUtils.loadCircleImage(mContext,iv_header, SharePref.user().getUserHead());
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result!=null) {
                    int resultCode = result.getResultCode();
                    if (resultCode==Activity.RESULT_OK) {
//                        area_code = result.getData().getStringExtra("area_code");
//                        tv_number_before.setText("+"+area_code);
                    }
                }
            }
        });
    }



    @OnClick({R.id.iv_header,R.id.arrowItemView_industry,R.id.arrowItemView_year,R.id.arrowItemView_city,R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_header:
                checkGalleryPermission(1);
                break;
            case R.id.arrowItemView_industry:
                Intent intent = new Intent(mActivity, AreaCodeListActivity.class);
                mActivityResultLauncher.launch(intent);
                break;
            case R.id.arrowItemView_year:
                showPickStringDialog();
                break;
            case R.id.arrowItemView_city:

                break;
            case R.id.tv_next:
//                if(!haveUploadImg){
//                    T.ss("请上传照片");
//                    return;
//                }

                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE));
                break;

        }
    }

    private void showPickStringDialog() {
        PickStringDialog mPickStringDialog = new PickStringDialog(mActivity, Arrays.asList(yearList),"", new PickStringDialog.ChooseCallback() {
            @Override
            public void onConfirm(String value) {
                arrowItemView_year.getTip().setVisibility(View.GONE);
                arrowItemView_year.getTvContent().setText(value);
            }
        });
        mPickStringDialog.show();
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
