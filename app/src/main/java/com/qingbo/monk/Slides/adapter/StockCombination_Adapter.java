package com.qingbo.monk.Slides.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.StockCombinationBean;

/**
 * 侧边栏 股票—十大股东/十大流通股东 item
 */
public class StockCombination_Adapter extends BaseQuickAdapter<StockCombinationBean, BaseViewHolder> {
    public StockCombination_Adapter() {
        super(R.layout.fundcombination_adapter);
    }

    public StockCombination_Adapter(String type) {
        super(R.layout.fundcombination_adapter);
        this.type = type;
    }

    private String type;


    @Override
    protected void convert(@NonNull BaseViewHolder helper, StockCombinationBean item) {
        TextView fundTime_Tv = helper.getView(R.id.fundTime_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);

        fundTime_Tv.setText(item.getNewsDigest());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        StockCombination_Shares_Adapter stockCombination_shares_adapter;
        if (TextUtils.equals(type, "3")) {
            stockCombination_shares_adapter = new StockCombination_Shares_Adapter(type);
        } else {
            stockCombination_shares_adapter = new StockCombination_Shares_Adapter();
        }
        mNineView.setAdapter(stockCombination_shares_adapter);
        stockCombination_shares_adapter.setNewData(item.getData().getItems());
    }


}
