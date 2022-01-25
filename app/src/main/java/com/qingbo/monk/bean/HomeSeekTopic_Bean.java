package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekTopic_Bean {

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("nickname")
    private String nickname;
}
