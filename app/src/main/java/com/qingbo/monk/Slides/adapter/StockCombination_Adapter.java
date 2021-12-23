package com.qingbo.monk.Slides.adapter;

import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.FundCombinationBean;
import com.qingbo.monk.bean.StockCombinationBean;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;

/**
 * 侧边栏 个股-股票持股 item
 */
public class StockCombination_Adapter extends BaseQuickAdapter<StockCombinationBean, BaseViewHolder> {
    public StockCombination_Adapter() {
        super(R.layout.fundcombination_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StockCombinationBean item) {
        TextView fundTime_Tv = helper.getView(R.id.fundTime_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);

        fundTime_Tv.setText(item.getNewsDigest());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        StockCombination_Shares_Adapter stockCombination_shares_adapter = new StockCombination_Shares_Adapter();
        mNineView.setAdapter(stockCombination_shares_adapter);
        stockCombination_shares_adapter.setNewData(item.getData().getItems());
    }


    /**
     * 获取分割线
     *
     * @param drawableId 分割线id
     * @return
     */
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        CustomDecoration itemDecoration = new CustomDecoration(mContext, LinearLayoutManager.VERTICAL, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, drawableId));
        return itemDecoration;
    }


}
