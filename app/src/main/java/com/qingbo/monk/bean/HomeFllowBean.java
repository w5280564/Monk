package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class HomeFllowBean {

    @SerializedName("article_id")
    private String articleId;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("images")
    private String images;
    @SerializedName("liked_num")
    private String likedNum;
    @SerializedName("comment_num")
    private String commentNum;
    @SerializedName("type")
    private String type;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("is_anonymous")
    private String isAnonymous;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("action")
    private String action;
    @SerializedName("shequn_id")
    private String shequnId;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("is_json")
    private String is_json;
    @SerializedName("follow_status")
    private int follow_status;
    @SerializedName("liked_status")
    private int liked_status;
    @SerializedName("shequn")
    private ShequnDTO shequn;
    @SerializedName("group")
    private GroupDTO group;

    @NoArgsConstructor
    @Data
    public static class GroupDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("group_name")
        private String groupName;
        @SerializedName("group_image")
        private String groupImage;
        @SerializedName("group_des")
        private String groupDes;
        @SerializedName("user_avatar")
        private String userAvatar;
    }

    @NoArgsConstructor
    @Data
    public static class ShequnDTO {
        @SerializedName("id")
        private String id;
        @SerializedName("shequn_name")
        private String shequnName;
        @SerializedName("shequn_image")
        private String shequnImage;
        @SerializedName("shequn_des")
        private String shequnDes;
        @SerializedName("user_avatar")
        private String userAvatar;
    }


}
