package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GroupQuit_Bean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("shequn_id")
        private String shequnId;
        @SerializedName("pay_channel")
        private Integer payChannel;
        @SerializedName("order_no")
        private String orderNo;
        @SerializedName("amount")
        private String amount;
    }
}
