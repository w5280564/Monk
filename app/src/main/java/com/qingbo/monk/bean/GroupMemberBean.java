package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class GroupMemberBean {


    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role")
    private String role;
    @SerializedName("create_time")
    private String createTime;
}
