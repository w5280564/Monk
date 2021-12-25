package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Character_Bean {
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
    @SerializedName("tag_name")
    private String tag_name;
}
