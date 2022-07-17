package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockCombinationBean {


            @SerializedName("news_digest")
            private String newsDigest;
            @SerializedName("news_posttime")
            private String newsPosttime;
            @SerializedName("data")
            private DataDTO data;

            @NoArgsConstructor
            @Data
            public static class DataDTO {
                @SerializedName("items")
                private List<ItemsDTO> items;
                @SerializedName("url")
                private String url;

                @NoArgsConstructor
                @Data
                public static class ItemsDTO {
                    @SerializedName("to_float_shares_ratio")
                    private Double toFloatSharesRatio;
                    @SerializedName("cgbl")
                    private Double cgbl;
                    @SerializedName("org_name_or_fund_name")
                    private String orgNameOrFundName;
                    @SerializedName("cgsl")
                    private String cgsl;
                    @SerializedName("gdmc")
                    private String gdmc;
                    @SerializedName("held_num")
                    private String heldNum;
                    @SerializedName("cgblbd")
                    private String cgblbd;
                    @SerializedName("holder_name")
                    private String holderName;
                    @SerializedName("held_ratio")
                    private String heldRatio;
                }
            }
}
