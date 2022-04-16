package com.qingbo.monk.person.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_More;
import com.qingbo.monk.base.MyConstant;
import com.qingbo.monk.base.baseview.AtEditText;
import com.qingbo.monk.base.livedatas.LiveDataBus;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.bean.UploadPictureBean;
import com.qingbo.monk.question.adapter.ChooseImageAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创作者发表文章
 */
public class MyCrateArticle_Avtivity extends BaseCameraAndGalleryActivity_More implements View.OnClickListener {
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    AtEditText et_content;
    @BindView(R.id.recycleView_image)
    RecyclerView recycleView_image;
    @BindView(R.id.tv_remains_text)
    TextView tv_remains_text;
    @BindView(R.id.tv_remains_image)
    TextView tv_remains_image;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.at_Img)
    ImageView at_Img;

    private List<UploadPictureBean> imageList = new ArrayList<>();
    private List<String> imageStringList = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private TwoButtonDialogBlue mDialog;
    private String mTitle, mContent, images, questionId;
    private String submitRequestUrl, shequn_id;
    private boolean isEdit;
    private String isOriginator;

    public static void actionStart(Context context, OwnPublishBean mQuestionBeanMy, boolean isEdit) {
        Intent intent = new Intent(context, MyCrateArticle_Avtivity.class);
        intent.putExtra("obj", mQuestionBeanMy);
        intent.putExtra("isEdit", isEdit);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param isOriginator 1是创作者
     */
    public static void actionStart(Context context, String isOriginator) {
        Intent intent = new Intent(context, MyCrateArticle_Avtivity.class);
        intent.putExtra("isOriginator", isOriginator);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        initFirstAddData();
        initImageRecyclerViewAndAdapter();

        isOriginator = getIntent().getStringExtra("isOriginator");
        title = "";
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {//编辑
            OwnPublishBean mQuestionBeanMy = (OwnPublishBean) getIntent().getSerializableExtra("obj");
            if (mQuestionBeanMy != null) {
                questionId = mQuestionBeanMy.getId();
                handleEditOtherData(mQuestionBeanMy);
                handleEditImageData(mQuestionBeanMy);
            }
        }
    }

    private void handleEditOtherData(OwnPublishBean mQuestionBeanMy) {
        et_title.setText(StringUtil.getStringValue(mQuestionBeanMy.getTitle()));
        et_content.setText(StringUtil.getStringValue(mQuestionBeanMy.getContent()));
        tv_remains_text.setText(String.format("%s/2000", StringUtil.getEditText(et_content).length()));
        String is_anonymous = mQuestionBeanMy.getIsAnonymous();//1是匿名
        if (TextUtils.equals(is_anonymous, "1")) {
            tvTag.setText("匿名");
            setDrawableLeft(R.mipmap.niming);
            llTag.setTag("1");
        } else {
            tvTag.setText("公开");
            setDrawableLeft(R.mipmap.gongkai);
            llTag.setTag("0");
        }
    }

    private void handleEditImageData(OwnPublishBean mQuestionBeanMy) {
        String images = mQuestionBeanMy.getImages();
        if (!StringUtil.isBlank(images)) {
            List<String> tempStringUrlList = StringUtil.stringToList(images);
            List<UploadPictureBean> urlList = new ArrayList<>();
            for (String imageUrl : tempStringUrlList) {
                UploadPictureBean obj = new UploadPictureBean();
                obj.setImageUrl(imageUrl);
                obj.setType(0);
                urlList.add(obj);
            }

            imageStringList.addAll(tempStringUrlList);
            showImageListImages(urlList);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.mycrate_center;
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(at_Img, 50);
        apply();
        //接收at人的实体类
        LiveDataBus.get().with(MyConstant.FRIEND_DATA, FriendContactBean.class).observe(this, new Observer<FriendContactBean>() {
            @Override
            public void onChanged(FriendContactBean friendContactBean) {
                if (friendContactBean != null) {
                    et_content.addAtContent(friendContactBean.getId(), friendContactBean.getNickname(), friendContactBean.getDescription());
                }
            }
        });
    }


    private void setDrawableLeft(int mipmap) {
        Drawable drawableLeft = getResources().getDrawable(mipmap);
        tvTag.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }


    /**
     * 添加添加图片这个对象
     */
    private void initFirstAddData() {
        UploadPictureBean bean = new UploadPictureBean();
        bean.setType(1);
        imageList.add(bean);
    }


    private void initImageRecyclerViewAndAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false, DisplayUtil.sp2px(mContext, 32), getResources().getColor(R.color.white));
        recycleView_image.addItemDecoration(mItemDecoration);
        recycleView_image.setLayoutManager(layoutManager);
        mAdapter = new ChooseImageAdapter(imageList);
        recycleView_image.setAdapter(mAdapter);
    }

    @OnClick(R.id.ll_tag)
    public void onClick() {
        String tag = (String) llTag.getTag();
        if ("0".equals(tag)) {
            tvTag.setText("匿名");
            setDrawableLeft(R.mipmap.niming);
            llTag.setTag("1");
        } else {
            tvTag.setText("公开");
            setDrawableLeft(R.mipmap.gongkai);
            llTag.setTag("0");
        }
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickPhoto(position);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                deleteImageNew(position);
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_remains_text.setText(String.format("%s/2000", StringUtil.getEditText(et_content).length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_next.setOnClickListener(this);
        at_Img.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        showBackDialog();
    }

    @Override
    public void onLeftClick() {
        showBackDialog();
    }


    private void showBackDialog() {
        getPramsValue();

        if (!StringUtil.isBlank(mTitle) || !StringUtil.isBlank(mContent) || !StringUtil.isBlank(images)) {
            if (mDialog == null) {
                mDialog = new TwoButtonDialogBlue(this, "是否将内容保存至「我-草稿箱」？", "不保存", "保存",
                        new TwoButtonDialogBlue.ConfirmListener() {

                            @Override
                            public void onClickRight() {
                                createOrEditSaveQuestion("1");
                            }

                            @Override
                            public void onClickLeft() {
                                finish();
                            }
                        });
            }

            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            finish();
        }

    }


    @Override
    public void onRightClick() {

    }

    private void getPramsValue() {
        mTitle = StringUtil.getEditText(et_title);
        mContent = StringUtil.getEditText(et_content);
        images = StringUtil.listToString(imageStringList);
    }

    /**
     * 创作者发表文章
     */
    private void createOrEditSaveQuestion(String optype) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("title", mTitle);
        baseMap.put("content", mContent);
        baseMap.put("isAnonymous", (String) llTag.getTag());
        baseMap.put("images", images);
        baseMap.put("draft", optype);
        if (!ListUtils.isEmpty(et_content.getAtList())) {
            StringBuffer alterList = new StringBuffer();
            for (AtEditText.Entity entity : et_content.getAtList()) {
                alterList.append(entity.getId() + ",");
            }
            baseMap.put("alterList", alterList.toString());//at人id
        }
        if (isEdit) {//编辑
            baseMap.put("id", questionId);
        }
        HttpSender sender = new HttpSender(HttpUrl.User_Post_Article, "创作者发表文章", baseMap, new MyOnHttpResListener() {
            @Override
            public void onComplete(String json, int status, String description, String data) {
                if (status == Constants.REQUEST_SUCCESS_CODE) {
                    if (TextUtils.equals(optype, "0")) {
//                        EventBus.getDefault().post(new FinishEvent(FinishEvent.PUBLISH_TOPIC));
                        showToastDialog("发布成功，待审核！");
                    } else if (TextUtils.equals(optype, "1")) {
                        showToastDialog("已保存至草稿箱！");
                    }
                }
            }
        }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }


    /**
     * 点击图片
     */
    private void clickPhoto(int position) {
        int all_size = imageList.size();
        if (all_size < 8) {
            if (position == all_size - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(7 - all_size);
            } else {
                jumpToPhotoShowActivity(position, imageStringList);
            }
        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(List<UploadPictureBean> mTempList) {
        imageList.addAll(imageList.size() - 1, mTempList);
        deleteLastOne();
        tv_remains_image.setText(String.format("%s/6", imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    private void deleteLastOne() {
        if (imageList.size() > 6) {
            imageList.remove(imageList.size() - 1);
        }
    }


    private void deleteImageNew(int position) {
        imageStringList.remove(position);
        imageList.remove(position);
        if (imageList.size() >= 2) {
            UploadPictureBean bean = imageList.get(imageList.size() - 1);
            if (bean.getType() != 1) {
                UploadPictureBean addBean = new UploadPictureBean();
                addBean.setType(1);
                imageList.add(imageList.size(), addBean);
            }
        }
        tv_remains_image.setText(String.format("%s/6", imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onUploadSuccess(List<String> urlList, List<File> fileList) {
        List<UploadPictureBean> uriList = new ArrayList<>();
        for (File mFile : fileList) {
            Uri filePath = FileProvider7.getUriForFile(mContext, mFile);
            UploadPictureBean obj = new UploadPictureBean();
            obj.setImageUri(filePath);
            obj.setType(2);
            uriList.add(obj);
        }
        imageStringList.addAll(urlList);
        showImageListImages(uriList);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

    /**
     * 申请成为创作者
     */
    private void applyCrate() {
        HashMap<String, String> baseMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.User_apply, "申请成为创作者", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            T.s(data, 3000);
                            finish();
                        }
                    }
                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                getPramsValue();
                if (StringUtil.isBlank(mContent)) {
                    T.ss("内容必须填写");
                    return;
                }
                createOrEditSaveQuestion("0");
                break;
            case R.id.at_Img:
                skipAnotherActivity(MyCrateArticle_At.class);
                break;
        }
    }

    /**
     * 申请创作者
     */
    private void apply() {
        if (TextUtils.equals(isOriginator, "0")) {
            new TwoButtonDialogBlue(mActivity, "创作发布需要申请开通创作人权限是否现在去申请？", "取消", "确定", new TwoButtonDialogBlue.ConfirmListener() {
                @Override
                public void onClickRight() {
                    if (TextUtils.equals(isOriginator, "0")) {
                        applyCrate();
                    }
                }

                @Override
                public void onClickLeft() {
                    finish();
                }
            }).show();
        }
    }


}
