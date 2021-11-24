package com.xunda.lib.common.bean;


public class AppConfigBean {


    private double maxSingleRechargeAmount;
    private double minSingleRechargeAmount;

    private double maxSingleWithdrawalAmount;
    private double minSingleWithdrawalAmount;

    private double maxTransferAmount;
    private double minTransferAmount;

    private double friendPacketMaxAmount;
    private double exclusivePacketMaxAmount;


    public double getMaxSingleRechargeAmount() {
        return maxSingleRechargeAmount;
    }

    public double getMinSingleRechargeAmount() {
        return minSingleRechargeAmount;
    }

    public double getMaxSingleWithdrawalAmount() {
        return maxSingleWithdrawalAmount;
    }

    public double getMinSingleWithdrawalAmount() {
        return minSingleWithdrawalAmount;
    }

    public double getMaxTransferAmount() {
        return maxTransferAmount;
    }

    public double getMinTransferAmount() {
        return minTransferAmount;
    }

    public double getFriendPacketMaxAmount() {
        return friendPacketMaxAmount;
    }

    public double getExclusivePacketMaxAmount() {
        return exclusivePacketMaxAmount;
    }
}
