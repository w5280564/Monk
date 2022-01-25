package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WalletDetailed_ListBean extends BaseSplitIndexBean<WalletDetailed_Bean> {
    @SerializedName("income")
    private String income;
    @SerializedName("refund")
    private String refund;
    @SerializedName("withdraw")
    private String withdraw;
    @SerializedName("month")
    private String month;

}
