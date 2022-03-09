package com.qingbo.monk.Slides.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.xunda.lib.common.common.glide.RoundedCornersTransform;
import com.xunda.lib.common.common.utils.DisplayUtil;

/**
 * ================================================
 *
 * @Description:问答我的社群展开 item
 * <p>
 * ================================================
 */
public class Question_MyGroupAdapter extends BaseQuickAdapter<MyCardGroup_Bean, BaseViewHolder> {

    public Question_MyGroupAdapter() {
        super(R.layout.question_mygroup_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyCardGroup_Bean item) {

        ConstraintLayout haveData_Con = helper.getView(R.id.haveData_Con);
        ConstraintLayout noData_Con = helper.getView(R.id.noData_Con);
        String itemType = item.getItemType();
        if (TextUtils.equals(itemType,"2")){
            haveData_Con.setVisibility(View.GONE);
            noData_Con.setVisibility(View.VISIBLE);
        }else {
            haveData_Con.setVisibility(View.VISIBLE);
            noData_Con.setVisibility(View.GONE);
        }

        ImageView iv_img_top = helper.getView(R.id.iv_img_top);
        helper.setText(R.id.tv_group_name, item.getShequnName());
        helper.setText(R.id.tv_name, item.getNickname());
        RoundedCornersTransform transform = new RoundedCornersTransform(mContext, DisplayUtil.dip2px(mContext, 8));
        transform.setNeedCorner(true, true, false, false);
        Glide.with(mContext).load(item.getShequnImage()).placeholder(R.mipmap.bg).transforms(transform).into(iv_img_top);

    }



}
