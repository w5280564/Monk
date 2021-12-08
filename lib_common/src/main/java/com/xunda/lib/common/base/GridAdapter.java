package com.xunda.lib.common.base;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 筛选适配器
 */
public class GridAdapter extends BaseQuickAdapter<NameIdBean, BaseViewHolder> {
    public GridAdapter() {
        super(R.layout.item_screen_grid);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NameIdBean item) {
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setText(StringUtil.getStringValue(item.getName()));
        tv_name.setTextColor(ContextCompat.getColor(mContext,item.isSelect()?R.color.text_color_444444:R.color.text_color_6f6f6f));
        tv_name.setBackgroundResource(item.isSelect()?R.drawable.shape_screen_bag_select:R.drawable.shape_screen_bag_normal);
    }
}
