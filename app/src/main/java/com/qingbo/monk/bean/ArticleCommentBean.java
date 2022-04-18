package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ArticleCommentBean implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("audit_status")
    private String auditStatus;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("is_anonymous")
    private String isAnonymous;
    @SerializedName("children_num")
    private String childrenNum;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("childrens")
    private List<ChildrensDTO> childrens;
    @SerializedName("liked_num")
    private String likedNum;
    @SerializedName("liked_status")
    private Integer likedStatus;
    @SerializedName("edit")
    private String edit;
    @SerializedName("del")
    private String del;


    @NoArgsConstructor
    @Data
    public static class ChildrensDTO implements Serializable {
        @SerializedName("parent_id")
        private String parentId;
        @SerializedName("comment_id")
        private String commentId;
        @SerializedName("comment")
        private String comment;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("author_name")
        private String authorName;
        @SerializedName("replyer_id")
        private String replyerId;
        @SerializedName("replyer_name")
        private String replyerName;
        @SerializedName("audit_status")
        private String auditStatus;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("edit")
        private String edit;
        @SerializedName("del")
        private String del;

    }

    @SerializedName("images")
    private String images;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

}
