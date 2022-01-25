package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class HomeSeekGroup_Bean {


        @SerializedName("id")
        private String id;
        @SerializedName("shequn_name")
        private String shequnName;
        @SerializedName("shequn_image")
        private String shequnImage;
        @SerializedName("shequn_des")
        private String shequnDes;
        @SerializedName("type")
        private String type;
        @SerializedName("join_num")
        private String joinNum;
        @SerializedName("join_status")
        private String joinStatus;
}
