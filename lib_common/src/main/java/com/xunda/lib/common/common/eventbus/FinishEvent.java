package com.xunda.lib.common.common.eventbus;

public class FinishEvent {
    public final static int ADD_OR_DELETE_ADDRESS = 1; //增加或删除地址返回
    public final static int AFTER_PAY = 2; //支付成功或失败以后返回
    public final static int REGISTER_APPLY_BACK = 3; //注册企事业单位，业务代表身份信息提交成功以后的返回
    public final static int LOGIN_SUCCESS = 4; //登录成功
    public final static int ReSetPass_SUCCESS = 101; //密码成功
    public final static int Charge_SUCCESS = 102; //充值密码成功
    public final static int ReSetNewPhone = 103; //修改绑定手机号
    public final static int AuditSuccess = 104; //提现成功审核
    public final static int ReSetPayPass_SUCCESS = 105; //充值支付密码成功
    public final static int CODE_LOGIN_SUCCESS = 106; //验证码登录
    public final static int LEAVE_GROUP = 107; //退群/解散群
    public final static int UNBIND_BANK_CARD = 108; //解绑银行卡
    public final static int ADD_BANK_CARD = 109; //添加银行卡
    //操作所属的模块类型(业务线)
    public int type;

    public FinishEvent(int type) {
        this.type = type;
    }

}
