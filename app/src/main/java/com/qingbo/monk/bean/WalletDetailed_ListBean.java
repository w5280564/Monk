package com.qingbo.monk.bean;

import com.xunda.lib.common.bean.BaseSplitIndexBean;


public class WalletDetailed_ListBean extends BaseSplitIndexBean<WalletDetailed_Bean> {
    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    //    @SerializedName("income")
    private String income;
//    @SerializedName("refund")
    private String refund;
//    @SerializedName("withdraw")
    private String withdraw;
//    @SerializedName("month")
    private String month;


}
