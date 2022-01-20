package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InterestDetail_Bean {
    @SerializedName("id")
    private String id;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("group_image")
    private String groupImage;
    @SerializedName("group_des")
    private String groupDes;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("create_day")
    private String createDay;
    @SerializedName("status_join")
    private String statusJoin;
    @SerializedName("list")
    private List<String> list;
    @SerializedName("count")
    private String count;
    @SerializedName("status_num")
    private String status_num;

}
