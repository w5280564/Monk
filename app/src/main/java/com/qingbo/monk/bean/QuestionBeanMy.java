package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuestionBeanMy {


    @SerializedName("id")
    private String id;
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("shequn_id")
    private String shequnId;
    @SerializedName("status")
    private String status;//状态(0待审核 1通过 2未通过 3保存草稿)
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("topic_type")
    private String topicType;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("role")
    private Object role;
    @SerializedName("answername")
    private Object answername;
    @SerializedName("detail")
    private List<?> detail;
    @SerializedName("like")
    private Integer like;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("is_anonymous")
    private String isAnonymous;
}
