package com.qingbo.monk.Slides.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeInsiderBean;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 基金/股票-公告
 */
public class FundNotice_Insider_Adapter extends BaseQuickAdapter<HomeInsiderBean, BaseViewHolder> {
    public FundNotice_Insider_Adapter() {
        super(R.layout.insider_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeInsiderBean item) {

        ConstraintLayout Insider_Con = helper.getView(R.id.Insider_Con);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        StringUtil.changeShapColor(Insider_Con, ContextCompat.getColor(mContext, R.color.text_color_fbfbfb));
        title_Tv.setText(item.getNewsTitle());
        content_Tv.setText(item.getNews_content());
        time_Tv.setText(item.getNewsPosttime());
    }
    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
