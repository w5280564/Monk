package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ArticleLikedBean {
    @SerializedName("id")
    private String id;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("description")
    private String description;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("county")
    private String county;
    @SerializedName("email")
    private String email;
    @SerializedName("industry")
    private String industry;
    @SerializedName("article_num")
    private String articleNum;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("fans_num")
    private String fansNum;
    @SerializedName("follower_num")
    private String followerNum;
    @SerializedName("data_source")
    private String data_source;

}
