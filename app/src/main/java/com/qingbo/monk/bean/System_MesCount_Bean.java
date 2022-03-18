package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class System_MesCount_Bean {

        @SerializedName("checkCount")
        private String checkCount;
        @SerializedName("commentCount")
        private String commentCount;
        @SerializedName("likeCount")
        private String likeCount;
        @SerializedName("alertCount")
        private String alertCount;
}
