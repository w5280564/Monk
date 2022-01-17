package com.qingbo.monk.person.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HaveBean;
import com.qingbo.monk.person.activity.Edit_Change_Harvest;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

/**
 * ================================================
 *
 * @Description:收获列表
 * <p>
 * ================================================
 */
public class HarvestAdapter extends BaseQuickAdapter<HaveBean, BaseViewHolder> {
    List<String>  mChooseGetResourceList;
    public HarvestAdapter(List<String> mChooseGetResourceList) {
        super(R.layout.item_have_list);
        this.mChooseGetResourceList = mChooseGetResourceList;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HaveBean item) {
        ImageView iv_img = helper.getView(R.id.iv_img);
        ImageView iv_check = helper.getView(R.id.iv_check);
        GlideUtils.loadImage(mContext,iv_img,item.getImage());
        helper.setText(R.id.tv_name, StringUtil.getStringValue(item.getName()));
        if (mChooseGetResourceList.contains(item.getName())){
            item.setCheck(true);
        }else {
            item.setCheck(false);
        }
        iv_check.setVisibility(item.isCheck()? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
