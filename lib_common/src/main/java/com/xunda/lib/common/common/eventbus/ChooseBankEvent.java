package com.xunda.lib.common.common.eventbus;

import android.app.Activity;

import com.xunda.lib.common.bean.WalletBankListBean;


public class ChooseBankEvent {
    //操作所属的模块类型(业务线)
    public Activity fromFlag;
    public WalletBankListBean obj;

    public ChooseBankEvent(Activity fromFlag, WalletBankListBean obj) {
        this.obj = obj;
        this.fromFlag = fromFlag;
    }
}
