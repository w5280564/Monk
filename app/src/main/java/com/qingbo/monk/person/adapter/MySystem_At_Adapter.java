package com.qingbo.monk.person.adapter;

import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.SystemAt_Bean;
import com.qingbo.monk.bean.SystemLikes_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;

import java.util.Arrays;
import java.util.List;

/**
 * ================================================
 *
 * @Description at我的item
 * <p>
 * ================================================
 */
public class MySystem_At_Adapter extends BaseQuickAdapter<SystemAt_Bean, BaseViewHolder> {
    public MySystem_At_Adapter() {
        super(R.layout.mysysytem_at_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SystemAt_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        helper.setText(R.id.name_Tv, item.getAuthorName());
        helper.setText(R.id.time_Tv, item.getCreateTime());
        ImageView art_Img = helper.getView(R.id.art_Img);
        helper.setText(R.id.artName_tv, item.getTitle());
        helper.setText(R.id.artContent_Tv, item.getContent());

        String avatar = item.getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Img, avatar);
        if (!TextUtils.isEmpty(item.getImages())) {
            List<String> strings = Arrays.asList(item.getImages().split(","));
            GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0), 9);
        } else {
            art_Img.setImageResource(R.mipmap.img_pic_none_square);
        }

    }


}
