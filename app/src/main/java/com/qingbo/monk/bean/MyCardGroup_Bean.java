package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MyCardGroup_Bean{
        @SerializedName("id")
        private String id;
        @SerializedName("shequn_name")
        private String shequnName;
        @SerializedName("shequn_des")
        private String shequnDes;
        @SerializedName("shequn_image")
        private String shequnImage;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("create_day")
        private String createDay;
        @SerializedName("is_pass")
        private String isPass;
        @SerializedName("role")
        private String role;
        @SerializedName("detail")
        private List<String> detail;
        @SerializedName("total")
        private String total;
}
