package com.xunda.lib.common.bean;

import java.io.Serializable;

public class NanEnvelopeDetailPerBean implements Serializable {
    private Double amount;
    private String blessingWord;
    private String fromHeadImg;
    private String fromName;
    private Integer packetStatus;
    private Integer payResult;
    private String receiveHeadImg;

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

    public String getReceiveHeadImg() {
        return receiveHeadImg;
    }

    public void setReceiveHeadImg(String receiveHeadImg) {
        this.receiveHeadImg = receiveHeadImg;
    }

    public String getReceiveNickname() {
        return receiveNickname;
    }

    public void setReceiveNickname(String receiveNickname) {
        this.receiveNickname = receiveNickname;
    }

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    private String receiveNickname;
    private Long receiveTime;
}
