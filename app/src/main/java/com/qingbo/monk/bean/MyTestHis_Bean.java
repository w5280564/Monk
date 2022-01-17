package com.qingbo.monk.bean;

import com.baozi.treerecyclerview.annotation.TreeDataType;
import com.google.gson.annotations.SerializedName;
import com.qingbo.monk.person.mygroup.AreaItem;
import com.qingbo.monk.person.mygroup.ProvinceItem;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyTestHis_Bean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    @TreeDataType(iClass = ProvinceItem.class, bindField = "type")
    public static class DataDTO {
        @SerializedName("name")
        private String name;
        @SerializedName("count")
        private Integer count;
        @SerializedName("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        @TreeDataType(iClass = AreaItem.class)
        public static class ListDTO {
            @SerializedName("article_id")
            private String articleId;
            @SerializedName("title")
            private String title;
            @SerializedName("type")
            private String type;
            @SerializedName("content")
            private String content;
            @SerializedName("images")
            private String images;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("author_id")
            private String authorId;
            @SerializedName("author_name")
            private String authorName;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("look_date")
            private String lookDate;
            @SerializedName("is_anonymous")
            private String isAnonymous;
            @SerializedName("liked_num")
            private String likedNum;
            @SerializedName("liked_status")
            private Integer likedStatus;
            @SerializedName("comment_num")
            private String commentNum;
        }
    }


}
