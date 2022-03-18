package com.qingbo.monk.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

public class HomeFollow_Adapter extends BaseQuickAdapter<ArticleLikedBean, BaseViewHolder> {
    public HomeFollow_Adapter() {
        super(R.layout.interest_lable);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleLikedBean item) {
        ImageView head_Tv = helper.getView(R.id.head_Tv);
        TextView shares_Tv = helper.getView(R.id.shares_Tv);
        TextView join_Tv = helper.getView(R.id.join_Tv);
        TextView update_Tv = helper.getView(R.id.update_Tv);
        String headUrl = item.getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Tv, headUrl, R.mipmap.icon_logo);
        shares_Tv.setText(item.getNickname());
        isFollow(item.getFollowStatus(), join_Tv);
        helper.addOnClickListener(R.id.join_Tv);
        update_Tv.setVisibility(View.GONE);
        if (!TextUtils.equals(item.getNew_article_num(),"0")){
            update_Tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }


    /**
     * /**
     *
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     */
    public void isFollow(int follow_status, TextView follow_Tv) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        }
    }

}
