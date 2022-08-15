package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateDataBean implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("zanCount")
    String zanCount;
    @SerializedName("zanState")
    Integer zanState;
    @SerializedName("isCollect")
    String isCollect;
    @SerializedName("followState")
    Integer followState;

}
