package com.qingbo.monk.message.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;

/**
 * ================================================
 *
 * @Description:消息列表
 * <p>
 * ================================================
 */
public class MessageListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MessageListAdapter() {
        super(R.layout.item_message_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }

}
