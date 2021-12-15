package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AAndHkDetail_Bean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("detail")
        private DetailDTO detail;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @SerializedName("news_digest")
            private String newsDigest;
            @SerializedName("news_author")
            private String newsAuthor;
            @SerializedName("news_title")
            private String newsTitle;
            @SerializedName("news_posttime")
            private String newsPosttime;
            @SerializedName("news_content")
            private String newsContent;
            @SerializedName("news_url")
            private String newsUrl;
            @SerializedName("extra_data")
            private ExtraDataDTO extraData;
            @SerializedName("file_url")
            private String fileUrl;

            @NoArgsConstructor
            @Data
            public static class ExtraDataDTO {
                @SerializedName("attach_url")
                private String attachUrl;
                @SerializedName("short_name")
                private String shortName;
                @SerializedName("art_code")
                private String artCode;
                @SerializedName("request_url")
                private String requestUrl;
                @SerializedName("stock_code")
                private String stockCode;
            }
        }
    }
}
