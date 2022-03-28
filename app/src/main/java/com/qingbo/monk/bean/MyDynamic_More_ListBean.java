package com.qingbo.monk.bean;

import com.xunda.lib.common.bean.BaseSplitIndexBean;

public class MyDynamic_More_ListBean extends BaseSplitIndexBean<MyDynamic_MoreItem_Bean> {
    public String getReadTotal() {
        return read_total;
    }

    public String getCommentTotal() {
        return comment_total;
    }

    public String getLikedTotal() {
        return liked_total;
    }

    public void setReadTotal(String readTotal) {
        this.read_total = readTotal;
    }

    public void setCommentTotal(String commentTotal) {
        this.comment_total = commentTotal;
    }

    public void setLikedTotal(String likedTotal) {
        this.liked_total = likedTotal;
    }

    private String read_total;
    private String comment_total;
    private String liked_total;
}
