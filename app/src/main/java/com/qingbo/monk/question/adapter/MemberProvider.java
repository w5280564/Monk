package com.qingbo.monk.question.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.GroupMemberBean;
import com.xunda.lib.common.common.glide.GlideUtils;


public class MemberProvider extends BaseItemProvider<GroupMemberBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return GroupManagerOrPartnerBigAdapter.TYPE_MEMBER;
    }

    @Override
    public int layout() {
        return R.layout.item_choose_manager_partner;
    }

    @Override
    public void convert(BaseViewHolder helper, GroupMemberBean item, int position) {
        CheckBox cb_check = helper.getView(R.id.cb_check);
        cb_check.setChecked(item.isCheck());
        ImageView iv_header = helper.getView(R.id.iv_header);
        helper.setText(R.id.tv_name,item.getNickname());
        GlideUtils.loadCircleImage(mContext, iv_header, item.getAvatar());
    }

}
