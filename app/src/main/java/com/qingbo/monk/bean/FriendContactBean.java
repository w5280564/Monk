package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FriendContactBean implements Serializable {


    @SerializedName("id")
    private String id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("description")
    private String description;
    @SerializedName("initials")
    private String initials;

    @SerializedName("firstLetter")
    private String firstLetter;
    @SerializedName("item_type")
    private Integer itemType=0;//0是header  1是member
}
