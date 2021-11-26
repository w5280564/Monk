package com.xunda.lib.common.common.eventbus;

public class LoginMoreInfoEvent {

    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_ZERO = 0;//登录填写更多信息第零步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_ONE = 1;//登录填写更多信息第一步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_TWO = 2;//登录填写更多信息第二步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_THREE = 3;//登录填写更多信息第三步
    public final static int LOGIN_SUBMIT_MORE_INFO_STEP_FOUR = 4;//登录填写更多信息第四步

    //操作所属的模块类型(业务线)
    public int type;

    public LoginMoreInfoEvent(int type) {
        this.type = type;
    }
}
