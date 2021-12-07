package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

public class testBean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("count")
        private String count;
        @SerializedName("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
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
            private Object shequnId;
            @SerializedName("group_id")
            private String groupId;
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

    }
}
