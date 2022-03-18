package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyDynamic_Bean  implements Serializable {

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
}
