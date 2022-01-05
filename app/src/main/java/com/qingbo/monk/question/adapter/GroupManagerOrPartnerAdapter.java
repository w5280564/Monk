package com.qingbo.monk.question.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.GroupMemberBean;
import com.xunda.lib.common.common.glide.GlideUtils;

/**
 * 管理员或合伙人
 */
public class GroupManagerOrPartnerAdapter extends BaseQuickAdapter<GroupMemberBean, BaseViewHolder> {

    public GroupManagerOrPartnerAdapter() {
        super(R.layout.item_choose_manager_partner);
    }





    @Override
    protected void convert(BaseViewHolder helper, GroupMemberBean item) {
        CheckBox cb_check = helper.getView(R.id.cb_check);
        ImageView iv_header = helper.getView(R.id.iv_header);
        helper.setText(R.id.tv_name,item.getNickname());
        GlideUtils.loadCircleImage(mContext, iv_header, item.getAvatar());
    }

}
