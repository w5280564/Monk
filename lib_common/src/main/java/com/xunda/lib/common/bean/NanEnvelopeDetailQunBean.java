package com.xunda.lib.common.bean;

import java.util.List;

public class NanEnvelopeDetailQunBean{

    private Double amount;
    private Double account;
    private String blessingWord;
    private String fromHeadImg;
    private String fromName;
    private List<GetterListDTO> getterList;
    private Integer isFinish;
    private Integer packetStatus;
    private Integer payResult;
    private Integer redType;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBlessingWord() {
        return blessingWord;
    }

    public void setBlessingWord(String blessingWord) {
        this.blessingWord = blessingWord;
    }

    public String getFromHeadImg() {
        return fromHeadImg;
    }

    public void setFromHeadImg(String fromHeadImg) {
        this.fromHeadImg = fromHeadImg;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public List<GetterListDTO> getGetterList() {
        return getterList;
    }

    public void setGetterList(List<GetterListDTO> getterList) {
        this.getterList = getterList;
    }

    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getPacketStatus() {
        return packetStatus;
    }

    public void setPacketStatus(Integer packetStatus) {
        this.packetStatus = packetStatus;
    }

    public Integer getPayResult() {
        return payResult;
    }

    public void setPayResult(Integer payResult) {
        this.payResult = payResult;
    }

    public Integer getRedType() {
        return redType;
    }

    public void setRedType(Integer redType) {
        this.redType = redType;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getRemainingPacket() {
        return remainingPacket;
    }

    public void setRemainingPacket(Integer remainingPacket) {
        this.remainingPacket = remainingPacket;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalPacket() {
        return totalPacket;
    }

    public void setTotalPacket(Integer totalPacket) {
        this.totalPacket = totalPacket;
    }

    public Double getAccount() {
        return account;
    }

    private Double remainingAmount;
    private Integer remainingPacket;
    private String remark;
    private Double totalAmount;
    private Integer totalPacket;


    public static class GetterListDTO {
        private Double getAmount;
        private Long getTime;
        private String headImg;
        private Integer isBestLuck;

        public Double getGetAmount() {
            return getAmount;
        }

        public void setGetAmount(Double getAmount) {
            this.getAmount = getAmount;
        }

        public Long getGetTime() {
            return getTime;
        }

        public void setGetTime(Long getTime) {
            this.getTime = getTime;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public Integer getIsBestLuck() {
            return isBestLuck;
        }

        public void setIsBestLuck(Integer isBestLuck) {
            this.isBestLuck = isBestLuck;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        private String nickname;
        private String userId;
    }
}
