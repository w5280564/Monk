package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CombinationLineChart_Bean {
        @SerializedName("datetime")
        private List<String> datetime;
        @SerializedName("dataName")
        private List<String> dataName;
        @SerializedName("data")
        private DataDT1 data;

        @NoArgsConstructor
        @Data
        public static class DataDT1 {
            @SerializedName("jingzhi_line")
            private List<String> jingzhiLine;
        }
}
