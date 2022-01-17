package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyCommentBean {

    @SerializedName("author_name")
    private String authorName;
    @SerializedName("comment")
    private String comment;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("article_time")
    private String articleTime;
}
