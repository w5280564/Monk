package com.xunda.lib.common.common.eventbus;

/**
 * <p>文件描述：审核展商事件<p>
 * <p>作者：zhangliangliang<p>
 * <p>创建时间：2019/10/23 0023<p>
 * <p>版本号：1<p>
 */
public class ReviewShopEvent {
    public final static int PASSORREFUSE = 1;//审核通过或不通过

    //操作所属的模块类型(业务线)
    public int type;

    public ReviewShopEvent(int type) {
        this.type = type;
    }
}
