package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchAll_Bean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("people")
        private List<PeopleDTO> people;
        @SerializedName("user")
        private List<UserDTO> user;
        @SerializedName("company")
        private List<CompanyDTO> company;
        @SerializedName("topic")
        private List<TopicDTO> topic;
        @SerializedName("group")
        private List<GroupDTO> group;

        @NoArgsConstructor
        @Data
        public static class PeopleDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("nickname")
            private String nickname;
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("company_name")
            private String companyName;
        }

        @NoArgsConstructor
        @Data
        public static class UserDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("nickname")
            private String nickname;
            @SerializedName("fans_num")
            private String fansNum;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("article_num")
            private String articleNum;
            @SerializedName("tag_name")
            private Object tagName;
            @SerializedName("status")
            private String status;
            @SerializedName("sort")
            private Integer sort;
            @SerializedName("relation")
            private Integer relation;
        }

        @NoArgsConstructor
        @Data
        public static class CompanyDTO {
            @SerializedName("name")
            private String name;
            @SerializedName("number")
            private String number;
        }

        @NoArgsConstructor
        @Data
        public static class TopicDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("title")
            private String title;
            @SerializedName("content")
            private String content;
            @SerializedName("images")
            private String images;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("nickname")
            private String nickname;
        }

        @NoArgsConstructor
        @Data
        public static class GroupDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("shequn_name")
            private String shequnName;
            @SerializedName("shequn_image")
            private String shequnImage;
            @SerializedName("shequn_des")
            private String shequnDes;
            @SerializedName("type")
            private String type;
            @SerializedName("join_num")
            private String joinNum;
            @SerializedName("join_status")
            private Object joinStatus;
        }
    }
}
