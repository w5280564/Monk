package com.qingbo.monk.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class Follow_Adapter extends BaseQuickAdapter<HomeFllowBean, BaseViewHolder> {

    public Follow_Adapter() {
        super(R.layout.follow_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeFllowBean item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        ImageView personHead_Img = helper.getView(R.id.personHead_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        ImageView follow_Img = helper.getView(R.id.follow_Img);

        title_Tv.setText(item.getTitle());
        content_Tv.setText(item.getContent());
        if (!TextUtils.isEmpty(item.getCreateTime())) {
            time_Tv.setText(DateUtil.getUserDate(item.getCreateTime()));
        }

        GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar());
        group_Name.setText(item.getTitle());
        follow_Count.setText(item.getLikedNum());
        mes_Count.setText(item.getCommentNum());

        String action = item.getAction();//1是社群 2是兴趣圈 3是个人
        if (TextUtils.equals(action, "1")) {
            if (item.getShequn() != null) {
                GlideUtils.loadCircleImage(mContext, personHead_Img, item.getShequn().getUserAvatar(), R.mipmap.icon_logo);
                nickName_Tv.setText(item.getShequn().getShequnName());
            }
            String is_anonymous = item.getIsAnonymous();//1是匿名
            if (TextUtils.equals(is_anonymous, "1")) {
                nickName_Tv.setText("匿名");
            }
        } else if (TextUtils.equals(action, "2")) {
            if (item.getGroup() != null) {
                GlideUtils.loadCircleImage(mContext, personHead_Img, item.getGroup().getUserAvatar(), R.mipmap.icon_logo);
                nickName_Tv.setText(item.getGroup().getGroupName());
            }
        } else if (TextUtils.equals(action, "3")) {
            group_Name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数
            personHead_Img.setVisibility(View.GONE);
            nickName_Tv.setVisibility(View.GONE);
            labelFlow(lable_Lin, mContext, item.getTagName());
            isFollow(item.getFollow_status(), follow_Tv, send_Mes);
            if (!TextUtils.isEmpty(item.getCreateTime())) {
                String userDate = DateUtil.getUserDate(item.getCreateTime())+" "+item.getCompany_name();
                time_Tv.setText(userDate);
            }
        }
        isLike(item.getLiked_status(), item.getLikedNum(), follow_Img, follow_Count);

        //多张图片
        if (!TextUtils.isEmpty(item.getImages())) {
            String[] imgS = item.getImages().split(",");
            List<String> strings = Arrays.asList(imgS);
            mNineView.setLayoutManager(new NineGridLayoutManager(mNineView.getContext()));
            NineGridAdapter nineGridAdapter = new NineGridAdapter();
            mNineView.setAdapter(nineGridAdapter);
            nineGridAdapter.setNewData(strings);

            nineGridAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    onItemImgClickLister.OnItemImgClickLister(position, strings);
                }
            });
        }
        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
    }

    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0")) {
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
            follow_Tv.setVisibility(View.GONE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }

    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
//            nowLike -= 1;
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
//            nowLike += 1;
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


