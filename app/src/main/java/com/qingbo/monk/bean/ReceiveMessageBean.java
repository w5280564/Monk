package com.qingbo.monk.bean;


public class ReceiveMessageBean {
    public static final int CHAT_TYPE_SEND = 1;//发出的内容

    public static final int CHAT_TYPE_RECEIVER = 2;//回答的内容

    private Integer errorCode;//错误代码0为正常
    private String from;
    private String fromName;
    private String fromHeader;
    private String message;
    private String time;
    private String msgType;//text和image

    private int type = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFromHeader() {
        return fromHeader;
    }

    public void setFromHeader(String fromHeader) {
        this.fromHeader = fromHeader;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
