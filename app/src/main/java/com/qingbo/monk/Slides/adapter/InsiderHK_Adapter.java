package com.qingbo.monk.Slides.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.qingbo.monk.bean.HomeInsiderHKBean;
import com.xunda.lib.common.common.utils.StringUtil;

public class InsiderHK_Adapter extends BaseQuickAdapter<HomeInsiderHKBean, BaseViewHolder> {
    public InsiderHK_Adapter() {
        super(R.layout.insider_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeInsiderHKBean item) {

        ConstraintLayout Insider_Con = helper.getView(R.id.Insider_Con);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        StringUtil.changeShapColor(Insider_Con, ContextCompat.getColor(mContext, R.color.text_color_fbfbfb));
        nickName_Tv.setVisibility(View.VISIBLE);
        title_Tv.setText(item.getItem().getName());
        nickName_Tv.setText(item.getItem().getShareholderName());
        content_Tv.setText(item.getItem().getReason());
        time_Tv.setText(item.getItem().getRelevantEventDate());


    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
