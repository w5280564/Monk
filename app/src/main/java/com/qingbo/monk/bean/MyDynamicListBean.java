package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyDynamicListBean extends BaseSplitIndexBean<MyDynamic_Bean> {
    @SerializedName("read_total")
    private String readTotal;
    @SerializedName("comment_total")
    private String commentTotal;
    @SerializedName("liked_total")
    private String likedTotal;
}
