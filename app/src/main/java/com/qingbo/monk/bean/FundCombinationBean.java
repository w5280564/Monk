package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FundCombinationBean {

    @SerializedName("news_title")
    private String newsTitle;
    @SerializedName("news_author")
    private Object newsAuthor;
    @SerializedName("news_posttime")
    private String newsPosttime;
    @SerializedName("extra")
    private ExtraDTO extra;

    @NoArgsConstructor
    @Data
    public static class ExtraDTO {
        @SerializedName("items")
        private List<ItemsDTO> items;

        @NoArgsConstructor
        @Data
        public static class ItemsDTO {
            @SerializedName("code")
            private String code;
            @SerializedName("name")
            private String name;
            @SerializedName("jzbl")
            private String jzbl;
            @SerializedName("ccsz")
            private String ccsz;
            @SerializedName("cgs")
            private String cgs;
            @SerializedName("change")
            private String change;
        }
    }
}
