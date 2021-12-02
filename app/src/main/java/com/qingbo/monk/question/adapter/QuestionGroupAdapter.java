package com.qingbo.monk.question.adapter;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;

/**
 * ================================================
 *
 * @Description:问答群组列表
 * <p>
 * ================================================
 */
public class QuestionGroupAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public QuestionGroupAdapter() {
        super(R.layout.item_group_shequn);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        DiscussionAvatarView mAvatarViewList = helper.getView(R.id.daview);
        ArrayList<String> mList = new ArrayList<>();
        String headerUrl = SharePref.user().getUserHead();
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mList.add(headerUrl);
        mAvatarViewList.initDatas(mList);
    }

}
