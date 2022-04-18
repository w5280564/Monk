package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentListBean extends BaseSplitIndexBean<CommentBean> implements Serializable {
    @SerializedName("commentData")
    private CommentBean commentData;
//    @NoArgsConstructor
//    @Data
//    public static class CommentDataDTO {
//        @SerializedName("id")
//        private String id;
//        @SerializedName("comment")
//        private String comment;
//        @SerializedName("author_id")
//        private String authorId;
//        @SerializedName("author_name")
//        private String authorName;
//        @SerializedName("audit_status")
//        private String auditStatus;
//        @SerializedName("create_time")
//        private String createTime;
//        @SerializedName("is_anonymous")
//        private String isAnonymous;
//        @SerializedName("avatar")
//        private String avatar;
//        @SerializedName("liked_num")
//        private String likedNum;
//        @SerializedName("liked_status")
//        private Integer likedStatus;
//        @SerializedName("edit")
//        private String edit;
//        @SerializedName("del")
//        private String del;
//
//    }

    @SerializedName("images")
    private String images;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

}
