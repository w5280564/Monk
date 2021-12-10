package com.qingbo.monk.question.activity;

import android.graphics.drawable.Drawable;
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
import com.qingbo.monk.bean.UploadPictureBean;
import com.qingbo.monk.question.adapter.ChooseImageAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
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
 * 广场问答发布页
 */
public class PublisherActivity extends BaseCameraAndGalleryActivity_More {
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
    private ChooseImageAdapter mAdapter;
    private TwoButtonDialogBlue mDialog;
    private String mTitle,mContent,images;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publisher;
    }

    @Override
    protected void initLocalData() {
        initData();
        initImageRecyclerViewAndAdapter();
    }

    private void initData() {
        UploadPictureBean bean = new UploadPictureBean();
        bean.setType(1);
        imageList.add(bean);
        initImageRecyclerViewAndAdapter();
    }


    private void initImageRecyclerViewAndAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false,32,getResources().getColor(R.color.white));
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
                                createOrSaveTopic("1");
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

        createOrSaveTopic("0");
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
     * 发布话题或提问，或保存至草稿
     */
    private void createOrSaveTopic(String optype) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("type", "1");
        baseMap.put("title", mTitle);
        baseMap.put("content", mContent);
        baseMap.put("is_anonymous", (String) llTag.getTag());
        baseMap.put("action", "3");
        baseMap.put("images", images);
        baseMap.put("optype", optype);//默认是0,0是发布,1是保存
        HttpSender sender = new HttpSender(HttpUrl.createTopic, "创建话题或提问", baseMap,
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
            }
        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(List<String> urlList) {
        if (imageList.size() < 2) {//第一次添加，把每次选择的图片添加到数组最后的位置
            for (int i = 0; i < urlList.size(); i++) {
                UploadPictureBean obj = new UploadPictureBean();
                obj.setImageUrl(urlList.get(i));
                imageList.add(imageList.size() - 1, obj);
            }
        } else {//第二次添加，把每次选择的图片添加到index等于0的位置
            for (int i = 0; i < urlList.size(); i++) {
                UploadPictureBean obj = new UploadPictureBean();
                obj.setImageUrl(urlList.get(i));
                imageList.add(0, obj);
            }
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
        showImageListImages(urlList);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

}
