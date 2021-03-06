package com.qingbo.monk.person.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyCommentBean;
import com.qingbo.monk.bean.SystemLikes_Bean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.PrefUtil;

import java.util.Arrays;
import java.util.List;

/**
 * ================================================
 *
 * @Description 我的点赞item
 * <p>
 * ================================================
 */
public class MySystem_Like_Adapter extends BaseQuickAdapter<SystemLikes_Bean, BaseViewHolder> {
    public MySystem_Like_Adapter() {
        super(R.layout.mysysytem_like_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SystemLikes_Bean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        helper.setText(R.id.name_Tv, item.getNickname());
        helper.setText(R.id.time_Tv, item.getCreateTime());
        helper.setText(R.id.zan_Tv, item.getLikeTypeName());
        TextView comment_Tv = helper.getView(R.id.comment_Tv);
        ImageView art_Img = helper.getView(R.id.art_Img);
        helper.setText(R.id.artName_tv, item.getContent().getAuthorName());
        helper.setText(R.id.artContent_Tv, item.getContent().getContent());

        String likeType = item.getLikeType();
        if (TextUtils.equals(likeType, "1")) {
            comment_Tv.setVisibility(View.GONE);
        } else {
            comment_Tv.setVisibility(View.VISIBLE);
            String authorName = "@" + item.getContent().getAuthorName();
            String content = "："+item.getContent().getContent();
//            String format = String.format("@%1$s：%2$s", authorName, content);
//            comment_Tv.setText(format);
            String setStr = authorName + content;
            int length = authorName.length();
            int startLength = 0;
            setName(setStr, length, startLength, length, comment_Tv);
        }
        String avatar = item.getAvatar();
        GlideUtils.loadCircleImage(mContext, head_Img, avatar);
        if (!TextUtils.isEmpty(item.getContent().getArticleImages())) {
//            List<String> strings = StringUtil.stringToList(item.getImages());
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
