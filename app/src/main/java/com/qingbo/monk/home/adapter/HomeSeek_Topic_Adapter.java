package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeSeekPerson_Bean;
import com.qingbo.monk.bean.HomeSeekTopic_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class HomeSeek_Topic_Adapter extends BaseQuickAdapter<HomeSeekTopic_Bean, BaseViewHolder> {

    public HomeSeek_Topic_Adapter() {
        super(R.layout.homeseek_topic);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeSeekTopic_Bean item) {
        helper.setText(R.id.artName_tv,item.getNickname());
        helper.setText(R.id.artContent_Tv,item.getContent());
        ImageView art_Img =helper.getView(R.id.art_Img);
        if (!TextUtils.isEmpty(item.getImages())) {
            List<String> strings = Arrays.asList(item.getImages().split(","));
            GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0), 9);
        } else {
            art_Img.setImageResource(R.mipmap.img_pic_none_square);
        }
    }


    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }


}


