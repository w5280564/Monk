package com.qingbo.monk.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class InterestBean {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("list")
        private List<ListDTO> list;
        @JsonProperty("count")
        private String count;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("group_name")
            private String groupName;
            @JsonProperty("group_des")
            private String groupDes;
            @JsonProperty("group_image")
            private String groupImage;
            @JsonProperty("create_time")
            private String createTime;
            @JsonProperty("update_time")
            private String updateTime;
            @JsonProperty("order")
            private String order;
            @JsonProperty("detail")
            private List<DetailDTO> detail;

            @NoArgsConstructor
            @Data
            public static class DetailDTO {
                @JsonProperty("group_id")
                private String groupId;
                @JsonProperty("avatar")
                private String avatar;
            }
        }
    }
}
