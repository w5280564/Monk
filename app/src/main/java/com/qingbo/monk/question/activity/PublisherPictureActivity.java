package com.qingbo.monk.question.activity;

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
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 广场问答发布页
 */
public class PublisherPictureActivity extends BaseCameraAndGalleryActivity_More {
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
    private boolean haveUploadPicture;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_publisher_picture;
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
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(DisplayUtil.dip2px(mContext,32), mContext.getResources().getColor(R.color.white));
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
        if (haveUploadPicture) {
            if (mDialog == null) {
                mDialog = new TwoButtonDialogBlue(this, "是否将内容保存至「我-草稿箱」？", "不保存", "保存",
                        new TwoButtonDialogBlue.ConfirmListener() {

                            @Override
                            public void onClickRight() {
                                finish();
                            }

                            @Override
                            public void onClickLeft() {

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
            llTag.setTag("1");
        }else{
            tvTag.setText("公开");
            llTag.setTag("0");
        }
    }


    @Override
    public void onRightClick() {
        String title = StringUtil.getEditText(et_title);

        String mContent = StringUtil.getEditText(et_content);

        if (StringUtil.isBlank(title)  && StringUtil.isBlank(title) && !haveUploadPicture) {
            T.ss("标题、内容、图片必须填写一项");
            return;
        }

        createTopic(title, mContent);
    }

    /**
     * 创建话题或提问
     */
    private void createTopic(String title, String mPictureContent) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("type", "1");
        baseMap.put("title", title);
        baseMap.put("content", mPictureContent);
        baseMap.put("is_anonymous", (String) llTag.getTag());
        baseMap.put("action", "3");
        baseMap.put("images", "3");
        HttpSender sender = new HttpSender(HttpUrl.createTopic, "创建话题或提问", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            showCommonToastDialog("发布成功！", 1);
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

        if (imageList.size() < 8) {
            if (position == imageList.size() - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(6);
            }

        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(String url) {
        if (imageList.size() < 2) {//第一次添加，把每次选择的图片添加到数组最后的位置
            UploadPictureBean obj = new UploadPictureBean();
            obj.setImageUrl(url);
            imageList.add(imageList.size() - 1, obj);
        } else {//第二次添加，把每次选择的图片添加到index等于0的位置
            UploadPictureBean obj = new UploadPictureBean();
            obj.setImageUrl(url);
            imageList.add(0, obj);

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


    /**
     * 删除动态图片
     */
    private void deleteImage(final int position) {
//        HashMap<String, String> baseMap = new HashMap<String, String>();
//        baseMap.put("index", position + "");
//        HttpSender sender = new HttpSender(HttpUrl.deleteImage_dynamic, "删除动态图片", baseMap,
//                new MyOnHttpResListener() {
//
//                    @Override
//                    public void onComplete(String json, int status, String description, String data) {
//                        if (status == Constants.REQUEST_SUCCESS_STATUS) {//成功
//                            T.ss("删除成功");
//                            imageList.remove(position);
//                            if (imageList.size() >= 2) {
//                                UploadPictureBean_Local bean = imageList.get(imageList.size() - 1);
//                                if (bean.getType() != 1) {
//                                    UploadPictureBean_Local addBean = new UploadPictureBean_Local();
//                                    addBean.setType(1);
//                                    imageList.add(imageList.size(), addBean);
//                                }
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        } else {
//                            T.ss(description);
//                        }
//
//                    }
//                }, true);
//        sender.setContext(mActivity);
//        sender.sendPost();

    }


    @Override
    protected void onUploadSuccess(String imageString) {
        haveUploadPicture = true;
        showImageListImages(imageString);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

}
