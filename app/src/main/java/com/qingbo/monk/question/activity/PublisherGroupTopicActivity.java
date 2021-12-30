package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_More;
import com.qingbo.monk.bean.QuestionBeanMy;
import com.qingbo.monk.bean.UploadPictureBean;
import com.qingbo.monk.question.adapter.ChooseImageAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群话题发布页
 */
public class PublisherGroupTopicActivity extends BaseCameraAndGalleryActivity_More {
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.recycleView_image)
    RecyclerView recycleView_image;
    private List<UploadPictureBean> imageList = new ArrayList<>();
    private List<String> imageStringList = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private TwoButtonDialogBlue mDialog;
    private String mTitle,mContent,images,questionId;
    private String submitRequestUrl,shequn_id;
    private boolean isEdit;


    public static void actionStart(Context context, String shequn_id, QuestionBeanMy mQuestionBeanMy,boolean isEdit) {
        Intent intent = new Intent(context, PublisherGroupTopicActivity.class);
        intent.putExtra("shequn_id",shequn_id);
        intent.putExtra("obj",mQuestionBeanMy);
        intent.putExtra("isEdit",isEdit);
        context.startActivity(intent);
    }
    public static void actionStart(Context context, String shequn_id) {
        Intent intent = new Intent(context, PublisherGroupTopicActivity.class);
        intent.putExtra("shequn_id",shequn_id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publisher;
    }

    @Override
    protected void initLocalData() {
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
        title = "社群话题";
        shequn_id = getIntent().getStringExtra("shequn_id");
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        if (isEdit) {//编辑
            submitRequestUrl = HttpUrl.editQuestion;
            QuestionBeanMy mQuestionBeanMy = (QuestionBeanMy) getIntent().getSerializableExtra("obj");
            if (mQuestionBeanMy!=null) {
                questionId = mQuestionBeanMy.getId();
                handleEditOtherData(mQuestionBeanMy);
                handleEditImageData(mQuestionBeanMy);
            }
        }else{
            submitRequestUrl = HttpUrl.createTopic;
        }

    }

    private void handleEditOtherData(QuestionBeanMy mQuestionBeanMy) {
        et_title.setText(StringUtil.getStringValue(mQuestionBeanMy.getTitle()));
        et_content.setText(StringUtil.getStringValue(mQuestionBeanMy.getContent()));
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

    private void handleEditImageData(QuestionBeanMy mQuestionBeanMy) {
        String images = mQuestionBeanMy.getImages();
        if (!StringUtil.isBlank(images)) {
            List<String> urlList = StringUtil.stringToList(images);
            imageStringList.addAll(urlList);
            showImageListImages(urlList);
        }
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
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false, DisplayUtil.sp2px(mContext,32),getResources().getColor(R.color.white));
        recycleView_image.addItemDecoration(mItemDecoration);
        recycleView_image.setLayoutManager(layoutManager);
        mAdapter = new ChooseImageAdapter(imageList);
        recycleView_image.setAdapter(mAdapter);
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

        if (!StringUtil.isBlank(mTitle)  || !StringUtil.isBlank(mContent) || !StringUtil.isBlank(images)) {
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


    @OnClick(R.id.ll_tag)
    public void onClick() {
        String tag = (String) llTag.getTag();
        if ("0".equals(tag)) {
            tvTag.setText("匿名");
            setDrawableLeft(R.mipmap.niming);
            llTag.setTag("1");
        }else{
            tvTag.setText("公开");
            setDrawableLeft(R.mipmap.gongkai);
            llTag.setTag("0");
        }
    }

    private void setDrawableLeft(int mipmap) {
        Drawable drawableLeft = getResources().getDrawable(
                mipmap);
        tvTag.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }


    @Override
    public void onRightClick() {
        getPramsValue();

        if (StringUtil.isBlank(mTitle)  && StringUtil.isBlank(mContent) && StringUtil.isBlank(images)) {
            T.ss("标题、内容、图片必须填写一项");
            return;
        }

        createOrEditSaveQuestion("0");
    }

    private void getPramsValue() {
        mTitle = StringUtil.getEditText(et_title);

        mContent = StringUtil.getEditText(et_content);

        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (UploadPictureBean mImageObj : imageList) {
            if (mImageObj.getType() != 1) {
                if (flag) {
                    result.append(",");
                } else {
                    flag = true;
                }
                result.append(mImageObj.getImageUrl());
            }
        }
        images = result.toString();
    }

    /**
     * 创建或编辑话题或提问，或保存至草稿
     */
    private void createOrEditSaveQuestion(String optype) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("title", mTitle);
        baseMap.put("content", mContent);
        baseMap.put("is_anonymous", (String) llTag.getTag());
        baseMap.put("images", images);
        baseMap.put("shequn_id", shequn_id);
        if (isEdit) {//编辑
            baseMap.put("id", questionId);
        }else{
            baseMap.put("type", "1");
            baseMap.put("action", "1");
            baseMap.put("optype", optype);//默认是0,0是发布,1是保存
        }

        HttpSender sender = new HttpSender(submitRequestUrl, " 创建或编辑话题或提问，或保存至草稿", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            if ("0".equals(optype)) {
                                EventBus.getDefault().post(new FinishEvent(FinishEvent.PUBLISH_QUESTION));
                                showToastDialog("发布成功！");
                            }else{
                                showToastDialog("已保存至草稿箱！");
                            }
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }


    private void showToastDialog(String description) {//1 点击确定会返回， 0 只是弹窗消失
        ToastDialog mDialog = new ToastDialog(this, getString(R.string.toast_warm_prompt), description, getString(R.string.Sure), new ToastDialog.DialogConfirmListener() {
            @Override
            public void onConfirmClick() {
                back();
            }
        });
        mDialog.show();
    }


    /**
     * 点击图片
     */
    private void clickPhoto(int position) {
        int all_size = imageList.size();
        if (all_size < 8) {
            if (position == all_size - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(7-all_size);
            }else{
                jumpToPhotoShowActivity(position,imageStringList);
            }
        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(List<String> urlList) {
        for (int i = 0; i < urlList.size(); i++) {
            UploadPictureBean obj = new UploadPictureBean();
            obj.setImageUrl(urlList.get(i));
            imageList.add(imageList.size()-1, obj);
        }
        deleteLastOne();
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
        mAdapter.notifyDataSetChanged();
    }




    @Override
    protected void onUploadSuccess(List<String> urlList) {
        imageStringList.addAll(urlList);
        showImageListImages(urlList);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

}
