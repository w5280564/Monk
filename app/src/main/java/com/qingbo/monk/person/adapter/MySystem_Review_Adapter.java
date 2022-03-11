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
import com.qingbo.monk.bean.SystemLikes_Bean;
import com.qingbo.monk.bean.SystemReview_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;

import java.util.Arrays;
import java.util.List;

/**
 * ================================================
 *
 * @Description 我的点赞item
 * <p>
 * ================================================
 */
public class MySystem_Review_Adapter extends BaseQuickAdapter<SystemReview_Bean, BaseViewHolder> {
    public MySystem_Review_Adapter() {
        super(R.layout.mysysytem_review_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SystemReview_Bean item) {
        helper.setText(R.id.time_Tv, item.getCreateTime());
        helper.setText(R.id.review_Tv, item.getLikeTypeName());
        TextView comment_Tv = helper.getView(R.id.comment_Tv);
        ImageView art_Img = helper.getView(R.id.art_Img);
        helper.setText(R.id.artContent_Tv, item.getContent().getContent());

        String likeType = item.getLikeType();
        if (TextUtils.equals(likeType, "1")) {
            comment_Tv.setVisibility(View.GONE);
        } else {
            comment_Tv.setVisibility(View.VISIBLE);
            String authorName = "@" + item.getContent().getAuthorName();
            String content = "："+item.getContent().getContent();
            String setStr = authorName + content;
            int length = authorName.length();
            int startLength = 0;
            setName(setStr, length, startLength, length, comment_Tv);
        }
        if (!TextUtils.isEmpty(item.getContent().getArticleImages())) {
            List<String> strings = Arrays.asList(item.getContent().getArticleImages().split(","));
            GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0), 9);
        } else {
            art_Img.setImageResource(R.mipmap.img_pic_none_square);
        }

    }


    /**
     * @param name        要显示的数据
     * @param nameLength  要改颜色的字体长度
     * @param startLength 改色起始位置
     * @param endLength   改色结束位置
     * @param viewName
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }
}
