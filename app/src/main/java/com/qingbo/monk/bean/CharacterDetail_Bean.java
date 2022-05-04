package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CharacterDetail_Bean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("count")
        private String count;
        @SerializedName("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("nickname")
            private String nickname;
            @SerializedName("description")
            private String description;
            @SerializedName("keywords")
            private String keywords;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("company_name")
            private String companyName;
            @SerializedName("tags")
            private String tags;
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("info")
            private List<InfoDTO> info;

            @SerializedName("stock_name")
            private String stock_name;

            @NoArgsConstructor
            @Data
            public static class InfoDTO {
                @SerializedName("quarters")
                private String quarters;
                @SerializedName("list")
                private List<ListDT1> list;
                @SerializedName("pie_data")
                private PieDataDTO pieData;

                @NoArgsConstructor
                @Data
                public static class PieDataDTO {
                    @SerializedName("legend")
                    private List<String> legend;
                    @SerializedName("data")
                    private List<DataDT1> data;
                    @SerializedName("total")
                    private Double total;

                    @NoArgsConstructor
                    @Data
                    public static class DataDT1 {
                        @SerializedName("name")
                        private String name;
                        @SerializedName("value")
                        private Double value;
                    }
                }

                @NoArgsConstructor
                @Data
                public static class ListDT1 {
                    @SerializedName("number")
                    private String number;
                    @SerializedName("name")
                    private String name;
                    @SerializedName("update_time")
                    private String updateTime;
                    @SerializedName("holding_num")
                    private String holdingNum;
                    @SerializedName("position")
                    private String position;
                    @SerializedName("change")
                    private String change;
                    @SerializedName("industy")
                    private String industy;
                    @SerializedName("shares_type")
                    private String sharesType;
                    @SerializedName("quarters")
                    private String quarters;
                    @SerializedName("total")
                    private String total;
                    @SerializedName("shareholder")
                    private String shareholder;
                    @SerializedName("quarter")
                    private String quarter;
                    @SerializedName("position_id")
                    private String positionId;
                    @SerializedName("positionListName")
                    private String positionListName;
                }
            }
        }
    }


}
