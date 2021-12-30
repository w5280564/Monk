package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CheckOtherGroupBean {

    @SerializedName("id")
    private String id;
    @SerializedName("build_id")
    private String buildId;
    @SerializedName("sq_id")
    private String sqId;
    @SerializedName("shequn_name")
    private String shequnName;
    @SerializedName("shequn_des")
    private String shequnDes;
    @SerializedName("shequn_fee")
    private String shequnFee;
    @SerializedName("type")
    private String type;
    @SerializedName("tags")
    private String tags;
    @SerializedName("create_day")
    private String createDay;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role")
    private String role;
    @SerializedName("pay_notice")
    private String payNotice;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("member_count")
    private String memberCount;
    @SerializedName("theme_count")
    private String themeCount;
    @SerializedName("topic_count")
    private String topicCount;
    @SerializedName("last_up_time")
    private String lastUpTime;
}
