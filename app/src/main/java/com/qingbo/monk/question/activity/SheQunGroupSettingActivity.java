package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.MySheQunBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.EditStringDialog;
import com.xunda.lib.common.view.RadiusImageWidget;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群编辑
 */
public class SheQunGroupSettingActivity extends BaseCameraAndGalleryActivity_Single {
    @BindView(R.id.iv_header_group)
    RadiusImageWidget iv_header_group;
    @BindView(R.id.tv_group_tag)
    TextView tv_group_tag;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_des)
    TextView tv_group_des;
    private String id;
    private String group_header;
    private MySheQunBean sheQunBean;


    public static void actionStart(Context context, MySheQunBean sheQunBean) {
        Intent intent = new Intent(context, SheQunGroupSettingActivity.class);
        intent.putExtra("sheQunBean",sheQunBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shequn_setting;
    }




    @Override
    protected void initLocalData() {
        sheQunBean = (MySheQunBean) getIntent().getSerializableExtra("sheQunBean");
        if (sheQunBean!=null) {
            id = sheQunBean.getId();
            tv_group_tag.setText(StringUtil.getStringValue(sheQunBean.getTags()));
            tv_group_des.setText(StringUtil.getStringValue(sheQunBean.getShequnDes()));
            tv_group_name.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
            group_header = sheQunBean.getShequnImage();
            if (StringUtil.isBlank(group_header)) {
                iv_header_group.setImageResource(R.mipmap.bg_create_group);
            }else{
                GlideUtils.loadImage(mContext,iv_header_group,group_header);
            }
        }
    }

    @Override
    protected void getServerData() {
        getShequnTag();
    }






    /**
     * 获取所有社群标签
     */
    private void getShequnTag() {
        HashMap<String, String> map = new HashMap<String, String>();
        HttpSender sender = new HttpSender(HttpUrl.getShequnTag, "获取所有社群标签", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {

                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(MySheQunBean obj) {
        if (obj != null) {
//            String group_header = obj.getShequnImage();
//            if (StringUtil.isBlank(group_header)) {
//                iv_head_bag.setImageResource(R.mipmap.bg_group_top);
//            }else{
//                GlideUtils.loadImage(mContext,iv_head_bag,group_header);
//            }
        }
    }




    @OnClick({R.id.tv_group_tag,R.id.shangchuan,R.id.tv_group_name,R.id.ll_des})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_group_tag:


                break;
            case R.id.shangchuan:
                checkGalleryPermission();
                break;
            case R.id.tv_group_name:
                editValue("社群名称", tv_group_name, "请输入社群名称", "name");
                break;
            case R.id.ll_des:
                editValue("社群介绍", tv_group_des, "请输入社群介绍", "des");
                break;
        }
    }

    private void editValue(String title, TextView tv_group_des, String hint, String key) {
        String content = tv_group_des.getText().toString();
        showEditStringDialog(title, content, hint, key);
    }

    private void showEditStringDialog(String title,String content,String hint,String key) {
        EditStringDialog mEditStringDialog = new EditStringDialog(this, title, content, hint, new EditStringDialog.OnCompleteListener() {
            @Override
            public void OnComplete(String value) {
                editShequn(key,value);
            }
        });
        mEditStringDialog.show();
    }


    @Override
    protected void onUploadSuccess(String imageString) {
        editShequn("image",group_header);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }



    /**
     * 编辑社群
     */
    private void editShequn(String key,String value) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        baseMap.put(key, value);
        HttpSender sender = new HttpSender(HttpUrl.editShequn, "编辑社群", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("修改成功");
                            if ("name".equals(key)) {
                                tv_group_name.setText(StringUtil.getStringValue(value));
                            }else if("des".equals(key)){
                                tv_group_des.setText(StringUtil.getStringValue(value));
                            }else if("image".equals(key)){
                                group_header = value;
                                GlideUtils.loadImage(mContext,iv_header_group,group_header);
                            }
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }
}
