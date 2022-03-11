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
import com.qingbo.monk.bean.System_Examine_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;

import java.util.Arrays;
import java.util.List;

/**
 * ================================================
 *
 * @Description <p>
 * ================================================
 */
public class MySystem_Examine_Adapter extends BaseQuickAdapter<System_Examine_Bean, BaseViewHolder> {
    public MySystem_Examine_Adapter() {
        super(R.layout.mysysytem_examine_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, System_Examine_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        helper.setText(R.id.name_Tv, item.getAuthorName());
        helper.setText(R.id.time_Tv, item.getCreateTime());
        TextView toComment_Tv = helper.getView(R.id.toComment_Tv);
        TextView comment_Tv = helper.getView(R.id.comment_Tv);
        ImageView art_Img = helper.getView(R.id.art_Img);
        helper.setText(R.id.artName_tv, item.getTitle());
        helper.setText(R.id.artContent_Tv, item.getContent());

        String avatar = item.getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Img, avatar);

        if (TextUtils.equals(item.getArticleId(), "1")) { //1为评论文章 2为回复评论
            if (item.getParent() != null) {
                toComment_Tv.setText(item.getComment());
                toComment_Tv.setVisibility(View.VISIBLE);
                comment_Tv.setVisibility(View.GONE);
            }
        } else {

            if (item.getParent() != null) {
                //评论
                toComment_Tv.setVisibility(View.VISIBLE);
                String hui = "回复";
                String nickname = "@" + item.getParent().getNickname();
                String comment = item.getComment();
                String s = hui + nickname + comment;
                setName(s, nickname.length(), hui.length(), hui.length() + nickname.length(), toComment_Tv);

                //被回复的评论
                comment_Tv.setVisibility(View.VISIBLE);
                String authorName = "@" + item.getNickname();
                String content = "：" + item.getParent().getComment();
                String setStr = authorName + content;
                int length = authorName.length();
                int startLength = 0;
                setName(setStr, length, startLength, length, comment_Tv);
            }
        }
        if (!TextUtils.isEmpty(item.getImages())) {
//            List<String> strings = StringUtil.stringToList(item.getImages());
            List<String> strings = Arrays.asList(item.getImages().split(","));
            GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0), 3);
        } else {
            art_Img.setImageResource(R.mipmap.img_pic_none_square);
        }

        helper.addOnClickListener(R.id.reply_Tv_);

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
