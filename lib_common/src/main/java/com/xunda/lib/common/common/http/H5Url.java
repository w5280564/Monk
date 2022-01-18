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
    public static final String H5PrivatePolicy = "http://shjr.gsdata.cn/privacyPolicy";

    /**
     * 用户协议H5
     */
    public static final String H5UserPolicy = "http://shjr.gsdata.cn/userAgreement";


}