package com.qingbo.monk.Slides.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.isNumber;

import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.FundCombinationBean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.regex.Pattern;

public class FundCombination_Shares_Adapter extends BaseQuickAdapter<FundCombinationBean.ExtraDTO.ItemsDTO, BaseViewHolder> {
    public FundCombination_Shares_Adapter() {
        super(R.layout.item_fundshares);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, FundCombinationBean.ExtraDTO.ItemsDTO item) {
        TextView shareName_Tv = helper.getView(R.id.shareName_Tv);
        TextView cgs_Tv = helper.getView(R.id.cgs_Tv);
        TextView jzbl_Tv = helper.getView(R.id.jzbl_Tv);
        TextView change_Tv = helper.getView(R.id.change_Tv);

        shareName_Tv.setText(item.getName());
        cgs_Tv.setText(item.getCgs());
        jzbl_Tv.setText(item.getJzbl());
        change_Tv.setText(item.getChange());

        boolean number = isNumber(item.getChange());
        if (number) {
            double v = Double.parseDouble(item.getChange());
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
