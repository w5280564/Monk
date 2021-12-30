package com.qingbo.monk.home.adapter;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleCommentBean;
import com.qingbo.monk.home.activity.ArticleDetali_CommentList_Activity;
import com.qingbo.monk.home.activity.CombinationDetail_CommentList_Activity;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.List;

public class CombinationComment_Adapter extends BaseQuickAdapter<ArticleCommentBean, BaseViewHolder> {
    String articleId, type;

    public CombinationComment_Adapter(String articleId, String type) {
        super(R.layout.articlecomment_adapter);
        this.articleId = articleId;
        this.type = type;

    }


    @Override
    public void convert(@NonNull BaseViewHolder helper, ArticleCommentBean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        ImageView mes_Img = helper.getView(R.id.mes_Img);
        ConstraintLayout children_Comment = helper.getView(R.id.children_Comment);
        RecyclerView commentChildren_List = helper.getView(R.id.commentChildren_List);
        TextView commentMore_Tv = helper.getView(R.id.commentMore_Tv);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
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

        if (ListUtils.isEmpty(item.getChildrens())) {//是否有二级评论
            children_Comment.setVisibility(View.GONE);
        } else {
            children_Comment.setVisibility(View.VISIBLE);
            showNineView(commentChildren_List, item);
            if (!TextUtils.isEmpty(item.getChildrenNum()) && Integer.parseInt(item.getChildrenNum()) > 3) {//二级评论超过三条
                commentMore_Tv.setVisibility(View.VISIBLE);
                String format = String.format("查看全部%1$s条回复", item.getChildrenNum());
                commentMore_Tv.setText(format);
            }
        }

        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.commentMore_Tv);
        helper.addOnClickListener(R.id.mes_Img);
        helper.addOnClickListener(R.id.commentChildren_List);
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




    /**
     * 加载二级评论
     */
    private void showNineView(RecyclerView mNineView, ArticleCommentBean data) {
        LinearLayoutManager layout = new LinearLayoutManager(mContext);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mNineView.setLayoutManager(layout);
        ArticleComment_Childrens_Adapter childrens_adapter = new ArticleComment_Childrens_Adapter();
        mNineView.setAdapter(childrens_adapter);
        childrens_adapter.setNewData(data.getChildrens());


        childrens_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                String id = data.getId();
//                CombinationDetail_CommentList_Activity.startActivity(mContext, data, id);
                onClickLister.onItemClick(view,position);
            }
        });
    }

    public interface OnLister {
        void onItemClick(View view, int postion);
    }
    private OnLister onClickLister;

    public void setOnClickLister(OnLister onClickLister) {
        this.onClickLister = onClickLister;
    }


}


