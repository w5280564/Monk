package com.xunda.lib.common.bean;

import java.io.Serializable;

/**
 * ================================================
 *
 * @Description: 收货地址
 * @Author: Zhangliangliang
 * @CreateDate: 2021/8/12 23:03
 * ================================================
 */
public class NanAddressBean implements Serializable {
    private String address;
    private String areaName;
    private String createTime;
    private int isDefault;  //1默认0非默认
    private String phoneNum;
    private String receivingAddressId;     //地址id
    private String receivingName;     //收件人名
    private String userId;     //用户id
    private boolean isChoosed;  //是否选中为商品收货地址

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getReceivingAddressId() {
        return receivingAddressId;
    }

    public void setReceivingAddressId(String receivingAddressId) {
        this.receivingAddressId = receivingAddressId;
    }

    public String getReceivingName() {
        return receivingName;
    }

    public void setReceivingName(String receivingName) {
        this.receivingName = receivingName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
