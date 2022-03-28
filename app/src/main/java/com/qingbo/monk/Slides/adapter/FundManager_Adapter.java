package com.qingbo.monk.Slides.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.baseview.ExpandTextView;
import com.qingbo.monk.bean.FundCombinationBean;
import com.qingbo.monk.bean.FundManagerBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import me.jessyan.autosize.utils.ScreenUtils;

public class FundManager_Adapter extends BaseQuickAdapter<FundManagerBean, BaseViewHolder> {
    public FundManager_Adapter() {
        super(R.layout.fundmanager_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FundManagerBean item) {
        ConstraintLayout manager_Con = helper.getView(R.id.manager_Con);
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        FlowLayout lable_flow = helper.getView(R.id.lable_flow);
        TextView allTime_Tv = helper.getView(R.id.allTime_Tv);
        TextView startTime_Tv = helper.getView(R.id.startTime_Tv);
        ExpandTextView content_Tv = helper.getView(R.id.content_Tv);


        GlideUtils.loadCircleImage(mContext, head_Img, item.getAvatar(), R.mipmap.icon_logo);
        nickName_Tv.setText(item.getNewsTitle());
        allTime_Tv.setText("累计任职时间：" + item.getDays());
        startTime_Tv.setText("任职起始日期：" + item.getNewsPosttime());


        int width = com.qingbo.monk.base.baseview.ScreenUtils.getScreenWidth(mContext) - com.qingbo.monk.base.baseview.ScreenUtils.dip2px(mContext, 40);
        content_Tv.initWidth(width);
        content_Tv.setCloseText("基金经理简介：\n" + item.getNewsContent());

        labelFlow(lable_flow, mContext, item.getTagName());

    }


    public void labelFlow(FlowLayout myFlow, Context mContext, String tag) {
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
