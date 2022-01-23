package com.qingbo.monk.person.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.isNumber;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.WalletDetailed_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

/**
 * 交易记录item
 */
public class MyWallet_Detailed_Adapter extends BaseQuickAdapter<WalletDetailed_Bean, BaseViewHolder> {

    public MyWallet_Detailed_Adapter() {
        super(R.layout.wallet_detailed_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, WalletDetailed_Bean item) {
        helper.setText(R.id.content_Tv, item.getTrade_desc());
        helper.setText(R.id.time_Tv, item.getCreate_time());
        helper.setText(R.id.count_Tv, item.getMoney());


//        TextView count_Tv = helper.getView(R.id.count_Tv);
//        count_Tv.setText(item.getMoney() + "");
//        boolean number = isNumber(item.getMoney());
//        if (number) {
//            double v = Double.parseDouble(item.getMoney());
//            if (v > 0) {
//                count_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_1F8FE5));
//            } else if (v < 0) {
//                count_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
//            }
//        }

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


