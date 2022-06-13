package com.qingbo.monk.home.adapter;

import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.CommentBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;

import java.util.List;

public class CommentDetail_Adapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public CommentDetail_Adapter() {
        super(R.layout.comment_item_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentBean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        ImageView mes_Img = helper.getView(R.id.mes_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);


        String is_anonymous = item.getIsAnonymous();//1是匿名
        if (TextUtils.equals(is_anonymous, "1")) {
            nickName_Tv.setText("匿名用户");
            head_Img.setBackgroundResource(R.mipmap.icon_logo);
            head_Img.setEnabled(false);
        } else {
            GlideUtils.loadCircleImage(mContext, head_Img, item.getAvatar(), R.mipmap.icon_logo);
            nickName_Tv.setText(item.getAuthorName());
        }
        if (TextUtils.isEmpty(item.getComment())) {


            content_Tv.setVisibility(View.GONE);
        } else {
            content_Tv.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(item.getReplyerName())) {
            content_Tv.setText(item.getComment());
        } else {
            String format = String.format("回复@%1$s:%2$s", item.getReplyerName(), item.getComment());
            int startLength = 2;
            int endLength = startLength + (item.getReplyerName() + "：").length();
            setName(format, startLength, startLength, endLength, content_Tv);
        }

        String userDate = DateUtil.getUserDate(item.getCreateTime());
        time_Tv.setText(userDate);
        follow_Count.setText(item.getLikedNum());
//        mes_Count.setText(item.getChildrenNum());
        isLike(item.getLikedStatus(), item.getLikedNum(), follow_Img, follow_Count);

        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.mes_Img);
        helper.addOnClickListener(R.id.head_Img);
        helper.addOnClickListener(R.id.share_Tv);
        TextView collect_Tv = helper.getView(R.id.collect_Tv);
        isCollect(item.getIs_collect(), collect_Tv);
        helper.addOnClickListener(R.id.collect_Tv);
    }


    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(nowLike + "");
    }


    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }

    /**
     * @param name        要显示的数据
     * @param nameLength  要改颜色的字体长度
     * @param startLength 改色起始位置
     * @param endLength   改色结束位置
     * @param viewName
     */
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }

    /**
     * 收藏/取消收藏
     * @param status
     * @param collect_Tv
     */
    public void isCollect(String status, TextView collect_Tv) {
        int mipmap = R.mipmap.shoucang;
        if (TextUtils.equals(status, "1")) {
            mipmap = R.mipmap.shoucang_select;
        }
        collect_Tv.setBackgroundResource(mipmap);
    }

}


