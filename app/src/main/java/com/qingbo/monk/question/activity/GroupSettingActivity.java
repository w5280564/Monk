package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.GroupMoreInfoBean;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.EditStringDialog;
import com.qingbo.monk.dialog.GridDialog;
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

    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;
    @BindView(R.id.shangchuan)
    ImageView shangchuan;
    @BindView(R.id.ll_des)
    LinearLayout ll_des;
    @BindView(R.id.iv_more)
    ImageView iv_more;

    private String id,tag;
    private GroupMoreInfoBean sheQunBean;
    private List<NameIdBean> tagList = new ArrayList<>();

    private GridDialog mGridDialog;


    public static void actionStart(Context context, GroupMoreInfoBean sheQunBean) {
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
        sheQunBean = (GroupMoreInfoBean) getIntent().getSerializableExtra("sheQunBean");
        if (sheQunBean!=null) {
            id = sheQunBean.getSqId();
            tag = StringUtil.getStringValue(sheQunBean.getTags());
            tv_group_tag.setText(StringUtil.getStringValue(sheQunBean.getTags()));
            tv_group_des.setText(StringUtil.getStringValue(sheQunBean.getShequnDes()));
            tv_group_name.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
            if (StringUtil.isBlank(sheQunBean.getShequnImage())) {
                iv_header_group.setImageResource(R.mipmap.bg_create_group);
            }else{
                GlideUtils.loadImage(mContext,iv_header_group,sheQunBean.getShequnImage());
            }

            String role = sheQunBean.getRole();
            if ("1".equals(role)||"2".equals(role)||"3".equals(role)) {//1管理员2合伙人0一般用户3群主
                ll_tag.setEnabled(true);
                shangchuan.setEnabled(true);
                ll_des.setEnabled(true);
                tv_group_name.setEnabled(true);
                shangchuan.setVisibility(View.VISIBLE);
                iv_more.setVisibility(View.VISIBLE);
                setTextViewDrawableRight(tv_group_name,true,R.mipmap.bianji_pen,9);
                setTextViewDrawableRight(tv_group_tag,true,R.mipmap.icon_jiantou_down_hui,5);
            }else {
                ll_tag.setEnabled(false);
                shangchuan.setEnabled(false);
                ll_des.setEnabled(false);
                tv_group_name.setEnabled(false);
                shangchuan.setVisibility(View.GONE);
                iv_more.setVisibility(View.GONE);
                setTextViewDrawableRight(tv_group_name,false,0,0);
                setTextViewDrawableRight(tv_group_tag,false,0,0);
            }
        }
    }


    protected void setTextViewDrawableRight(TextView mTextview, boolean isHave, int resource, int padding) {
        if (isHave) {
            Drawable drawableRight = getResources().getDrawable(resource);
            mTextview.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, drawableRight, null);
            mTextview.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext,padding));
        }else{
            mTextview.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
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
                                for (String itemTag:strings) {
                                    NameIdBean mNameIdBean = new NameIdBean();
                                    mNameIdBean.setName(itemTag);
                                    if (!StringUtil.isBlank(tag)){
                                        if (itemTag.equals(tag)) {
                                            mNameIdBean.setSelect(true);
                                        }
                                    }
                                    tagList.add(mNameIdBean);
                                }
                            }
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    @Override
    public void onRightClick() {
        PreviewGroupDetailActivity.actionStart(mActivity,id);
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
                                tv_group_tag.setText(value);
                            }
                            EventBus.getDefault().post(new EditGroupEvent(EditGroupEvent.EDIT_GROUP));
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    @Override
    public void onSelectItem(int position) {
        if (!tagList.get(position).isSelect()) {
            String tag = tagList.get(position).getName();
            for (NameIdBean mObj:tagList) {
                mObj.setSelect(false);
            }
            tagList.get(position).setSelect(true);
            editShequn("tag",tag);
        }
    }
}
