package com.qingbo.monk.message.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ServiceChatBean;
import com.qingbo.monk.bean.ServiceChatIndexBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.List;

/**
 * 发送
 */
public class SenderViewProvider extends BaseItemProvider<ServiceChatIndexBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return ChatAdapter.TYPE_CHAT_SENDER;
    }

    @Override
    public int layout() {
        return R.layout.item_chat_sender;
    }

    @Override
    public void convert(BaseViewHolder helper, ServiceChatIndexBean item, int position) {
        List<ServiceChatBean> mList =  item.getList();
        if(!ListUtils.isEmpty(mList)){
            helper.setText(R.id.tv_content, StringUtil.isBlank(mList.get(0).getContent()) ? "" : mList.get(0).getContent());
            ImageView iv_head = helper.getView(R.id.iv_head);
            GlideUtils.loadCircleImage(mContext,iv_head, SharePref.user().getUserHead());
        }
    }

}
