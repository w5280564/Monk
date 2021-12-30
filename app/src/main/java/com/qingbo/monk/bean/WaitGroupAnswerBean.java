package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WaitGroupAnswerBean {


    @SerializedName("id")
    private String id;
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tags")
    private String tags;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("read_num")
    private String readNum;
    @SerializedName("author")
    private String author;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("is_anonymous")
    private String isAnonymous;
}
