package com.qingbo.monk.question.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.qingbo.monk.bean.GroupMemberBean;
import java.util.List;


/**
 * 群通讯录
 */
public class GroupManagerOrPartnerBigAdapter extends MultipleItemRvAdapter<GroupMemberBean, BaseViewHolder> {
    public static final int TYPE_HEADER = 101;
    public static final int TYPE_MEMBER = 201;

    public GroupManagerOrPartnerBigAdapter(List<GroupMemberBean> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(GroupMemberBean entity) {
        if (entity.getItemType() == 1) {// 1是header 0是member
            return TYPE_HEADER;
        } else{
            return TYPE_MEMBER;
        }
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new HeaderProvider());
        mProviderDelegate.registerProvider(new MemberProvider());
    }


}
