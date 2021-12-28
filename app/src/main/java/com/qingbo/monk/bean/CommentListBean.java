package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommentListBean extends BaseSplitIndexBean<CommentBean> {
    @SerializedName("commentData")
    private CommentDataDTO commentData;
    @NoArgsConstructor
    @Data
    public static class CommentDataDTO {
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
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("liked_num")
        private String likedNum;
        @SerializedName("liked_status")
        private Integer likedStatus;
    }


}
