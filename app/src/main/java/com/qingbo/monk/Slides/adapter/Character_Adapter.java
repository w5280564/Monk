package com.qingbo.monk.Slides.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.Character_Bean;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

public class Character_Adapter extends BaseQuickAdapter<Character_Bean, BaseViewHolder> {
    public Character_Adapter() {
        super(R.layout.character_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Character_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        TextView company_Tv = helper.getView(R.id.company_Tv);

        GlideUtils.loadCircleImage(mContext,head_Img,item.getAvatar(),R.mipmap.icon_logo);
        nickName_Tv.setText(item.getNickname());
        company_Tv.setText(item.getCompanyName());
        labelFlow(lable_Lin, mContext, item.getTag_name());

        helper.addOnClickListener(R.id.head_Img);
    }
    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public void labelFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
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

}
