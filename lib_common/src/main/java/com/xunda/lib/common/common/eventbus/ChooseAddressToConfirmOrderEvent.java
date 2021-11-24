package com.xunda.lib.common.common.eventbus;

import android.app.Activity;

import com.xunda.lib.common.bean.NanAddressBean;


/**
 * 选择地址到确认订单页
 */
public class ChooseAddressToConfirmOrderEvent {


    //操作所属的模块类型(业务线)
    public Activity fromFlag;
    public NanAddressBean obj;


    public ChooseAddressToConfirmOrderEvent(Activity fromFlag, NanAddressBean obj) {
        this.obj = obj;
        this.fromFlag = fromFlag;
    }


}
