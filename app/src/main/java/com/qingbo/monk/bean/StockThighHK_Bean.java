package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockThighHK_Bean {


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

                @NoArgsConstructor
                @Data
                public static class ItemsDTO {
                    @SerializedName("zyfxptgbl")
                    private String zyfxptgbl;
                    @SerializedName("cgbl")
                    private String cgbl;
                    @SerializedName("gfxz")
                    private String gfxz;
                    @SerializedName("gdmc")
                    private String gdmc;
                    @SerializedName("cgsl")
                    private String cgsl;
                    @SerializedName("cgblbd")
                    private String cgblbd;
                    @SerializedName("zjcgsl")
                    private String zjcgsl;
                }
    }
}
