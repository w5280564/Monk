package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ForWardBean implements Serializable {
    @SerializedName("artOrComID")
    String artOrComID;
    @SerializedName("name")
    String name;
    @SerializedName("commentAuthorId")
    String commentAuthorId;
    @SerializedName("commentId") //评论ID
    String commentId;
    @SerializedName("comment")
    String comment;
    @SerializedName("imgurl")
    String imgurl;
    @SerializedName("type")
    String type;
    @SerializedName("isStockOrFund")
    boolean isStockOrFund;
    @SerializedName("title")
    String title;
    @SerializedName("content")
    String content;

    @SerializedName("groupId") //社群/兴趣组ID
    String groupId;
    @SerializedName("groupOrInterest")
    String groupOrInterest;

    @SerializedName("isGroup")
    boolean isGroup;


}
