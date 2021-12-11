package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuestionBean {

        @SerializedName("article_id")
        private String articleId;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("author_id")
        private String authorId;
        @SerializedName("tag_name")
        private String tagName;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("images")
        private String images;
        @SerializedName("is_anonymous")
        private String isAnonymous;
        @SerializedName("company_name")
        private String companyName;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("liked_num")
        private String likedNum;
        @SerializedName("comment_num")
        private String commentNum;
        @SerializedName("follow_status")
        private Integer followStatus;
        @SerializedName("liked_status")
        private Integer likedStatus;
        @SerializedName("is_hot")
        private String isHot;
}
