package com.qingbo.monk.home.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.home.activity.CombinationDetail_Activity;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class Combination_Adapter extends BaseQuickAdapter<HomeCombinationBean, BaseViewHolder> {
    public Combination_Adapter() {
        super(R.layout.combination_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeCombinationBean item) {
        TextView comName_TV = helper.getView(R.id.comName_TV);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        TextView more_Tv = helper.getView(R.id.more_Tv);

        comName_TV.setText(item.getName());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);
        mes_Count.setText(item.getCommentcount());
        time_Tv.setText(DateUtil.getUserDate(item.getCreateTime()));

        addRecycleData(mNineView, item,more_Tv);

        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.mes_Img);
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
     * 添加子列表数据
     *
     * @param mNineView
     * @param item
     */
    private void addRecycleData(RecyclerView mNineView, HomeCombinationBean item,TextView more_Tv) {
        if (mNineView != null){
            mNineView.removeAllViews();
        }
//        mNineView.addItemDecoration(getRecyclerViewDivider(R.drawable.recyleview_solid));//添加横向分割线
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        Combination_Shares_Adapter combination_shares_adapter = new Combination_Shares_Adapter();
        mNineView.setAdapter(combination_shares_adapter);
        if (!ListUtils.isEmpty(item.getDetail())) {
            List<HomeCombinationBean.DetailDTO> detail = item.getDetail();
            List<HomeCombinationBean.DetailDTO> detail1 = new ArrayList<>();
            for (int k = 0; k < detail.size(); k++) {
                if (k > 4) {
                    more_Tv.setVisibility(View.VISIBLE);
                    continue;
                }
                detail1.add(detail.get(k));
            }
            combination_shares_adapter.setNewData(detail1);
        }
        combination_shares_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
                String id = item.getId();
                CombinationDetail_Activity.startActivity(mContext, "0", id);
            }
        });
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


    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
