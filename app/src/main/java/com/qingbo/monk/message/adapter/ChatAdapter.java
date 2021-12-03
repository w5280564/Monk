package com.qingbo.monk.message.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.qingbo.monk.bean.ServiceChatIndexBean;

import java.util.List;

/**
 * 聊天列表适配器
 */
public class ChatAdapter extends MultipleItemRvAdapter<ServiceChatIndexBean, BaseViewHolder> {

    public static final int TYPE_CHAT_SENDER = 200;
    public static final int TYPE_CHAT_RECEIVER = 300;


    public ChatAdapter(List<ServiceChatIndexBean> data) {
        super(data);
        finishInitialize();
    }

    @Override
    protected int getViewType(ServiceChatIndexBean entity) {
        if (entity.getType() == ServiceChatIndexBean.CHAT_TYPE_SEND) {
            return TYPE_CHAT_SENDER;
        } else if (entity.getType() == ServiceChatIndexBean.CHAT_TYPE_ANSWER) {
            return TYPE_CHAT_RECEIVER;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new SenderViewProvider());
        mProviderDelegate.registerProvider(new ReceiverViewProvider());
    }



}
