package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeSeekFund_Bean;
import com.qingbo.monk.bean.HomeSeekPerson_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

public class HomeSeek_Fund_Adapter extends BaseQuickAdapter<HomeSeekFund_Bean, BaseViewHolder> {

    public HomeSeek_Fund_Adapter() {
        super(R.layout.homeseek_fund_label);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeSeekFund_Bean item) {
        helper.setText(R.id.fundName_Tv,item.getName());
        helper.setText(R.id.fundCode_Tv,item.getNumber());
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


