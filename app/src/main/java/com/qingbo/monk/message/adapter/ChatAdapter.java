package com.qingbo.monk.message.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.xunda.lib.common.bean.ReceiveMessageBean;
import java.util.List;

/**
 * 聊天列表适配器
 */
public class ChatAdapter extends MultipleItemRvAdapter<ReceiveMessageBean, BaseViewHolder> {

    public static final int TYPE_CHAT_SENDER = 200;
    public static final int TYPE_CHAT_RECEIVER = 300;


    public ChatAdapter(List<ReceiveMessageBean> data) {
        super(data);
        finishInitialize();
    }

    @Override
    protected int getViewType(ReceiveMessageBean entity) {
        if (entity.getType() == ReceiveMessageBean.CHAT_TYPE_SEND) {
            return TYPE_CHAT_SENDER;
        } else if (entity.getType() == ReceiveMessageBean.CHAT_TYPE_RECEIVER) {
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
