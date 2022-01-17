package com.qingbo.monk.person.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_More;

import java.io.File;
import java.util.List;

public class MyFeedBack_Activity extends BaseCameraAndGalleryActivity_More {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_feed_back;
    }

    @Override
    protected void onUploadSuccess(List<String> urlList, List<File> fileList) {

    }

    @Override
    protected void onUploadFailure(String error_info) {

    }
}