package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekGroup_All  {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<HomeSeekGroup_Bean> data;

}
