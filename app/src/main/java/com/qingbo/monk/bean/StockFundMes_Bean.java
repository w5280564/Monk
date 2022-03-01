package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockFundMes_Bean {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("author")
    private String author;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("follower_num")
    private String followerNum;
    @SerializedName("like")
    private Integer like;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("data_source")
    private String data_source;

}
