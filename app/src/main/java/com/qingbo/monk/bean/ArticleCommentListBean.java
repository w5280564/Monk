package com.qingbo.monk.bean;

import com.google.gson.annotations.SerializedName;
import com.xunda.lib.common.bean.BaseSplitIndexBean;

public class ArticleCommentListBean extends BaseSplitIndexBean<ArticleCommentBean> {


    @SerializedName("comment_total")
    private String comment_total;

    public String getComment_total() {
        return comment_total;
    }
}
