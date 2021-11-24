package com.xunda.lib.common.common.http;


import com.xunda.lib.common.common.Config;


/**
 * H5链接地址
 *
 * @author ouyang
 */
public class H5Url {


    public static String server = Config.Link.getWholeUrl();// 全局接口地址


    /**
     * 隐私政策H5
     */
    public static final String H5PrivatePolicy = server + "privacy.html";

    /**
     * 用户协议H5
     */
    public static final String H5UserPolicy = server + "protocol.html";

    /**
     * 充值和提现须知
     */
    public static final String H5ChargePolicy = server + "finance_protocol.html";

    /**
     * 招商
     */
    public static final String H5Activity = server + "activity.html";

}