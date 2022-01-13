package com.xunda.lib.common.bean;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class UserBean implements Serializable {
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
    private List<ColumnDTO> column;
    @SerializedName("visitor_num")
    private Integer visitorNum;
    @SerializedName("follow_num")
    private String followNum;
    @SerializedName("fans_num")
    private String fansNum;
    @SerializedName("band_wx")
    private Integer bandWx;
    @SerializedName("follow_status")
    private Integer followStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public String getGetResource() {
        return getResource;
    }

    public void setGetResource(String getResource) {
        this.getResource = getResource;
    }

    public String getIsOriginator() {
        return isOriginator;
    }

    public void setIsOriginator(String isOriginator) {
        this.isOriginator = isOriginator;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCover_image() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<ColumnDTO> getColumn() {
        return column;
    }

    public void setColumn(List<ColumnDTO> column) {
        this.column = column;
    }

    public Integer getVisitorNum() {
        return visitorNum;
    }

    public void setVisitorNum(Integer visitorNum) {
        this.visitorNum = visitorNum;
    }

    public String getFollowNum() {
        return followNum;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getBand_wx() {
        return bandWx;
    }

    public void setBandWx(Integer bandWx) {
        this.bandWx = bandWx;
    }

    public Integer getFollow_status() {
        return followStatus;
    }

    public void setFollow_status(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public static class ColumnDTO implements Serializable{
        @SerializedName("id")
        private String id;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("column_name")
        private String columnName;
        @SerializedName("column_url")
        private String columnUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnUrl() {
            return columnUrl;
        }

        public void setColumnUrl(String columnUrl) {
            this.columnUrl = columnUrl;
        }
    }



}
