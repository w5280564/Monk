package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ThemeBean {


    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("read_num")
    private String readNum;
    @SerializedName("role")
    private String role;
    @SerializedName("images")
    private String images;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("answer_content")
    private Object answerContent;
    @SerializedName("last_up_time")
    private String lastUpTime;
    @SerializedName("topic_type")
    private String topicType;
    @SerializedName("detail")
    private List<DetailDTO> detail;

    @NoArgsConstructor
    @Data
    public static class DetailDTO {

        @SerializedName("answer_content")
        private String answerContent;
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
