package com.qingbo.monk.bean;

import android.text.Editable;

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
    @SerializedName("article_id")
    private String articleId;
    @SerializedName("shequn_id")
    private String shequnId;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("type")
    private String type;
    @SerializedName("read_num")
    private String readNum;
    @SerializedName("topic_type")
    private String topicType;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("is_anonymous")
    private String isAnonymous;
    @SerializedName("author")
    private String author;
    @SerializedName("author_name")
    private String authorName;
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
    @SerializedName("pre_author_id")
    private String preAuthorId;
    @SerializedName("pre_article_id")
    private String preArticleId;
    @SerializedName("reprint_type")
    private String reprintType;
    @SerializedName("pre_comment_id")
    private String preCommentId;
    @SerializedName("extra_content")
    private String extraContent;
    @SerializedName("is_reprint")
    private String isReprint;
    @SerializedName("source_type")
    private String sourceType;
    @SerializedName("detail")
    private List<DetailDTO> detail;
    @SerializedName("build_id")
    private Integer buildId;
    @SerializedName("like")
    private Integer like;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("liked_num")
    private String likedNum;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("comment_num")
    private String commentNum;
    @SerializedName("status_num")
    private String statusNum;
    @SerializedName("pre_author_name")
    private String preAuthorName;

    @SerializedName("topic_id")
    private String topic_id;
    @SerializedName("isCheck")
    private boolean isCheck;
    @SerializedName("data_source")
    private String data_source;

    @SerializedName("comment_author_name")
    private String commentAuthorName;
    @SerializedName("comment_comment")
    private String commentComment;
    @SerializedName("editableContent")
    private Editable editableContent;
    @SerializedName("html_content")
    private String  html_content;

    @NoArgsConstructor
    @Data
    public static class DetailDTO implements Serializable {

        @SerializedName("answer_content")
        private String answerContent;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("status_num")
        private String statusNum;
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


//    @SerializedName("id")
//    private String id;
//    @SerializedName("author_id")
//    private String authorId;
//    @SerializedName("article_id")
//    private String articleId;
//    @SerializedName("shequn_id")
//    private String shequnId;
//    @SerializedName("topic_id")
//    private String topic_id;
//    @SerializedName("title")
//    private String title;
//    @SerializedName("content")
//    private String content;
//    @SerializedName("topic_type")
//    private String topicType;
//    @SerializedName("images")
//    private String images;
//    @SerializedName("create_time")
//    private String createTime;
//    @SerializedName("nickname")
//    private String nickname;
//    @SerializedName("avatar")
//    private String avatar;
//    @SerializedName("tag_name")
//    private String tagName;
//    @SerializedName("read_num")
//    private String readNum;
//    @SerializedName("role")
//    private String role;
//    @SerializedName("status")
//    private String status;
//    @SerializedName("status_num")
//    private int statusNum;
//    @SerializedName("is_hot")
//    private String isHot;
//    @SerializedName("like")
//    private Integer like;
//    @SerializedName("likecount")
//    private String likecount;
//    @SerializedName("commentcount")
//    private String commentcount;
//    @SerializedName("is_anonymous")
//    private String isAnonymous;
//    @SerializedName("detail")
//    private List<DetailDTO> detail;
//    @SerializedName("isCheck")
//    private boolean isCheck;
//    @SerializedName("data_source")
//    private String data_source;
//
//    @SerializedName("fragmentType")
//    private String fragmentType;
//
//
//    @NoArgsConstructor
//    @Data
//    public static class DetailDTO implements Serializable {
//
//        @SerializedName("answer_content")
//        private String answerContent;
//        @SerializedName("author_id")
//        private String authorId;
//        @SerializedName("status_num")
//        private int statusNum;
//        @SerializedName("nickname")
//        private String nickname;
//        @SerializedName("create_time")
//        private String createTime;
//        @SerializedName("avatar")
//        private String avatar;
//        @SerializedName("role")
//        private String role;
//    }



