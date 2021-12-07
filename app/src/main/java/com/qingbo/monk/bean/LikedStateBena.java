package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LikedStateBena {

    @SerializedName("liked_status")
    private Integer liked_status;
}
