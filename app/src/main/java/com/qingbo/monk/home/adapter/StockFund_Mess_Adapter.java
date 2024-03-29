package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.bean.StockFundMes_Bean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class StockFund_Mess_Adapter extends BaseQuickAdapter<StockFundMes_Bean, BaseViewHolder> {
    private boolean isFund;

    public StockFund_Mess_Adapter() {
        super(R.layout.stockfund_mess_adapter);
    }
    public StockFund_Mess_Adapter(boolean isFund) {
        super(R.layout.stockfund_mess_adapter);
        this.isFund = isFund;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, StockFundMes_Bean item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        TextView collect_Tv = helper.getView(R.id.collect_Tv);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        group_Name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数

        GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar(), R.mipmap.icon_logo);
        group_Name.setText(item.getAuthor());
        labelFlow(lable_Lin, mContext, item.getTagName());
        if (TextUtils.isEmpty(item.getTitle())) {
            title_Tv.setVisibility(View.GONE);
        } else {
            title_Tv.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(item.getContent())) {
            content_Tv.setVisibility(View.GONE);
        } else {
            content_Tv.setVisibility(View.VISIBLE);
        }
        title_Tv.setText(item.getTitle());
        content_Tv.setText(item.getContent());
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }
        follow_Count.setText(item.getLikecount());
        mes_Count.setText(item.getCommentcount());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);
        isCollect(item.getIs_collect(), collect_Tv);

        if (isFund){
            group_Img.setVisibility(View.GONE);
            group_Name.setVisibility(View.GONE);
            lable_Lin.setVisibility(View.GONE);
            time_Tv.setVisibility(View.GONE);
        }


        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.group_Img);
        helper.addOnClickListener(R.id.share_Img);
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


    public void labelFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
            label_Name.setOnClickListener(v -> {
            });
        }
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


