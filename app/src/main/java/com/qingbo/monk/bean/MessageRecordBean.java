package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.ReceiveMessageBean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageRecordBean {


    @SerializedName("id")
    private String id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("description")
    private String description;
    @SerializedName("unread_num")
    private Integer unreadNum;
    @SerializedName("last_msg")
    private ReceiveMessageBean lastMsg;
    @SerializedName("initials")
    private String initials;


}
