package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InterestMember_Bean {

    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("fans_num")
    private String fansNum;
    @SerializedName("follower_num")
    private String followerNum;
    @SerializedName("status")
    private String status;
    @SerializedName("status_num")
    private String status_num;

}
