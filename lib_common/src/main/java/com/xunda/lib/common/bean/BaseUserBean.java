package com.xunda.lib.common.bean;


public class BaseUserBean {


    public UserBean getInfo() {
        return info;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    private UserBean info;
    private String accessToken;
    private String refreshToken;


}
