package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyDynamic_Bean  implements Serializable {
//            @SerializedName("article_id")
//            private String articleId;
//            @SerializedName("collect_type")
//            private String collectType;
//            @SerializedName("title")
//            private String title;
//            @SerializedName("content")
//            private String content;
//            @SerializedName("images")
//            private String images;
//            @SerializedName("author_id")
//            private String authorId;
//            @SerializedName("create_time")
//            private String createTime;
//            @SerializedName("is_anonymous")
//            private String isAnonymous;
//            @SerializedName("read_num")
//            private String readNum;
//            @SerializedName("is_hot")
//            private String isHot;
//            @SerializedName("type")
//            private String type;
//            @SerializedName("commentcount")
//            private String commentcount;
//            @SerializedName("comment_num")
//            private String commentNum;
//            @SerializedName("follow_status")
//            private String followStatus;
//            @SerializedName("likecount")
//            private String likecount;
//            @SerializedName("liked_num")
//            private String likedNum;
//            @SerializedName("liked_status")
//            private String likedStatus;
//            @SerializedName("like")
//            private Integer like;
//            @SerializedName("biz_id")
//            private String bizId;
//            @SerializedName("author_name")
//            private String authorName;
//            @SerializedName("nickname")
//            private String nickname;
//            @SerializedName("avatar")
//            private String avatar;
//            @SerializedName("tag_name")
//            private String tagName;
//            @SerializedName("company_name")
//            private String companyName;
//            @SerializedName("is_originator")
//            private String isOriginator;
//            @SerializedName("status")
//            private String status;
//            @SerializedName("audit_status")
//            private String auditStatus;


    @SerializedName("id")
    private String id;
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
    @SerializedName("nickname")
    private String nickname;
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
    @SerializedName("read_num")
    private String readNum;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("type")
    private String type;
    @SerializedName("is_hot")
    private String isHot;
    @SerializedName("audit_status")
    private String auditStatus;
    @SerializedName("status")
    private String status;
    @SerializedName("is_originator")
    private String isOriginator;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("liked_status")
    private Integer likedStatus;
    @SerializedName("like")
    private Integer like;

    @SerializedName("collect_type")
    private String collect_type;
    @SerializedName("biz_id")
    private String biz_id;



}
