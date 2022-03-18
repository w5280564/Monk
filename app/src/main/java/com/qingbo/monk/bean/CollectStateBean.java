package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CollectStateBean {

    @SerializedName("collect_status")
    private Integer collect_status;
}
