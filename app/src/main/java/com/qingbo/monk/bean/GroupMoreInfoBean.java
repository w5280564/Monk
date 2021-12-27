package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GroupMoreInfoBean implements Serializable {


    @SerializedName("sq_id")
    private String sqId;
    @SerializedName("shequn_name")
    private String shequnName;
    @SerializedName("shequn_image")
    private String shequnImage;
    @SerializedName("shequn_des")
    private String shequnDes;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("tags")
    private String tags;
    @SerializedName("create_day")
    private String createDay;
    @SerializedName("status")
    private String status;
    @SerializedName("role")
    private String role;
    @SerializedName("back")
    private String back;
    @SerializedName("count")
    private String count;
    @SerializedName("share_url")
    private String shareUrl;
}
