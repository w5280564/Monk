package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemReview_Bean {

    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("refuse_msg")
    private String refuseMsg;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("main_id")
    private String mainId;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("content")
    private ContentDTO content;

    @NoArgsConstructor
    @Data
    public static class ContentDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("content")
        private String content;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("author_name")
        private String authorName;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("article_id")
        private String articleId;
        @SerializedName("article_title")
        private String articleTitle;
        @SerializedName("article_content")
        private String articleContent;
        @SerializedName("article_images")
        private String articleImages;
        @SerializedName("shequn_name")
        private String shequn_name;
        @SerializedName("shequn_des")
        private String shequn_des;
        @SerializedName("shequn_image")
        private String shequn_image;

    }
}
