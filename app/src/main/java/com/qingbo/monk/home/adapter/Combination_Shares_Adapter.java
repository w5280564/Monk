package com.qingbo.monk.home.adapter;

import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.xunda.lib.common.common.glide.GlideUtils;

public class Combination_Shares_Adapter extends BaseQuickAdapter<HomeCombinationBean.DetailDTO, BaseViewHolder> {
    public Combination_Shares_Adapter() {
        super(R.layout.item_shares);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeCombinationBean.DetailDTO item) {
        TextView shareName_Tv = helper.getView(R.id.shareName_Tv);
        TextView proportion_Tv = helper.getView(R.id.proportion_Tv);

        String format = String.format("%1$s%2$s", item.getNumber(), item.getName());
        shareName_Tv.setText(format);
//        String format1 = String.format("%1$s手%2$s", item.getNum(), item.getPosition());
        String s = item.getNum() + "手";
        String s1 = "（" + item.getPosition() + "%）";
        String s2 = s + s1;
        int length = s2.length();
        int startLength = length - s1.length();
        setName(s2,length,startLength,length,proportion_Tv);

    }

    /**
     * @param name          要显示的数据
     * @param nameLength    要改颜色的字体长度
     * @param startLength   改色起始位置
     * @param endLength     改色结束位置
     * @param viewName
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }

}
