package com.qingbo.monk.person.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SystemLikes_Bean item) {
//        ImageView head_Img = helper.getView(R.id.head_Img);
//        TextView name_Tv = helper.getView(R.id.name_Tv);
//        TextView time_Tv = helper.getView(R.id.time_Tv);
//        TextView comment_Tv = helper.getView(R.id.comment_Tv);
//        ConstraintLayout content_Con = helper.getView(R.id.content_Con);
//        TextView artName_tv = helper.getView(R.id.artName_tv);
//        ImageView art_Img = helper.getView(R.id.art_Img);
//        TextView artContent_Tv = helper.getView(R.id.artContent_Tv);
//        TextView artTime_Tv = helper.getView(R.id.artTime_Tv);
//        changeShapColor(content_Con, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));

//        String avatar = PrefUtil.getUser().getAvatar();
//        GlideUtils.loadCircleImage(mContext,head_Img,avatar);
//        name_Tv.setText(item.getAuthorName());
//        time_Tv.setText(item.getCreateTime());
//        if (TextUtils.isEmpty(item.getNickname())) {//为空代表评论的是文章,不为空代表是回复评论
//            comment_Tv.setText(item.getComment());
//        } else {
//            String format = String.format("回复@%1$s：%2$s", item.getNickname(), item.getComment());
//            comment_Tv.setText(format);
//        }
//        artName_tv.setText(item.getTitle());
//        if (!TextUtils.isEmpty(item.getImages())) {
////            List<String> strings = StringUtil.stringToList(item.getImages());
//            List<String> strings = Arrays.asList(item.getImages().split(","));
//            GlideUtils.loadRoundImage(mContext, art_Img, strings.get(0),9);
//        }else {
//            art_Img.setImageResource(R.mipmap.img_pic_none_square);
//        }
//        artContent_Tv.setText(item.getContent());
//        artTime_Tv.setText(item.getArticleTime());

    }
}
