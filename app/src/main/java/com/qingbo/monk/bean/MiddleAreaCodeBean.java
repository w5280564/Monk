package com.qingbo.monk.bean;

import java.util.List;

public class MiddleAreaCodeBean {
    private String firstLetter;
    private List<SmallAreaCodeBean> childlist;

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public List<SmallAreaCodeBean> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<SmallAreaCodeBean> childlist) {
        this.childlist = childlist;
    }
}
