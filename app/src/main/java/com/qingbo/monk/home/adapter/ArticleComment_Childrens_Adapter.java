package com.qingbo.monk.home.adapter;

import android.os.Build;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

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

