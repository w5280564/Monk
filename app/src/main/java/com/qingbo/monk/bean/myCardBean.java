package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class myCardBean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("list")
        private List<ListDTO> list;
        @SerializedName("count")
        private String count;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("shequn_name")
            private String shequnName;
            @SerializedName("shequn_des")
            private String shequnDes;
            @SerializedName("shequn_image")
            private String shequnImage;
            @SerializedName("nickname")
            private String nickname;
            @SerializedName("user_id")
            private String userId;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("create_day")
            private String createDay;
            @SerializedName("is_pass")
            private String isPass;
            @SerializedName("detail")
            private List<String> detail;
            @SerializedName("total")
            private String total;
        }
    }
}
