package com.qingbo.monk.message.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ServiceChatIndexBean;

/**
 *接收
 */

public class ReceiverViewProvider extends BaseItemProvider<ServiceChatIndexBean,BaseViewHolder> {


    @Override
    public int viewType() {
        return ChatAdapter.TYPE_CHAT_RECEIVER;
    }

    @Override
    public int layout() {
        return R.layout.item_chat_receiver;
    }

    @Override
    public void convert(BaseViewHolder helper, ServiceChatIndexBean item, int position) {

    }



}
