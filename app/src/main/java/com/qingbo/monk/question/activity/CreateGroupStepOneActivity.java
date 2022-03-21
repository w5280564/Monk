package com.qingbo.monk.question.activity;

import android.widget.EditText;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.RoundImageView;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建社群第一步
 */
public class CreateGroupStepOneActivity extends BaseCameraAndGalleryActivity_Single {
    @BindView(R.id.iv_header_group)
    RoundImageView ivHeaderGroup;
    @BindView(R.id.et_name)
    EditText etName;
    private String group_header;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_group_step_one;
    }

    @Override
    protected void initView() {
        registerEventBus();
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if(event.type == FinishEvent.CREATE_GROUP){
            back();
        }
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
        checkGalleryPermission();
    }



    @Override
    protected void onUploadSuccess(String imageString) {
        group_header = imageString;
        ImgLoader.getInstance().displayCrop(mContext,ivHeaderGroup,group_header,R.mipmap.bg_group_top);
    }

    @Override
    protected void onUploadFailure(String error_info) {


    }

}
