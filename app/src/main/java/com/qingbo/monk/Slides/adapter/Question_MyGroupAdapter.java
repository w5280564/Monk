package com.qingbo.monk.Slides.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyCardGroup_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.RoundImageView;

/**
 * ================================================
 *
 * @Description:问答我的社群展开 item
 * <p>
 * ================================================
 */
public class Question_MyGroupAdapter extends BaseQuickAdapter<MyCardGroup_Bean, BaseViewHolder> {
    boolean isMe;
    public Question_MyGroupAdapter(boolean isMe) {
        super(R.layout.question_mygroup_item);
        this.isMe = isMe;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyCardGroup_Bean item) {
        helper.setText(R.id.tv_name_group,item.getShequnName());
        helper.setText(R.id.tv_des_group,StringUtil.isBlank(item.getShequnDes())?"暂无群简介...":item.getShequnDes());
        TextView tv_join = helper.getView(R.id.tv_join);

        RoundImageView iv_header = helper.getView(R.id.iv_header);
        if (StringUtil.isBlank(item.getShequnImage())) {
            iv_header.setImageResource(R.mipmap.bg_create_group);
        }else{
            GlideUtils.loadCircleImage(mContext,iv_header,item.getShequnImage());
        }



        if (isMe){
            ImageView groupUser_Img = helper.getView(R.id.groupUser_Img);
            String id = PrefUtil.getUser().getId();
//            if (TextUtils.equals(id,item.getUserId())){
//                groupUser_Img.setVisibility(View.VISIBLE);
//            }else {
//                groupUser_Img.setVisibility(View.GONE);
//            }
            tv_join.setVisibility(View.VISIBLE);
        }else {
            tv_join.setVisibility(View.GONE);
        }


    }

}
