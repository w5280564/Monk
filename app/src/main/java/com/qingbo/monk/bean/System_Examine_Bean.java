package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class System_Examine_Bean {
    @SerializedName("parent_id")
    private String parentId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("comment")
    private String comment;
    @SerializedName("comment_type")
    private String commentType;
    @SerializedName("reply_type")
    private String replyType;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("content")
    private String content;
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("title")
    private String title;
    @SerializedName("images")
    private String images;
    @SerializedName("parent")
    private ParentDTO parent;
    @NoArgsConstructor
    @Data
    public static class ParentDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("comment")
        private String comment;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("avatar")
        private String avatar;
    }


}
