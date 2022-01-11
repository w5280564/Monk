package com.qingbo.monk.person.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.RoundImageView;

/**
 * ================================================
 *
 * @Description:我的社群列表item
 * <p>
 * ================================================
 */
public class MyGroupAdapter extends BaseQuickAdapter<MyCardGroup_Bean, BaseViewHolder> {
    public MyGroupAdapter() {
        super(R.layout.item_group_my);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyCardGroup_Bean item) {
        helper.setText(R.id.tv_name_group,item.getShequnName());
        helper.setText(R.id.tv_des_group,StringUtil.isBlank(item.getShequnDes())?"暂无群简介...":item.getShequnDes());

        RoundImageView iv_header = helper.getView(R.id.iv_header);
        if (StringUtil.isBlank(item.getShequnImage())) {
            iv_header.setImageResource(R.mipmap.bg_create_group);
        }else{
            GlideUtils.loadCircleImage(mContext,iv_header,item.getShequnImage());
        }
    }

}
