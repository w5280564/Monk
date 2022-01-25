package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HomeSeekPerson_Bean {
    @SerializedName("id")
    private String id;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("tag_name")
    private String tagName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("company_name")
    private String companyName;
}
