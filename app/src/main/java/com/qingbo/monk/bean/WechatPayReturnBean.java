package com.qingbo.monk.bean;


import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信支付返回参数
 */
@NoArgsConstructor
@Data
public class WechatPayReturnBean {


    @SerializedName("appId")
    private String appId;
    @SerializedName("timeStamp")
    private String timeStamp;
    @SerializedName("nonceStr")
    private String nonceStr;
    @SerializedName("prepayid")
    private String prepayid;
    @SerializedName("paySign")
    private String paySign;
    @SerializedName("signType")
    private String signType;
    @SerializedName("merchantId")
    private String merchantId;
}
