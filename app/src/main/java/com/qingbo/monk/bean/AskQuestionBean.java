package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AskQuestionBean {


    @SerializedName("nickname")
    private String nickname;
    @SerializedName("id")
    private String id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("role_name")
    private String roleName;
    @SerializedName("is_question")
    private String isQuestion;//0-不可提问 1-可提问
    @SerializedName("num")
    private String num;
}
