package com.qingbo.monk.home.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.utils.DateUtil;

public class Combination_Adapter extends BaseQuickAdapter<HomeCombinationBean, BaseViewHolder> {
    public Combination_Adapter() {
        super(R.layout.combination_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeCombinationBean item) {
        TextView follow_Count = helper.getView(R.id.follow_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img,100);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);

        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);
        mes_Count.setText(item.getCommentcount());
        time_Tv.setText(DateUtil.getUserDate(item.getCreateTime()));

        mNineView.addItemDecoration(getRecyclerViewDivider(R.drawable.recyleview_solid));//添加横向分割线
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        Combination_Shares_Adapter combination_shares_adapter = new Combination_Shares_Adapter();
        mNineView.setAdapter(combination_shares_adapter);
        combination_shares_adapter.setNewData(item.getDetail());
        helper.addOnClickListener(R.id.follow_Img);
    }

    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(likes + "");
    }

    /**
     * 获取分割线
     *
     * @param drawableId 分割线id
     * @return
     */
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        CustomDecoration itemDecoration = new CustomDecoration(mContext, LinearLayoutManager.VERTICAL, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, drawableId));
        return itemDecoration;
    }

}
