package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeCombinationBean {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("detail")
        private List<DetailDTO> detail;
        @SerializedName("like")
        private Integer like;
        @SerializedName("likecount")
        private String likecount;
        @SerializedName("commentcount")
        private String commentcount;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @SerializedName("warehouse_id")
            private String warehouseId;
            @SerializedName("name")
            private String name;
            @SerializedName("number")
            private String number;
            @SerializedName("num")
            private String num;
            @SerializedName("position")
            private String position;
        }
}

