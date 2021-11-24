package com.xunda.lib.common.common.eventbus;

/**
 * ================================================
 *
 * @Description: 订单管理事件
 * @Author: Zhangliangliang
 * @CreateDate: 2021/8/21 19:41
 * ================================================
 */
public class NanOrderManageEvent {
    public final static int ORDER_LIST = 1; //订单管理，获取收货地址列表
    public final static int ORDER_REFRESH_DATA = 2; //订单管理，订单操作刷新数据
    //操作所属的模块类型(业务线)
    public int type;
    private Object data;

    public NanOrderManageEvent(int type) {
        this.type = type;
    }

    public NanOrderManageEvent(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
