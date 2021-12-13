package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommendLikedStateBena {

    @SerializedName("like_status")
    private Integer like_status;
}
