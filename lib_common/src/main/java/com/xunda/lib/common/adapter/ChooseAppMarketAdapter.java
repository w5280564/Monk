package com.xunda.lib.common.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.AppMarketBean;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

/**
 * 选择应用市场弹出框适配器
 */
public class ChooseAppMarketAdapter extends BaseQuickAdapter<AppMarketBean, BaseViewHolder> {

    public ChooseAppMarketAdapter(List<AppMarketBean> data) {
        super(R.layout.item_market_dialog,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppMarketBean item) {

        ImageView iv_logo = helper.getView(R.id.iv_logo);
        TextView tv_name = helper.getView(R.id.tv_name);
        tv_name.setText(StringUtil.getStringValue(item.getMarketName()));
        iv_logo.setImageResource(item.getIconResource());
    }

}
