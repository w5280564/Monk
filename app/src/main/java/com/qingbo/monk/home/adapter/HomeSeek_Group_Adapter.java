package com.qingbo.monk.home.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.content.Context;
import android.text.InputFilter;
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
import com.qingbo.monk.bean.HomeSeekGroup_Bean;
import com.qingbo.monk.bean.HomeSeekUser_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

public class HomeSeek_Group_Adapter extends BaseQuickAdapter<HomeSeekGroup_Bean, BaseViewHolder> {

    public HomeSeek_Group_Adapter() {
        super(R.layout.homeseek_group);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeSeekGroup_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView followCount_Tv = helper.getView(R.id.followCount_Tv);
        TextView join_Tv = helper.getView(R.id.join_Tv);
        GlideUtils.loadCircleImage(mContext, head_Img, item.getShequnImage(), R.mipmap.icon_logo);
        nickName_Tv.setText(item.getShequnName());
        String format = String.format("关注 %1$s", item.getJoinNum());
        followCount_Tv.setText(format);
        content_Tv.setText(item.getShequnDes());
        String state = item.getJoinStatus();
        isJoin(state, join_Tv);

        helper.addOnClickListener(R.id.join_Tv);
    }


    /**
     *
     * @param state 1是已加入  其他都是未加入
     * @param join_Tv
     */
    public void isJoin(String state, TextView join_Tv) {
        if (TextUtils.equals(state,"1")) {
            join_Tv.setText("已加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            join_Tv.setText("加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
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


