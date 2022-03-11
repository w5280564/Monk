package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemReview_Bean {

    @SerializedName("like_type")
    private String likeType;
    @SerializedName("like_type_name")
    private String likeTypeName;
    @SerializedName("relations_id")
    private String relationsId;
    @SerializedName("user_id")
    private String userId;
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
    }
}
