package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserBean {

        @SerializedName("id")
        private String id;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("tag_name")
        private String tagName;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("username")
        private String username;
        @SerializedName("description")
        private String description;
        @SerializedName("country")
        private String country;
        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;
        @SerializedName("county")
        private String county;
        @SerializedName("email")
        private String email;
        @SerializedName("industry")
        private String industry;
        @SerializedName("work")
        private String work;
        @SerializedName("interested")
        private String interested;
        @SerializedName("domain")
        private String domain;
        @SerializedName("resource")
        private String resource;
        @SerializedName("achievement")
        private String achievement;
        @SerializedName("research")
        private String research;
        @SerializedName("get_resource")
        private String getResource;
        @SerializedName("is_originator")
        private String isOriginator;
        @SerializedName("article_num")
        private String articleNum;
        @SerializedName("region")
        private String region;
        @SerializedName("cover_image")
        private String coverImage;
        @SerializedName("column")
        private List<?> column;
        @SerializedName("visitor_num")
        private String visitorNum;
        @SerializedName("follow_num")
        private String followNum;
        @SerializedName("fans_num")
        private String fansNum;
        @SerializedName("band_wx")
        private Integer bandWx;
        @SerializedName("follow_status")
        private Integer followStatus;
}
