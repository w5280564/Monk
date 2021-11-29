package com.qingbo.monk.login.adapter;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HaveBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * ================================================
 *
 * @Description:收获列表
 * <p>
 * ================================================
 */
public class HaveAdapter extends BaseQuickAdapter<HaveBean, BaseViewHolder> {
    public HaveAdapter() {
        super(R.layout.item_have_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HaveBean item) {
        ImageView iv_img = helper.getView(R.id.iv_img);
        ImageView iv_check = helper.getView(R.id.iv_check);
        GlideUtils.loadImage(mContext,iv_img,item.getImage());
        helper.setText(R.id.tv_name, StringUtil.getStringValue(item.getName()));
        iv_check.setVisibility(item.isCheck()? View.VISIBLE:View.INVISIBLE);
    }
}
