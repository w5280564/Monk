package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WalletDetailed_Bean {
    @SerializedName("money")
    private String money;
    @SerializedName("trade_desc")
    private String tradeDesc;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("trade_no")
    private String tradeNo;
    @SerializedName("status")
    private String status;
    @SerializedName("order_id")
    private String orderId;
}
