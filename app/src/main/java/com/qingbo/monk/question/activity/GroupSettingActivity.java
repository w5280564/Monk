package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.EditStringDialog;
import com.xunda.lib.common.dialog.GridDialog;
import com.xunda.lib.common.view.RadiusImageWidget;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群编辑
 */
public class GroupSettingActivity extends BaseCameraAndGalleryActivity_Single implements GridDialog.OnSelectItemListener {
    @BindView(R.id.iv_header_group)
    RadiusImageWidget iv_header_group;
    @BindView(R.id.tv_group_tag)
    TextView tv_group_tag;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_des)
    TextView tv_group_des;
    private String id,tag;
    private MyGroupBean sheQunBean;
    private List<NameIdBean> tagList = new ArrayList<>();

    private GridDialog mGridDialog;


    public static void actionStart(Context context, MyGroupBean sheQunBean) {
        Intent intent = new Intent(context, GroupSettingActivity.class);
        intent.putExtra("sheQunBean",sheQunBean);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_setting;
    }


    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void initLocalData() {
        sheQunBean = (MyGroupBean) getIntent().getSerializableExtra("sheQunBean");
        if (sheQunBean!=null) {
            id = sheQunBean.getId();
            tag = StringUtil.getStringValue(sheQunBean.getTags());
            tv_group_tag.setText(tag);
            tv_group_des.setText(StringUtil.getStringValue(sheQunBean.getShequnDes()));
            tv_group_name.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
            if (StringUtil.isBlank(sheQunBean.getShequnImage())) {
                iv_header_group.setImageResource(R.mipmap.bg_create_group);
            }else{
                GlideUtils.loadImage(mContext,iv_header_group,sheQunBean.getShequnImage());
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
                            List<String> strings = StringUtil.stringToList(data);
                            if (!ListUtils.isEmpty(strings)) {
                                for (String item:strings) {
                                    NameIdBean mNameIdBean = new NameIdBean();
                                    mNameIdBean.setName(item);
                                    mNameIdBean.setSelect(tag.equals(item));
                                    tagList.add(mNameIdBean);
                                }
                            }
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }





    @OnClick({R.id.ll_tag,R.id.shangchuan,R.id.tv_group_name,R.id.ll_des})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tag:
                showGridDialog();
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

    private void showGridDialog() {
        if (mGridDialog == null) {
            mGridDialog = new GridDialog(this,tagList,this);
        }else{
            mGridDialog.resetList(tagList);
        }

        if (!mGridDialog.isShowing()) {
            mGridDialog.show();
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
        editShequn("image",imageString);
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
                                GlideUtils.loadImage(mContext,iv_header_group,value);
                            }else if("tag".equals(key)){
                                for (NameIdBean mNameIdBean:tagList) {
                                    mNameIdBean.setSelect(false);
                                }
                                tagList.get(currentPosition).setSelect(true);
                                tv_group_tag.setText(StringUtil.getStringValue(value));
                            }
                            EventBus.getDefault().post(new EditGroupEvent(EditGroupEvent.EDIT_GROUP));
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    private int currentPosition;
    @Override
    public void onSelectItem(int position) {
        currentPosition = position;
        String tag = tagList.get(currentPosition).getName();
        editShequn("tag",tag);
    }
}
