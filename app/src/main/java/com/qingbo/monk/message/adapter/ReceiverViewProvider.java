package com.qingbo.monk.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.DisplayUtil;
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

        RelativeLayout bubble = helper.getView(R.id.bubble);
        ImageView iv_header = helper.getView(R.id.iv_header);
        ImageView iv_image = helper.getView(R.id.iv_image);
        GlideUtils.loadCircleImage(mContext,iv_header, item.getFromHeader());
        helper.setText(R.id.tv_time, StringUtil.getStringValue(item.getTime()));

        if ("text".equals(item.getMsgType())) {
            bubble.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);
            helper.setText(R.id.tv_content, StringUtil.getStringValue(item.getMessage()));
        }else{
            bubble.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            GlideUtils.loadRoundImage(mContext,iv_image, item.getMessage(), 9);
            helper.addOnClickListener(R.id.iv_image);
        }
    }



}
