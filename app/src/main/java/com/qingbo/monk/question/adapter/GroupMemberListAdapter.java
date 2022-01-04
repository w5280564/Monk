package com.qingbo.monk.question.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.GroupMemberBean;
import com.xunda.lib.common.common.glide.GlideUtils;

/**
 * ================================================
 *
 * 群成员列表
 * <p>
 * ================================================
 */
public class GroupMemberListAdapter extends BaseQuickAdapter<GroupMemberBean, BaseViewHolder> {
    public GroupMemberListAdapter() {
        super(R.layout.item_group_member_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupMemberBean item) {
        helper.addOnClickListener(R.id.iv_more);
        ImageView iv_more = helper.getView(R.id.iv_more);
        TextView tv_role = helper.getView(R.id.tv_role);
        ImageView iv_header = helper.getView(R.id.iv_header);
        helper.setText(R.id.tv_name,item.getNickname());
        GlideUtils.loadCircleImage(mContext, iv_header, item.getAvatar());

        String role = item.getRole();
        if ("1".equals(role)) {//1管理员2合伙人0一般用户3群主
            iv_more.setVisibility(View.VISIBLE);
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("管理员");
        }else if("2".equals(role)) {
            iv_more.setVisibility(View.VISIBLE);
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("合伙人");
        }else if("3".equals(role)) {
            iv_more.setVisibility(View.GONE);
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("群主");
        }else{
            iv_more.setVisibility(View.VISIBLE);
            tv_role.setVisibility(View.GONE);
        }
    }

}
