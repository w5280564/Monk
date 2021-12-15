package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeInsiderHKBean {
    @SerializedName("news_uuid")
    private String newsUuid;
    @SerializedName("item")
    private ItemDTO item;

    @NoArgsConstructor
    @Data
    public static class ItemDTO {
        @SerializedName("reason")
        private String reason;
        @SerializedName("current")
        private String current;
        @SerializedName("code")
        private String code;
        @SerializedName("averageSharePrice")
        private String averageSharePrice;
        @SerializedName("before")
        private String before;
        @SerializedName("relevantEventDate")
        private String relevantEventDate;
        @SerializedName("num")
        private String num;
        @SerializedName("numberOfShares")
        private String numberOfShares;
        @SerializedName("name")
        private String name;
        @SerializedName("category")
        private String category;
        @SerializedName("shareholderName")
        private String shareholderName;
    }
}
