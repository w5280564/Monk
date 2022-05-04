package com.qingbo.monk.home.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.ArticleCommentBean;

public class ArticleComment_Childrens_Adapter extends BaseQuickAdapter<ArticleCommentBean.ChildrensDTO, BaseViewHolder> {

    public ArticleComment_Childrens_Adapter() {
        super(R.layout.comment_children);
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleCommentBean.ChildrensDTO item) {
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        String format = String.format("%1$s：%2$s", item.getAuthorName(), item.getComment());
        int startLength = 0;
        int endLength = (item.getAuthorName() + "：").length();
        setName(format,startLength,startLength,endLength,nickName_Tv);

    }


    /**
     * @param name          要显示的数据
     * @param nameLength    要改颜色的字体长度
     * @param startLength   改色起始位置
     * @param endLength     改色结束位置
     * @param viewName
     */
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }





}

