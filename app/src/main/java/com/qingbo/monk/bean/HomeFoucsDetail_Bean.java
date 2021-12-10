package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeFoucsDetail_Bean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("detail")
        private DetailDTO detail;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @SerializedName("article_id")
            private String articleId;
            @SerializedName("title")
            private String title;
            @SerializedName("content")
            private String content;
            @SerializedName("images")
            private String images;
            @SerializedName("author_id")
            private String authorId;
            @SerializedName("author_name")
            private String authorName;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("is_anonymous")
            private String isAnonymous;
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("liked_num")
            private String likedNum;
            @SerializedName("comment_num")
            private String commentNum;
            @SerializedName("follow_status")
            private Integer followStatus;
            @SerializedName("liked_status")
            private Integer likedStatus;
        }
    }
}
