package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FollowStateBena {

    @SerializedName("follow_status")
    private Integer followStatus;
}
