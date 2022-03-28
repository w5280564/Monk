package com.qingbo.monk.home.adapter;

import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeSeekFund_Bean;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

public class HomeSeek_Fund_Adapter extends BaseQuickAdapter<HomeSeekFund_Bean, BaseViewHolder> {
    String findStr;

    public HomeSeek_Fund_Adapter() {
        super(R.layout.homeseek_fund_label);
    }

    public void setFindStr(String findStr) {
        this.findStr = findStr;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeSeekFund_Bean item) {
        TextView fundName_Tv = helper.getView(R.id.fundName_Tv);
        if (!TextUtils.isEmpty(item.getName())){
//            fundName_Tv.setText(item.getName());
            SpannableString searchChange = StringUtil.findSearchChange(ContextCompat.getColor(mContext, R.color.text_color_ff5b29), item.getName(), findStr);
            fundName_Tv.setText(searchChange);
        }
        TextView fundCode_Tv = helper.getView(R.id.fundCode_Tv);
//        if (!TextUtils.isEmpty(item.getNumber())) {
            fundCode_Tv.setText(item.getNumber());
//        }

//        helper.setText(R.id.fundName_Tv,item.getName());
//        helper.setText(R.id.fundCode_Tv,item.getNumber());
    }




    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }


}


