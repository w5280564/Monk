package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockOrFund_QuestionBean {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("is_anonymous")
    private String isAnonymous;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("status")
    private int status;
    @SerializedName("like")
    private Integer like;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("commentcount")
    private String commentcount;
}
