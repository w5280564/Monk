package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Stock_Bean {


    @SerializedName("count")
    private String count;
    @SerializedName("list")
    private List<ListDTO> list;

    @NoArgsConstructor
    @Data
    public static class ListDTO {
        @SerializedName("news_uuid")
        private String newsUuid;
        @SerializedName("news_title")
        private String newsTitle;
        @SerializedName("news_digest")
        private String newsDigest;
        @SerializedName("news_author")
        private String news_Author;
    }
}
