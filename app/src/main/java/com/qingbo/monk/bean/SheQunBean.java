package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SheQunBean {


    @SerializedName("id")
    private String id;
    @SerializedName("shequn_name")
    private String shequnName;
    @SerializedName("shequn_image")
    private String shequnImage;
    @SerializedName("shequn_des")
    private String shequnDes;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("update_time")
    private String updateTime;
    @SerializedName("order")
    private String order;
    @SerializedName("shequn_fee")
    private String shequnFee;
    @SerializedName("detail")
    private List<DetailDTO> detail;

    @NoArgsConstructor
    @Data
    public static class DetailDTO {
        @SerializedName("shequn_id")
        private String shequnId;
        @SerializedName("avatar")
        private String avatar;
    }
}
