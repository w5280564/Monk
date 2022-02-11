package com.qingbo.monk.question.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.WaitGroupAnswerBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 社群话题等你回答
 */
public class GroupDetailWaitAnswerAdapter extends BaseQuickAdapter<WaitGroupAnswerBean, BaseViewHolder> {

    public GroupDetailWaitAnswerAdapter() {
        super(R.layout.item_group_detail_wait_answer);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, WaitGroupAnswerBean item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView tv_answer = helper.getView(R.id.tv_answer);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        ImageView question_Img = helper.getView(R.id.question_Img);

        GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar());
        group_Name.setText(item.getNickname());

        if (!StringUtil.isBlank(item.getContent())) {
            content_Tv.setVisibility(View.VISIBLE);
            content_Tv.setText(item.getContent());
        }else{
            content_Tv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }
        GlideUtils.loadImage(mContext,question_Img,item.getImages());

        tv_answer.setVisibility(View.VISIBLE);
        helper.addOnClickListener(R.id.tv_answer);
        helper.addOnClickListener(R.id.question_Img);
    }



}


