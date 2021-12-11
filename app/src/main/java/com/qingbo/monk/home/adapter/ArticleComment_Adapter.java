package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class ArticleComment_Adapter extends BaseQuickAdapter<ArticleCommentBean, BaseViewHolder> {

    public ArticleComment_Adapter() {
        super(R.layout.articlecomment_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleCommentBean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        RecyclerView commentChildren_List = helper.getView(R.id.commentChildren_List);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        nickName_Tv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数


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

        content_Tv.setText(item.getComment());
        String userDate = DateUtil.getUserDate(item.getCreateTime());
        time_Tv.setText(userDate);
        follow_Count.setText(item.getLikedNum());
        mes_Count.setText(item.getChildrenNum());
        isLike(item.getLikedStatus(), item.getLikedNum(), follow_Img, follow_Count);

        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
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


}


