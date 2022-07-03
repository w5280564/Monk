package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FindBean {


    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("order")
    private String order;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("article_num")
    private String articleNum;
    @SerializedName("city")
    private String city;
    @SerializedName("description")
    private String description;
    @SerializedName("follow_num")
    private String followNum;
    @SerializedName("fans_num")
    private String fansNum;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("industry")
    private String industry;
    @SerializedName("work")
    private String work;

}
