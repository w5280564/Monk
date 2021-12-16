package com.qingbo.monk.Slides.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.FindBean;

public class Find_Adapter extends BaseQuickAdapter<FindBean, BaseViewHolder> {
    public Find_Adapter() {
        super(R.layout.find_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FindBean item) {

//        RoundIma round_Img = helper.getView(R.id.round_Img);
//        GlideUtils.loadImage(mContext,round_Img,item.getAvatar());
//        GlideUtils.loadRoundImage(mContext,round_Img,item.getAvatar(),R.mipmap.icon_logo,10,true,false,false,false);

//        TextView title_Tv = helper.getView(R.id.title_Tv);
//        TextView content_Tv = helper.getView(R.id.content_Tv);
//        TextView time_Tv = helper.getView(R.id.time_Tv);
//        StringUtil.changeShapColor(Insider_Con, ContextCompat.getColor(mContext, R.color.text_color_fbfbfb));
//        title_Tv.setText(item.getNewsAuthor());
//        content_Tv.setText(item.getNewsTitle());
//        time_Tv.setText(item.getNewsPosttime());

    }
    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
