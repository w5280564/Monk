package com.qingbo.monk.question.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.AskQuestionBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 社群去提问列表
 */
public class GroupDetailListAdapterWhat extends BaseQuickAdapter<AskQuestionBean, BaseViewHolder> {

    public GroupDetailListAdapterWhat() {
        super(R.layout.item_group_detail_what);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, AskQuestionBean item) {
        ImageView iv_header = helper.getView(R.id.iv_header);
        TextView tv_submit = helper.getView(R.id.tv_submit);
        TextView tv_des = helper.getView(R.id.tv_des);
        helper.setText(R.id.tv_name, StringUtil.getStringValue(item.getNickname()));
        helper.setText(R.id.tv_tag,StringUtil.getStringValue(item.getRoleName()));

        GlideUtils.loadCircleImage(mContext, iv_header, item.getAvatar());

        if ("0".equals(item.getIsQuestion())) {//0-不可提问 1-可提问
            tv_submit.setText("不能向自己提问");
            tv_submit.setBackgroundResource(R.drawable.shape_bag_what_gray);
        }else{
            tv_submit.setText("向ta提问");
            tv_submit.setBackgroundResource(R.drawable.shape_bag_what_yellow);
            helper.addOnClickListener(R.id.tv_submit);
        }

        if ("0".equals(item.getNum())) {
            tv_des.setVisibility(View.GONE);
        }else{
            tv_des.setVisibility(View.VISIBLE);
            tv_des.setText(String.format("ta回答了%s个提问",item.getNum()));
        }
    }

}


