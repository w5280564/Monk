package com.qingbo.monk.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * 图片选择(返回网络图片)
 */
public class UploadPictureBean implements Serializable {


    private int type = 0;// 0-网络图片 1- 添加图标 2- 本地图片


    private String imageUrl;//图片地址
    private Uri imageUri;//本地图片路径

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}

