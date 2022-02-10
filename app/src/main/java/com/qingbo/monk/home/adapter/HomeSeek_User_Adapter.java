package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.HomeSeekUser_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.List;

public class HomeSeek_User_Adapter extends BaseQuickAdapter<HomeSeekUser_Bean, BaseViewHolder> {
    String findStr;

    public HomeSeek_User_Adapter() {
        super(R.layout.homeseek_persn);
    }

    public void setFindStr(String findStr) {
        this.findStr = findStr;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeSeekUser_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        FlowLayout label_Flow = helper.getView(R.id.label_Flow);
        nickName_Tv.setFilters(new InputFilter[]{new ByteLengthFilter(14)});//昵称字数
        GlideUtils.loadCircleImage(mContext, head_Img, item.getAvatar(), R.mipmap.icon_logo);
//        nickName_Tv.setText(item.getNickname());
        SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), item.getNickname(), findStr);
        nickName_Tv.setText(searchChange);
        isFollow(item.getFollowStatus(), follow_Tv, send_Mes);
        String format = String.format("发表 %1$s条  关注 %2$s人", item.getArticleNum(), item.getFansNum());
        content_Tv.setText(format);
        labelAllFlow(label_Flow, mContext, item.getTagName());

        helper.addOnClickListener(R.id.send_Mes);
        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.head_Img);
    }


    /**
     * 标签 全部显示
     *
     * @param myFlow
     * @param mContext
     * @param tag
     */
    public void labelAllFlow(FlowLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 8, 0, 0);
            view.setLayoutParams(itemParams);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
            label_Name.setOnClickListener(v -> {
            });
        }
    }

    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }


}


