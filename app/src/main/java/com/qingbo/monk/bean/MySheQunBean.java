package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MySheQunBean implements Serializable {


    @SerializedName("id")
    private String id;
    @SerializedName("shequn_name")
    private String shequnName;
    @SerializedName("shequn_image")
    private String shequnImage;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("shequn_des")
    private String shequnDes;
    @SerializedName("status")
    private String status;
    @SerializedName("tags")
    private String tags;
    @SerializedName("detail")
    private List<String> detail;
    @SerializedName("total")
    private String total;
}
