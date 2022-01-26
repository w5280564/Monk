package com.qingbo.monk.home.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.InterestBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;
import java.util.List;

public class HomeInterest_Adapter extends BaseQuickAdapter<InterestBean, BaseViewHolder> {
    public HomeInterest_Adapter() {
        super(R.layout.interest_lable);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InterestBean item) {
        ImageView head_Tv = helper.getView(R.id.head_Tv);
        TextView shares_Tv = helper.getView(R.id.shares_Tv);
        TextView add_Tv = helper.getView(R.id.add_Tv);
        TextView join_Tv = helper.getView(R.id.join_Tv);
        String headUrl = item.getGroupImage();
        GlideUtils.loadCircleImage(mContext, head_Tv, headUrl, R.mipmap.icon_logo);
        shares_Tv.setText(item.getGroupName());
        add_Tv.setText(item.getJoinNum() + "加入");
        String state = item.getJoinStatus();
        joinState(state, join_Tv);

        helper.addOnClickListener(R.id.join_Tv);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }



    /**
     * @param state  1是已加入  其他都是未加入
     * @param joinTv
     */
    public void joinState(String state, TextView joinTv) {
        if (!TextUtils.isEmpty(state)) {
            int indexState = Integer.parseInt(state);
            if (indexState == 1) { //1已加入 其他都是未加入
                joinTv.setText("已加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            } else {
                joinTv.setText("加入");
                joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
                changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
            }
        }
    }

}
