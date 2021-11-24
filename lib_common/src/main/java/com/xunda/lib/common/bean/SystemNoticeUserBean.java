package com.xunda.lib.common.bean;

import java.util.List;

public class SystemNoticeUserBean {


    private String amount;
    private Long createTime;
    private Integer notifyType;
    private String remark;
    private String subtitle;
    private List<SystemNotifyVoSonListDTO> systemNotifyVoSonList;
    private String title;
    private String userSystemNotifyId;

    public static class SystemNotifyVoSonListDTO {
        private String keyName;
        private String value;

        public String getKeyName() {
            return keyName;
        }

        public String getValue() {
            return value;
        }
    }

    public String getAmount() {
        return amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Integer getNotifyType() {
        return notifyType;
    }

    public String getRemark() {
        return remark;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public List<SystemNotifyVoSonListDTO> getSystemNotifyVoSonList() {
        return systemNotifyVoSonList;
    }

    public String getTitle() {
        return title;
    }

    public String getUserSystemNotifyId() {
        return userSystemNotifyId;
    }
}
