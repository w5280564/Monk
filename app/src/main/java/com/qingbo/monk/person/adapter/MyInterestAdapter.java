package com.qingbo.monk.person.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.RoundImageView;

/**
 * ================================================
 *
 * @Description:我的问答群组列表
 * <p>
 * ================================================
 */
public class MyInterestAdapter extends BaseQuickAdapter<InterestBean, BaseViewHolder> {
    public MyInterestAdapter() {
        super(R.layout.item_interest_my);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InterestBean item) {
        helper.setText(R.id.tv_name_group,item.getGroupName());
        helper.setText(R.id.tv_des_group,StringUtil.isBlank(item.getGroupDes())?"暂无介绍...":item.getGroupDes());

        RoundImageView iv_header = helper.getView(R.id.iv_header);
        if (StringUtil.isBlank(item.getGroupImage())) {
            iv_header.setImageResource(R.mipmap.bg_create_group);
        }else{
            GlideUtils.loadCircleImage(mContext,iv_header,item.getGroupImage());
        }
    }

}
