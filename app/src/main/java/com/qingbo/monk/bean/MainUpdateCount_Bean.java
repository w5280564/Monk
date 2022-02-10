package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class MainUpdateCount_Bean {

        @SerializedName("new_article")
        private String newArticle;
        @SerializedName("new_square_comment")
        private String newSquareComment;
        @SerializedName("new_shequn_comment")
        private String newShequnComment;
        @SerializedName("new_inter_comment")
        private String newInterComment;
}
