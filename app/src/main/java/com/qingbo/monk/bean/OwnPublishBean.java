package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OwnPublishBean implements Serializable {


    @SerializedName("id")
    private String id;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("shequn_id")
    private String shequnId;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("topic_type")
    private String topicType;
    @SerializedName("images")
    private String images;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("role")
    private String role;
    @SerializedName("status")
    private String status;
    @SerializedName("is_hot")
    private String isHot;
    @SerializedName("like")
    private Integer like;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("is_anonymous")
    private String isAnonymous;
    @SerializedName("detail")
    private List<DetailDTO> detail;
    @SerializedName("isCheck")
    private boolean isCheck;

    @NoArgsConstructor
    @Data
    public static class DetailDTO implements Serializable{

        @SerializedName("answer_content")
        private String answerContent;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("role")
        private String role;
    }
}
