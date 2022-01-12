package com.xunda.lib.common.bean;


import com.google.gson.annotations.SerializedName;


public class UserBean {


    private String id;
    private String avatar;
    private String tagName;
    private String nickname;
    private String description;
    private String country;
    private String city;
    private String county;
    private String email;
    private String industry;
    private String work;
    private String interested;
    private String domain;
    private String resource;
    private String achievement;
    private String research;
    private String getResource;
    private String isOriginator;
    private String visitor_num;
    private String follow_num;
    private String fans_num;
    private int follow_status;
    private String cover_image;
    private int band_wx;
    private String province;

    public String getProvince() {
        return province;
    }
    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTagName() {
        return tagName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDescription() {
        return description;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getEmail() {
        return email;
    }

    public String getIndustry() {
        return industry;
    }

    public String getWork() {
        return work;
    }

    public String getInterested() {
        return interested;
    }

    public String getDomain() {
        return domain;
    }

    public String getResource() {
        return resource;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getResearch() {
        return research;
    }

    public String getGetResource() {
        return getResource;
    }

    public String getIsOriginator() {
        return isOriginator;
    }

    public String getVisitorNum() {
        return visitor_num;
    }

    public String getFollowNum() {
        return follow_num;
    }

    public String getFansNum() {
        return fans_num;
    }

    public int getBand_wx() {
        return band_wx;
    }

    public int getFollow_status() {
        return follow_status;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setFollow_status(int follow_status) {
        this.follow_status = follow_status;
    }
}
