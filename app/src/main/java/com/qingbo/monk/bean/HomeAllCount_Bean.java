package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

@lombok.NoArgsConstructor
@lombok.Data
public class HomeAllCount_Bean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private String data;
}
