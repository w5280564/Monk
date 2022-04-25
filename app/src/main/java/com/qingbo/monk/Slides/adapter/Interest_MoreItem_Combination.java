package com.qingbo.monk.Slides.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;
import com.qingbo.monk.person.adapter.MyDynamic_MoreItem_Combination_Shares;
import com.qingbo.monk.person.adapter.My_MoreItem_Adapter;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Interest_MoreItem_Combination extends BaseItemProvider<MyDynamic_MoreItem_Bean, BaseViewHolder> {
    @Override
    public int viewType() {
        return My_MoreItem_Adapter.TYPE_Combination;
    }

    @Override
    public int layout() {
        return R.layout.interest_combination;
    }



    @Override
    public void convert(@NonNull BaseViewHolder helper, MyDynamic_MoreItem_Bean item, int position) {
        TextView report_Tv = helper.getView(R.id.report_Tv);
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        TextView comName_TV = helper.getView(R.id.comName_TV);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        TextView more_Tv = helper.getView(R.id.more_Tv);
        ImageView more_Img = helper.getView(R.id.more_Img);

        report_Tv.setText("转发仓位组合");
        if (!TextUtils.isEmpty(item.getExtraContent())) {
            String name = item.getCommentAuthorName();
            String comment = item.getCommentComment();
            String format = String.format("转发评论//@%1$s：%2$s", name, comment);
            int startLength = "转发评论//".length();
            int endLength = (String.format("转发评论//@%1$s：", name)).length();
            String extraContent = item.getExtraContent();
            if (TextUtils.isEmpty(extraContent)) {
                setName(format, startLength, startLength, endLength, report_Tv);
            } else {
                setName(extraContent, startLength, startLength, endLength, report_Tv);
            }
        }

        GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar());
        group_Name.setText(item.getNickname());
        labelFlow(lable_Lin, mContext, item.getTagName());
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }

        comName_TV.setText(item.getTitle());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);
        mes_Count.setText(item.getCommentcount());
        time_Tv.setText(DateUtil.getUserDate(item.getCreateTime()));
        addRecycleData(mNineView, item,more_Tv);

        isFollow(item.getStatusNum(), follow_Tv, send_Mes);

        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.mes_Img);
        helper.addOnClickListener(R.id.share_Img);
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
    private void addRecycleData(RecyclerView mNineView, MyDynamic_MoreItem_Bean item,TextView more_Tv) {
        if (mNineView != null){
            mNineView.removeAllViews();
        }
//        mNineView.addItemDecoration(getRecyclerViewDivider(R.drawable.recyleview_solid));//添加横向分割线
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mNineView.setLayoutManager(linearLayoutManager);
        MyDynamic_MoreItem_Combination_Shares combination_shares_adapter = new MyDynamic_MoreItem_Combination_Shares();
        mNineView.setAdapter(combination_shares_adapter);
        if (!ListUtils.isEmpty(item.getDetail())) {
            List<MyDynamic_MoreItem_Bean.DetailDTO> detail = item.getDetail();
            List<MyDynamic_MoreItem_Bean.DetailDTO> detail1 = new ArrayList<>();
            for (int k = 0; k < detail.size(); k++) {
                if (k > 4) {
                    more_Tv.setVisibility(View.VISIBLE);
                    continue;
                }
                detail1.add(detail.get(k));
            }
            combination_shares_adapter.setNewData(detail1);
        }

//        combination_shares_adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
////                HomeCombinationBean item = (HomeCombinationBean) adapter.getItem(position);
//                String id = item.getId();
//                CombinationDetail_Activity.startActivity(mContext, "0", id);
//            }
//        });
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


//    @Override
//    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
//        super.setOnItemClickListener(listener);
//    }

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
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0")|| TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }



}
