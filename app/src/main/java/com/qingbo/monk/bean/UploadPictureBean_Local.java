package com.qingbo.monk.bean;

import android.net.Uri;

import java.io.Serializable;

/**
 * 图片选择(返回本地图片)
 */
public class UploadPictureBean_Local implements Serializable {


    private int type = 0;// 0-本地图片 1- 添加图标


    private Uri imageUri;//本地路径

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
