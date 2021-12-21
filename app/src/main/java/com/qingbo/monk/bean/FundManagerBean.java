package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FundManagerBean {

    @SerializedName("news_uuid")
    private String newsUuid;
    @SerializedName("news_title")
    private String newsTitle;
    @SerializedName("news_content")
    private String newsContent;
    @SerializedName("news_posttime")
    private String newsPosttime;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("data")
    private DataDTO data;
    @SerializedName("days")
    private String days;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("items")
        private List<DataDTO.ItemsDTO> items;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @SerializedName("rzts")
            private String rzts;
            @SerializedName("code")
            private String code;
            @SerializedName("tlpm")
            private String tlpm;
            @SerializedName("tlpj")
            private String tlpj;
            @SerializedName("jzsj")
            private String jzsj;
            @SerializedName("jjmc")
            private String jjmc;
            @SerializedName("rzhb")
            private String rzhb;
            @SerializedName("qssj")
            private String qssj;
            @SerializedName("jjlx")
            private String jjlx;
        }
    }
}
