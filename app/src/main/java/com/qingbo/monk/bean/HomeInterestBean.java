package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class HomeInterestBean {


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
            @SerializedName("group_name")
            private String groupName;
            @SerializedName("group_image")
            private String groupImage;
            @SerializedName("join_num")
            private String joinNum;
            @SerializedName("join_status")
            private Integer joinStatus;
        }
    }
}
