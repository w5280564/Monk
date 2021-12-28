package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CombinationDetail_Bean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("detail")
        private List<DetailDTO> detail;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @SerializedName("warehouse_id")
            private String warehouseId;
            @SerializedName("name")
            private Object name;
            @SerializedName("number")
            private Object number;
            @SerializedName("num")
            private String num;
            @SerializedName("position")
            private String position;
        }
    }
}
