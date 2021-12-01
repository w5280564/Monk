package com.xunda.lib.common.common.eventbus;

import org.json.JSONObject;

public class LoginMoreInfoEvent {

    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_ZERO = 0;//登录填写更多信息第零步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_ONE = 1;//登录填写更多信息第一步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_TWO = 2;//登录填写更多信息第二步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_THREE = 3;//登录填写更多信息第三步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_FOUR = 4;//登录填写更多信息第四步

    //操作所属的模块类型(业务线)
    public int type;
    public boolean isNext;
    public String nickname;
    public String city;
    public String county;
    public String work;
    public String industry;

    public String get_resourceOrInterestedOrDescription;

    public LoginMoreInfoEvent(int type) {
        this.type = type;
    }

    public LoginMoreInfoEvent(int type,boolean isNext, String nickname,String city,String county,String work,String industry) {
        this.type = type;
        this.isNext = isNext;
        this.nickname = nickname;
        this.city = city;
        this.county = county;
        this.work = work;
        this.industry = industry;

    }


    public LoginMoreInfoEvent(int type,boolean isNext, String get_resourceOrInterestedOrDescription) {
        this.type = type;
        this.isNext = isNext;
        this.get_resourceOrInterestedOrDescription = get_resourceOrInterestedOrDescription;
    }

}
