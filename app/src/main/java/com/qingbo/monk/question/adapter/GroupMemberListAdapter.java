package com.qingbo.monk.question.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;

/**
 * ================================================
 *
 * 群成员列表
 * <p>
 * ================================================
 */
public class GroupMemberListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GroupMemberListAdapter() {
        super(R.layout.item_group_member_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.iv_more);
//        TextView tv_name_question = helper.getView(R.id.tv_name_question);
//        TextView tv_name_fee = helper.getView(R.id.tv_name_fee);
//        if ("0".equals(item.getType())) {
//            tv_name_fee.setText("限时免费");
//        }else{
//            tv_name_fee.setText("加入");
//        }
//        tv_name_question.setText(StringUtil.getStringValue(item.getShequnName()));
//        DiscussionAvatarView mAvatarViewList = helper.getView(R.id.daview);
//        if (!ListUtils.isEmpty(item.getDetail())) {
//            ArrayList<String> mList = new ArrayList<>();
//            for (GroupBean.DetailDTO detailObj :item.getDetail()) {
//                mList.add(detailObj.getAvatar());
//            }
//            mAvatarViewList.initDatas(mList);
//        }
    }

}
