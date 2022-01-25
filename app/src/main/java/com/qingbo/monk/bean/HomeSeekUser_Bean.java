package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekUser_Bean {
    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("fans_num")
    private String fansNum;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("article_num")
    private String articleNum;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("status")
    private String status;
    @SerializedName("sort")
    private Integer sort;
    @SerializedName("relation")
    private Integer relation;
    @SerializedName("follow_status")
    private Integer followStatus;
}
