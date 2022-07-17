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
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
/**
 * 侧边栏 基金-基金持股 item
 */
public class FundCombination_Adapter extends BaseQuickAdapter<FundCombinationBean, BaseViewHolder> {
    public FundCombination_Adapter() {
        super(R.layout.fundcombination_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FundCombinationBean item) {
        TextView fundTime_Tv = helper.getView(R.id.fundTime_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        helper.setText(R.id.textView3,"持股数（万）");

        fundTime_Tv.setText(item.getNewsTitle());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        FundCombination_Shares_Adapter fundCombination_shares_adapter = new FundCombination_Shares_Adapter();
        mNineView.setAdapter(fundCombination_shares_adapter);
        fundCombination_shares_adapter.setNewData(item.getExtra().getItems());
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
