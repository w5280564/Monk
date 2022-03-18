package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemAt_Bean {


    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("source_url")
    private String sourceUrl;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("avatar")
    private String avatar;
}
