package com.qingbo.monk.question.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.FileUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.zhihu.matisse.Matisse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建社群第一步
 */
public class CreateGroupStepOneActivity extends BaseCameraAndGalleryActivity {
    @BindView(R.id.iv_header_group)
    ImageView ivHeaderGroup;
    @BindView(R.id.et_name)
    EditText etName;
    private String group_header;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_group_step_one;
    }

    @Override
    public void onRightClick() {
        String group_name = StringUtil.getEditText(etName);
        if (StringUtil.isBlank(group_name)) {
            T.ss("请输入社群名称");
            return;
        }

        CreateGroupStepTwoActivity.actionStart(mActivity, group_name,group_header);
    }


    @OnClick(R.id.shangchuan)
    public void onClick() {
        checkGalleryPermission(1);
    }


    /**
     * 单文件上传
     */
    private void uploadImage(final File mFile) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("file", "file");
        HttpSender sender = new HttpSender(HttpUrl.uploadFile, "上传社群头像", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            group_header = GsonUtil.getInstance().getValue(data,"file");
                            GlideUtils.loadRoundImage(mContext,ivHeaderGroup,group_header,15);
                        }
                    }
                },true);
        sender.setContext(mActivity);
        sender.sendPostImage(mFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
