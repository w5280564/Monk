package com.qingbo.monk.message.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;

/**
 * ================================================
 *
 * @Description:通讯录好友列表
 * <p>
 * ================================================
 */
public class ContactListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ContactListAdapter() {
        super(R.layout.item_contact_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }

}
