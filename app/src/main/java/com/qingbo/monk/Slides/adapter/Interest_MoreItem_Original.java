package com.qingbo.monk.Slides.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.base.PhotoShowActivity;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.qingbo.monk.person.adapter.My_MoreItem_Adapter;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 兴趣组全部 多种布局 我发布的
 */
public class Interest_MoreItem_Original extends BaseItemProvider<MyDynamic_MoreItem_Bean, BaseViewHolder> {

    @Override
    public int viewType() {
        return My_MoreItem_Adapter.TYPE_MY;
    }

    @Override
    public int layout() {
        return R.layout.interest_item_original;
    }


    @Override
    public void convert(@NonNull BaseViewHolder helper, MyDynamic_MoreItem_Bean item, int position) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView tv_status = helper.getView(R.id.tv_status);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        ImageView more_Img = helper.getView(R.id.more_Img);
        viewTouchDelegate.expandViewTouchDelegate(more_Img, 100);
        TextView collect_Tv = helper.getView(R.id.collect_Tv);
        more_Img.setVisibility(View.GONE);
        group_Name.setFilters(new InputFilter[]{new ByteLengthFilter(14)});

        String is_anonymous = item.getIsAnonymous();//1是匿名
        if (TextUtils.equals(is_anonymous, "1")) {
            group_Name.setText("匿名用户");
            group_Img.setEnabled(false);
            group_Img.setImageResource(R.mipmap.icon_logo_round);
        } else {
            GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar());
            group_Name.setText(item.getNickname());
            labelFlow(lable_Lin, mContext, item.getTagName());
            isFollow(item.getStatusNum(), follow_Tv, send_Mes);
        }


        if (!StringUtil.isBlank(item.getTitle())) {
            title_Tv.setVisibility(View.VISIBLE);
            title_Tv.setText(item.getTitle());
        } else {
            title_Tv.setVisibility(View.GONE);
        }

        if (!StringUtil.isBlank(item.getContent())) {
            content_Tv.setVisibility(View.VISIBLE);
            content_Tv.setText(item.getContent());
        } else {
            content_Tv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }

        follow_Count.setText(item.getLikecount());
        mes_Count.setText(item.getCommentcount());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);

        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        List<String> strings = new ArrayList<>();
        mNineView.setLayoutManager(new NineGridLayoutManager(mNineView.getContext()));
        mNineView.setAdapter(nineGridAdapter);
        //多张图片
        if (!TextUtils.isEmpty(item.getImages())) {
            String[] imgS = item.getImages().split(",");
            strings.addAll(Arrays.asList(imgS));
            nineGridAdapter.setNewData(strings);
            nineGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    onItemImgClickLister.OnItemImgClickLister(position, strings);
                    jumpToPhotoShowActivity(position, strings);
                }
            });

        } else {
            nineGridAdapter.setNewData(null);
        }

        if (isMe(item.getAuthorId())) {
            more_Img.setVisibility(View.VISIBLE);
            String status = item.getStatus();//0待审核 1通过 2未通过
            if (TextUtils.equals(status, "0")) {
                tv_status.setVisibility(View.VISIBLE);
                tv_status.setText("待审核");
                setDrawableLeft(R.mipmap.weishenhe, tv_status);
            } else if (TextUtils.equals(status, "1")) {
                tv_status.setVisibility(View.VISIBLE);
                tv_status.setText("审核通过");
                setDrawableLeft(R.mipmap.shenhetongguo, tv_status);
            } else if (TextUtils.equals(status, "2")) {
                tv_status.setVisibility(View.VISIBLE);
                setDrawableLeft(R.mipmap.weitongguo, tv_status);
                tv_status.setText("未通过");
            } else {
                tv_status.setVisibility(View.GONE);
            }
        }
        isCollect(item.getIs_collect(), collect_Tv);

        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.more_Img);
        helper.addOnClickListener(R.id.share_Img);
        helper.addOnClickListener(R.id.collect_Tv);
    }


    private void setDrawableLeft(int mipmap, TextView status) {
        Drawable drawableLeft = mContext.getResources().getDrawable(mipmap);
        status.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
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


//    @Override
//    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
//        super.setOnItemClickListener(listener);
//    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }

    /**
     * 是否是我自己
     *
     * @return
     */
    private boolean isMe(String userID) {
        String id = PrefUtil.getUser().getId();
        if (TextUtils.equals(userID, id)) {
            return true;
        }
        return false;
    }

    /**
     * 跳转到查看图片页
     *
     * @param position
     * @param mImageList
     */
    public void jumpToPhotoShowActivity(int position, List<String> mImageList) {
        Intent intent = new Intent(mContext, PhotoShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgList", (Serializable) mImageList);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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


