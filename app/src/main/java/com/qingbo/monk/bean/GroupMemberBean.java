package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class GroupMemberBean {


    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role")
    private String role;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("type")
    private Integer type=0;// 0-网络图片 1- 添加图标 2 踢人图标
    @SerializedName("isCheck")
    private boolean isCheck;
    @SerializedName("firstLetter")
    private String firstLetter;
    @SerializedName("item_type")
    private Integer itemType=0;//0是header  1是member

    @SerializedName("letterShow")
    private Integer letterShow=0;//0显示  1是不显示
}
