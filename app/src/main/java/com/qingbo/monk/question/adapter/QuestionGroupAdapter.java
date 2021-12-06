package com.qingbo.monk.question.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.SheQunBean;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;
import java.util.ArrayList;

/**
 * ================================================
 *
 * @Description:问答群组列表
 * <p>
 * ================================================
 */
public class QuestionGroupAdapter extends BaseQuickAdapter<SheQunBean, BaseViewHolder> {
    public QuestionGroupAdapter() {
        super(R.layout.item_group_shequn);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SheQunBean item) {
        TextView tv_name_question = helper.getView(R.id.tv_name_question);
        TextView tv_name_fee = helper.getView(R.id.tv_name_fee);
        if ("0".equals(item.getType())) {
            tv_name_fee.setText("限时免费");
        }else{
            tv_name_fee.setText("加入");
        }
        tv_name_question.setText(StringUtil.getStringValue(item.getShequnName()));
        DiscussionAvatarView mAvatarViewList = helper.getView(R.id.daview);
        if (!ListUtils.isEmpty(item.getDetail())) {
            ArrayList<String> mList = new ArrayList<>();
            for (SheQunBean.DetailDTO detailObj :item.getDetail()) {
                mList.add(detailObj.getAvatar());
            }
            mAvatarViewList.initDatas(mList);
        }
    }

}
