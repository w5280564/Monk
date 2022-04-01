package com.qingbo.monk.Slides.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.StockCombinationBean;
import com.qingbo.monk.bean.StockThighHK_Bean;
import com.xunda.lib.common.common.utils.ListUtils;

/**
 * 侧边栏 股票—港股-十大股东 item
 */
public class StockThighHK_Adapter extends BaseQuickAdapter<StockThighHK_Bean, BaseViewHolder> {
    public StockThighHK_Adapter() {
        super(R.layout.fundcombination_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StockThighHK_Bean item) {
        TextView fundTime_Tv = helper.getView(R.id.fundTime_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);

        fundTime_Tv.setText(item.getNewsDigest());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        StockThighHk_Item_Adapter stockThighHk_item_adapter = new StockThighHk_Item_Adapter();
        mNineView.setAdapter(stockThighHk_item_adapter);
        if (!ListUtils.isEmpty(item.getData().getItems())) {
            stockThighHk_item_adapter.setNewData(item.getData().getItems());
        }
    }


}
