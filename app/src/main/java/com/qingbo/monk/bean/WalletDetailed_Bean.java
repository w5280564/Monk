package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WalletDetailed_Bean {
    @SerializedName("money")
    private String money;
    @SerializedName("trade_desc")
    private String trade_desc;
    @SerializedName("create_time")
    private String create_time;
    @SerializedName("trade_no")
    private String trade_no;
    @SerializedName("order_id")
    private String order_id;

}
