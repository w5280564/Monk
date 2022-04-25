package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyDynamic_MoreItem_Bean implements Serializable {
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
    @SerializedName("is_reprint")
    private String isReprint;
    @SerializedName("pre_author_id")
    private String preAuthorId;
    @SerializedName("pre_author_name")
    private String preAuthorName;
    @SerializedName("pre_article_id")
    private String preArticleId;
    @SerializedName("reprint_type")
    private String reprintType;
    @SerializedName("pre_comment_id")
    private String preCommentId;
    @SerializedName("extra_content")
    private String extraContent;
    @SerializedName("comment_author_id")
    private String commentAuthorId;
    @SerializedName("comment_author_name")
    private String commentAuthorName;
    @SerializedName("comment_comment")
    private String commentComment;
    @SerializedName("create_day")
    private String createDay;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("follow_status")
    private Integer followStatus;
    @SerializedName("liked_status")
    private Integer likedStatus;
    @SerializedName("like")
    private Integer like;
    @SerializedName("source_type")
    private String source_type;


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("detail")
    private List<MyDynamic_MoreItem_Bean.DetailDTO> detail;

    @NoArgsConstructor
    @Data
    public static class DetailDTO {
        @SerializedName("warehouse_id")
        private String warehouseId;
        @SerializedName("name")
        private String name;
        @SerializedName("number")
        private String number;
        @SerializedName("num")
        private String num;
        @SerializedName("position")
        private String position;

        //兴趣组
        @SerializedName("answer_content")
        private String answerContent;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("status_num")
        private int statusNum;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("role")
        private String role;
    }

    //兴趣组
    @SerializedName("shequn_id")
    private String shequnId;
    @SerializedName("topic_id")
    private String topic_id;
    @SerializedName("topic_type")
    private String topicType;
    @SerializedName("role")
    private String role;
    @SerializedName("status_num")
    private int statusNum;
    @SerializedName("isCheck")
    private boolean isCheck;
    @SerializedName("data_source")
    private String data_source;

}




