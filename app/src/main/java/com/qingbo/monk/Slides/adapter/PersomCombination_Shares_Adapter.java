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
import com.qingbo.monk.bean.CharacterDetail_Bean;
import com.qingbo.monk.bean.StockCombinationBean;

public class PersomCombination_Shares_Adapter extends BaseQuickAdapter<CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1, BaseViewHolder> {
    public PersomCombination_Shares_Adapter() {
        super(R.layout.item_fundshares);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1 item) {
        TextView shareName_Tv = helper.getView(R.id.shareName_Tv);
        TextView cgs_Tv = helper.getView(R.id.cgs_Tv);
        TextView jzbl_Tv = helper.getView(R.id.jzbl_Tv);
        TextView change_Tv = helper.getView(R.id.change_Tv);


        shareName_Tv.setText(item.getName());
        if (!TextUtils.isEmpty(item.getHoldingNum())) {
//            Double i = Double.parseDouble(item.getHoldingNum()) / 10000;
            String holdingNum = subZeroAndDot(item.getHoldingNum());
            cgs_Tv.setText(holdingNum);
        }
        if (item.getPosition().contains("%")) {
            jzbl_Tv.setText(item.getPosition());
        } else {
            jzbl_Tv.setText(item.getPosition() + "%");
        }

        if (TextUtils.isEmpty(item.getChange())) {
            change_Tv.setText("");
        } else {
            change_Tv.setText(item.getChange() + "");
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

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


}
