package com.qingbo.monk.Slides.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.isNumber;

import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.StockCombinationBean;
import com.qingbo.monk.bean.StockThighHK_Bean;

public class StockThighHk_Item_Adapter extends BaseQuickAdapter<StockThighHK_Bean.DataDTO.ItemsDTO, BaseViewHolder> {
    public StockThighHk_Item_Adapter() {
        super(R.layout.item_fundshares);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, StockThighHK_Bean.DataDTO.ItemsDTO item) {
        TextView shareName_Tv = helper.getView(R.id.shareName_Tv);
        TextView cgs_Tv = helper.getView(R.id.cgs_Tv);
        TextView jzbl_Tv = helper.getView(R.id.jzbl_Tv);
        TextView change_Tv = helper.getView(R.id.change_Tv);


        shareName_Tv.setText(item.getGdmc());
        cgs_Tv.setText(item.getCgsl() + "");
        jzbl_Tv.setText(item.getCgbl() + "%");


        if (TextUtils.isEmpty(item.getCgblbd())) {
            change_Tv.setText("");
        } else {
            change_Tv.setText(item.getCgblbd() + "%");
            boolean number = isNumber(item.getCgblbd());
            if (number) {
                double v = Double.parseDouble(item.getCgblbd());
                if (v == 0) {
                    change_Tv.setText("不变");
                } else if (v > 0) {
                    change_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_FC0000));
                } else if (v < 0) {
                    change_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_76AD45));
                }
            }
        }


    }


}
