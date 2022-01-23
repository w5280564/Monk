package com.qingbo.monk.question.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.RoundImageView;

/**
 * ================================================
 *
 * @Description:我的问答群组列表
 * <p>
 * ================================================
 */
public class QuestionGroupAdapterMy extends BaseQuickAdapter<MyGroupBean, BaseViewHolder> {
    public QuestionGroupAdapterMy() {
        super(R.layout.mygroup_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyGroupBean item) {
        helper.setText(R.id.tv_name_group,item.getShequnName());
        helper.setText(R.id.tv_des_group,StringUtil.isBlank(item.getShequnDes())?"暂无群简介...":item.getShequnDes());

        RoundImageView iv_header = helper.getView(R.id.iv_header);
        if (StringUtil.isBlank(item.getShequnImage())) {
            iv_header.setImageResource(R.mipmap.bg_create_group);
        }else{
            GlideUtils.loadCircleImage(mContext,iv_header,item.getShequnImage());
        }

        ImageView groupUser_Img = helper.getView(R.id.groupUser_Img);
        String id = PrefUtil.getUser().getId();
        if (TextUtils.equals(id,item.getUserId())){
            groupUser_Img.setVisibility(View.VISIBLE);
        }else {
            groupUser_Img.setVisibility(View.GONE);
        }
    }

}
