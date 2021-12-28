package com.qingbo.monk.bean;

import java.io.Serializable;

/**
 *
 */
public class ChooseMemberBean implements Serializable {


    private int type = 0;// 0-网络图片 1- 添加图标 2 踢人图标


    private String imageUrl;//图片地址

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
}
