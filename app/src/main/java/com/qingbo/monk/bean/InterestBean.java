package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class InterestBean {
            @SerializedName("id")
            private String id;
            @SerializedName("group_name")
            private String groupName;
            @SerializedName("group_des")
            private String groupDes;
            @SerializedName("group_image")
            private String groupImage;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("update_time")
            private String updateTime;
            @SerializedName("order")
            private String order;
            @SerializedName("create_day")
            private String createDay;
            @SerializedName("detail")
            private List<DetailDTO> detail;
            @SerializedName("join_num")
            private String joinNum;
            @SerializedName("join_status")
            private String joinStatus;

            @NoArgsConstructor
            @Data
            public static class DetailDTO {
                @SerializedName("group_id")
                private String groupId;
                @SerializedName("avatar")
                private String avatar;
            }
}
