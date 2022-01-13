package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 向别人提问
 */
public class PublisherAskQuestionToPeopleActivity extends BaseCameraAndGalleryActivity_More {
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_to_name)
    TextView tv_to_name;
    @BindView(R.id.tv_remains_text)
    TextView tv_remains_text;
    @BindView(R.id.tv_remains_image)
    TextView tv_remains_image;
    @BindView(R.id.recycleView_image)
    RecyclerView recycleView_image;
    private List<UploadPictureBean> imageList = new ArrayList<>();
    private List<String> imageStringList = new ArrayList<>();
    private ChooseImageAdapter mAdapter;
    private TwoButtonDialogBlue mDialog;
    private String mContent, images;
    private String to_name, to_id,shequn_id;


    public static void actionStart(Context context, String to_name, String to_id, String shequn_id) {
        Intent intent = new Intent(context, PublisherAskQuestionToPeopleActivity.class);
        intent.putExtra("to_name", to_name);
        intent.putExtra("to_id", to_id);
        intent.putExtra("shequn_id", shequn_id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_question_to_people;
    }

    @Override
    protected void initLocalData() {
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
        to_name = getIntent().getStringExtra("to_name");
        to_id = getIntent().getStringExtra("to_id");
        shequn_id = getIntent().getStringExtra("shequn_id");
        tv_to_name.setText(StringUtil.getStringValue(to_name));
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
                tv_remains_text.setText(String.format("%s/120",StringUtil.getEditText(et_content).length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    private void showBackDialog() {
        getPramsValue();

        if (!StringUtil.isBlank(mContent) || !StringUtil.isBlank(images)) {
            if (mDialog == null) {
                mDialog = new TwoButtonDialogBlue(this, "返回将丢失填写的内容，确定返回吗", "取消", "确定",
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


    private void getPramsValue() {
        mContent = StringUtil.getEditText(et_content);
        images = StringUtil.listToString(imageStringList);
    }

    /**
     * 创建提问
     */
    private void createAskQuestion() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("type", "2");
        baseMap.put("id", to_id);
        baseMap.put("shequn_id", shequn_id);
        baseMap.put("content", mContent);
        baseMap.put("images", images);
        baseMap.put("action", "1");
        baseMap.put("optype", "0");//默认是0,0是发布,1是保存

        HttpSender sender = new HttpSender(HttpUrl.createTopic, "创建提问", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            showToastDialog("提交成功！");
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
        if (all_size < 3) {
            if (position == all_size - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(1);
            } else {
                jumpToPhotoShowActivity(position, imageStringList);
            }
        }
    }


    /**
     * 展示选择的图片
     */
    private void showImageListImages(List<UploadPictureBean> mTempList) {
        imageList.addAll(imageList.size()-1, mTempList);
        deleteLastOne();
        tv_remains_image.setText(String.format("%s/1",imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    private void deleteLastOne() {
        if (imageList.size() > 1) {
            imageList.remove(imageList.size() - 1);
        }
    }


    private void deleteImageNew(int position) {
        imageStringList.remove(position);
        imageList.remove(position);
        UploadPictureBean addBean = new UploadPictureBean();
        addBean.setType(1);
        imageList.add(addBean);
        tv_remains_image.setText(String.format("%s/1",imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onUploadSuccess(List<String> urlList,List<File> fileList) {
        List<UploadPictureBean> uriList = new ArrayList<>();
        for (File mFile:fileList) {
            Uri filePath = FileProvider7.getUriForFile(mContext,mFile);
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

    @Override
    public void onBackPressed() {
        showBackDialog();
    }


    @OnClick({R.id.rl_back, R.id.rl_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                showBackDialog();
                break;
            case R.id.rl_publish:
                getPramsValue();

                if (StringUtil.isBlank(mContent) && StringUtil.isBlank(images)) {
                    T.ss("内容、图片必须填写一项");
                    return;
                }

                createAskQuestion();
                break;
        }
    }
}
