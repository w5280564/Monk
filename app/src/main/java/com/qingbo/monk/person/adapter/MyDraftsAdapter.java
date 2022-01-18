package com.qingbo.monk.person.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.RoundImageView;

/**
 * ================================================
 *
 * @Description:我的草稿箱
 * <p>
 * ================================================
 */
public class MyDraftsAdapter extends BaseQuickAdapter<OwnPublishBean, BaseViewHolder> {
    public MyDraftsAdapter() {
        super(R.layout.item_drafts);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OwnPublishBean item) {
        helper.setText(R.id.title_Tv,item.getTitle());
        helper.setText(R.id.content_Tv,item.getContent());
        helper.setText(R.id.time_Tv,item.getCreateTime());

        helper.addOnClickListener(R.id.draft_Fa);
        helper.addOnClickListener(R.id.draft_Del);
    }

}
