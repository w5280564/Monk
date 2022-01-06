package com.qingbo.monk.message.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.bean.GroupMemberBean;
import java.util.List;

/**
 * ================================================
 *
 * @Description:通讯录好友列表
 * <p>
 * ================================================
 */
public class ContactListAdapter extends MultipleItemRvAdapter<FriendContactBean, BaseViewHolder> {
    public static final int TYPE_HEADER = 101;
    public static final int TYPE_MEMBER = 201;

    public ContactListAdapter(List<FriendContactBean> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(FriendContactBean entity) {
        if (entity.getItemType() == 1) {// 1是header 0是member
            return TYPE_HEADER;
        } else{
            return TYPE_MEMBER;
        }
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new ContactHeaderProvider());
        mProviderDelegate.registerProvider(new ContactMemberProvider());
    }


}
