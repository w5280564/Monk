package com.qingbo.monk.bean;

public class SmallAreaCodeBean {

    private String area;
    private String code;
    private int classification;//区别section和内容，自己添加:1,section，2内容

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }
}
