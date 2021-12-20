package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockFundBean {

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
        private Integer count;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("title")
            private String title;
            @SerializedName("images")
            private List<String> images;
            @SerializedName("content")
            private String content;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("author_id")
            private String authorId;
            @SerializedName("author_name")
            private String authorName;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("is_anonymous")
            private Integer isAnonymous;
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("follow_num")
            private String followNum;
        }
    }
}
