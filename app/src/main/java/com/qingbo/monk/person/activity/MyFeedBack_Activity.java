package com.qingbo.monk.person.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_More;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.bean.UploadPictureBean;
import com.qingbo.monk.question.adapter.ChooseImageAdapter;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.fileprovider.FileProvider7;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 帮助反馈
 */
public class MyFeedBack_Activity extends BaseCameraAndGalleryActivity_More implements View.OnClickListener {
    @BindView(R.id.change_Edit)
    EditText change_Edit;
    @BindView(R.id.changePhone_Edit)
    EditText changePhone_Edit;
    @BindView(R.id.changeCount_Tv)
    TextView changeCount_Tv;
    @BindView(R.id.tv_next)
    TextView tv_next;
    @BindView(R.id.recycleView_image)
    RecyclerView recycleView_image;

    @BindView(R.id.tv_remains_image)
    TextView tv_remains_image;

    private List<UploadPictureBean> imageList = new ArrayList<>();
    private List<String> imageStringList = new ArrayList<>();
    private ChooseImageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_feed_back;
    }

    @Override
    protected void initView() {
        HideIMEUtil.wrap(this, change_Edit);
        HideIMEUtil.wrap(this, changePhone_Edit);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//弹起键盘不遮挡布局，背景布局不会顶起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
    }

    @Override
    protected void initEvent() {
        addEditTextListener();
        tv_next.setOnClickListener(this);
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
     * 添加添加图片这个对象
     */
    private void initFirstAddData() {
        UploadPictureBean bean = new UploadPictureBean();
        bean.setType(1);
        imageList.add(bean);
    }

    private void initImageRecyclerViewAndAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false, DisplayUtil.sp2px(mContext, 30), getResources().getColor(R.color.white));
        recycleView_image.addItemDecoration(mItemDecoration);
        recycleView_image.setLayoutManager(layoutManager);
        mAdapter = new ChooseImageAdapter(imageList);
        recycleView_image.setAdapter(mAdapter);
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

    int allPhoto = 4; //能选择的图片总数

    /**
     * 点击图片
     */
    private void clickPhoto(int position) {
        int all_size = imageList.size();
        if (all_size < allPhoto) {
            if (position == all_size - 1 && imageList.get(position).getType() == 1) {//添加照片
                checkGalleryPermission(5 - all_size);
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
        tv_remains_image.setText(String.format("%s/4", imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    private void deleteLastOne() {
        if (imageList.size() > allPhoto) {
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
        tv_remains_image.setText(String.format("%s/4", imageStringList.size()));
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 给简介editext添加监听
     */
    private void addEditTextListener() {
        change_Edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = change_Edit.getText();
                int len = editable.length();
                //显示还可以输入多少字
                changeCount_Tv.setText(len + "/200");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                int length = change_Edit.getText().length();
                if (length < 20) {
                    T.s("内容不能少于20字", 3000);
                    return;
                }
                if (TextUtils.isEmpty(changePhone_Edit.getText().toString())) {
                    T.s("联系方式不能为空", 3000);
                    return;
                }
                edit_Info();
                break;
        }
    }

    private void edit_Info() {
        String images = StringUtil.listToString(imageStringList);
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("feedback_remark", change_Edit.getText().toString());
        requestMap.put("phone", changePhone_Edit.getText().toString());
        requestMap.put("accessory", images);
        HttpSender httpSender = new HttpSender(HttpUrl.User_FeedBack, "发布意见反馈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s("反馈已提交", 3000);
                    finish();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}