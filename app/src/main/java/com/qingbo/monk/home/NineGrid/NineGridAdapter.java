package com.qingbo.monk.home.NineGrid;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;

public class NineGridAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public NineGridAdapter() {
        super(R.layout.item_nine_grid);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        ImageView image = helper.getView(R.id.image);
        Glide.with(mContext).load(item).into(image);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }



}
