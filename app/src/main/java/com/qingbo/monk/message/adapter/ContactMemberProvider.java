package com.qingbo.monk.message.adapter;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerBigAdapter;
import com.xunda.lib.common.common.glide.GlideUtils;


public class ContactMemberProvider extends BaseItemProvider<FriendContactBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return GroupManagerOrPartnerBigAdapter.TYPE_MEMBER;
    }

    @Override
    public int layout() {
        return R.layout.item_contact_list;
    }

    @Override
    public void convert(BaseViewHolder helper, FriendContactBean item, int position) {
        ImageView avatar = helper.getView(R.id.avatar);
        helper.setText(R.id.name,item.getNickname());
        GlideUtils.loadCircleImage(mContext, avatar, item.getAvatar());
    }

}
