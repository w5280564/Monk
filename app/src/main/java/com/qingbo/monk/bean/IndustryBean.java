package com.qingbo.monk.bean;

import java.util.List;

public class IndustryBean {


    private String id;
    private String pid;
    private String name;
    private List<IndustryBean> children;

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public List<IndustryBean> getChildren() {
        return children;
    }
}
