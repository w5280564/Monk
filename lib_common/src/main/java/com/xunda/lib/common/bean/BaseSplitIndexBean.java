package com.xunda.lib.common.bean;


import java.util.List;

/**
 * @文件描述：分页的基类
 */
public class BaseSplitIndexBean<T>{

    public void setList(List<T> list) {
        this.list = list;
    }

    private List<T> list;
    private String count;


    public List<T> getList() {
        return list;
    }

    public String getCount() {
        return count;
    }
}
