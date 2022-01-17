package com.qingbo.monk.person.mygroup;


import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyTestHis_Bean;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.message.activity.ChatActivity;
import com.xunda.lib.common.common.glide.GlideUtils;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class AreaItem extends TreeItem<MyTestHis_Bean.DataDTO.ListDTO> {

    @Override
    public int getLayoutId() {
        return R.layout.item_two;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder) {
        ImageView head_Img = holder.getView(R.id.head_Img);
        ImageView art_Img = holder.getView(R.id.art_Img);
        holder.setText(R.id.artName_tv, data.getAuthorName());
        holder.setText(R.id.time_Tv, data.getCreateTime());
        holder.setText(R.id.artContent_Tv, data.getContent());
        holder.setText(R.id.follow_Count, data.getLikedNum());
        holder.setText(R.id.mes_Count, data.getCommentNum());

        GlideUtils.loadCircleImage(holder.itemView.getContext(), head_Img,data.getAvatar());
        if (!TextUtils.isEmpty(data.getImages())) {
            List<String> strings = Arrays.asList(data.getImages().split(","));
            GlideUtils.loadRoundImage(holder.itemView.getContext(), art_Img, strings.get(0),9);
        }else {
            art_Img.setImageResource(R.mipmap.img_pic_none_square);
        }
    }


    @Override
    public int getSpanSize(int maxSpan) {
        return 0;
    }


    @Override
    public void onClick(ViewHolder viewHolder) {
        super.onClick(viewHolder);
        String type = data.getType();
        ArticleDetail_Activity.startActivity(viewHolder.itemView.getContext(), data.getArticleId(), "0",type);
    }
}
