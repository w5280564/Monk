package com.xunda.lib.common.bean;


@lombok.NoArgsConstructor
@lombok.Data
public class BaseUserBean {


    private UserBean info;
    private String accessToken;
    private String refreshToken;


}
