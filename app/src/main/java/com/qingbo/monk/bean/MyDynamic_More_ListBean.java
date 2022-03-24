package com.qingbo.monk.bean;

import com.xunda.lib.common.bean.BaseSplitIndexBean;

public class MyDynamic_More_ListBean extends BaseSplitIndexBean<MyDynamic_MoreItem_Bean> {
    public String getReadTotal() {
        return readTotal;
    }

    public String getCommentTotal() {
        return commentTotal;
    }

    public String getLikedTotal() {
        return likedTotal;
    }

    public void setReadTotal(String readTotal) {
        this.readTotal = readTotal;
    }

    public void setCommentTotal(String commentTotal) {
        this.commentTotal = commentTotal;
    }

    public void setLikedTotal(String likedTotal) {
        this.likedTotal = likedTotal;
    }

    private String readTotal;
    private String commentTotal;
    private String likedTotal;
}
