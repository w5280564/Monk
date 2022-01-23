package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WalletDetailBean {
    @SerializedName("money")
    private String money;
    @SerializedName("trade_desc")
    private String trade_desc;
    @SerializedName("create_time")
    private String create_time;
    @SerializedName("trade_no")
    private String trade_no;
    @SerializedName("transaction_id")
    private String transaction_id;
    @SerializedName("refund_id")
    private String refund_id;
    @SerializedName("pay_user_id")
    private String pay_user_id;
    @SerializedName("pay_user_name")
    private String pay_user_name;
    @SerializedName("benefit_user_id")
    private String benefit_user_id;
    @SerializedName("benefit_user_name")
    private String benefit_user_name;
    @SerializedName("trade_state")
    private String trade_state;
}
