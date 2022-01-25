package com.qingbo.monk.home.adapter;

import com.google.gson.annotations.SerializedName;
import com.qingbo.monk.bean.HomeSeekFund_Bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekFund_All {

    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @SerializedName("list")
        private ListDTO list;
        @SerializedName("count")
        private String count;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @SerializedName("agu")
            private List<HomeSeekFund_Bean> agu;
            @SerializedName("ganggu")
            private List<HomeSeekFund_Bean> ganggu;
            @SerializedName("meigu")
            private List<MeiguDTO> meigu;
            @SerializedName("jijin")
            private List<JijinDTO> jijin;



            @NoArgsConstructor
            @Data
            public static class GangguDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("number")
                private String number;
            }

            @NoArgsConstructor
            @Data
            public static class MeiguDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("number")
                private String number;
            }

            @NoArgsConstructor
            @Data
            public static class JijinDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("number")
                private String number;
            }
        }
    }
}
