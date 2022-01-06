package com.qingbo.monk.message.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 *接收
 */

public class ReceiverViewProvider extends BaseItemProvider<ReceiveMessageBean,BaseViewHolder> {


    @Override
    public int viewType() {
        return ChatAdapter.TYPE_CHAT_RECEIVER;
    }

    @Override
    public int layout() {
        return R.layout.item_chat_receiver;
    }

    @Override
    public void convert(BaseViewHolder helper, ReceiveMessageBean item, int position) {
        helper.setText(R.id.tv_content, StringUtil.getStringValue(item.getMessage()));
        ImageView iv_head = helper.getView(R.id.iv_head);
        GlideUtils.loadCircleImage(mContext,iv_head, SharePref.user().getUserHead());
    }



}
