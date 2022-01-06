package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReceiveMessageBean {

    @SerializedName("errorCode")
    private Integer errorCode;
    @SerializedName("from")
    private Integer from;
    @SerializedName("fromName")
    private String fromName;
    @SerializedName("message")
    private String message;
    @SerializedName("time")
    private String time;
    @SerializedName("msgType")
    private String msgType;
}
