package com.xunda.lib.common.bean;

import java.util.List;


/**
 * ================================================
 *
 * @Description: 城市三级联动
 * ================================================
 */
public class AreaBean {

    private String name;
    private int id;
    private String fullname;
    private List<AreaBean> sub;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public List<AreaBean> getSub() {
        return sub;
    }
}
