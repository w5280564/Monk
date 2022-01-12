package com.qingbo.monk.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.xunda.lib.common.bean.ReceiveMessageBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.XunDaImageView;

/**
 * 发送
 */
public class SenderViewProvider extends BaseItemProvider<ReceiveMessageBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return ChatAdapter.TYPE_CHAT_SENDER;
    }

    @Override
    public int layout() {
        return R.layout.item_chat_sender;
    }

    @Override
    public void convert(BaseViewHolder helper, ReceiveMessageBean item, int position) {
        RelativeLayout bubble = helper.getView(R.id.bubble);
        ImageView iv_header = helper.getView(R.id.iv_header);
        XunDaImageView iv_image = helper.getView(R.id.iv_image);
        GlideUtils.loadCircleImage(mContext,iv_header, SharePref.user().getUserHead());
        helper.setText(R.id.tv_time, StringUtil.getStringValue(item.getTime()));

        if (ReceiveMessageBean.MESSAGE_TYPE_TEXT.equals(item.getMsgType())) {
            bubble.setVisibility(View.VISIBLE);
            iv_image.setVisibility(View.GONE);
            helper.setText(R.id.tv_content, StringUtil.getStringValue(item.getMessage()));
        }else{
            bubble.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            ImgLoader.getInstance().displayCrop(mContext, iv_image, StringUtil.getStringValue(item.getMessage()), R.mipmap.img_pic_none_square);
            helper.addOnClickListener(R.id.iv_image);
        }
    }

}
