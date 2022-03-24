package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeInsiderBean {

    @SerializedName("news_uuid")
    private String newsUuid;
    @SerializedName("news_digest")
    private String newsDigest;
    @SerializedName("news_author")
    private String newsAuthor;
    @SerializedName("news_title")
    private String newsTitle;
    @SerializedName("news_posttime")
    private String newsPosttime;
    @SerializedName("news_content")
    private String news_content;
    @SerializedName("news_url")
    private String news_url;
    @SerializedName("file_url")
    private String file_url;


}
