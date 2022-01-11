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
import com.qingbo.monk.bean.HomeFllowBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

public class Focus_Adapter extends BaseQuickAdapter<HomeFllowBean, BaseViewHolder> {

    public Focus_Adapter() {
        super(R.layout.foucs_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeFllowBean item) {
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
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        ImageView mes_Img = helper.getView(R.id.mes_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 100);
        group_Name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数


        String is_anonymous = item.getIsAnonymous();//1是匿名
        if (TextUtils.equals(is_anonymous, "1")) {
            group_Name.setText("匿名用户");
            group_Img.setBackgroundResource(R.mipmap.icon_logo);
            group_Img.setEnabled(false);
        } else {
            GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar(), R.mipmap.icon_logo);
            group_Name.setText(item.getAuthorName());
            labelFlow(lable_Lin, mContext, item.getTagName());
            isFollow(item.getFollow_status(), follow_Tv, send_Mes);
        }
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
            String companyName = TextUtils.isEmpty(item.getCompany_name()) ? "" : item.getCompany_name();
            String userDate = DateUtil.getUserDate(item.getCreateTime()) + " " + companyName;
//            String userDate = DateUtil.getUserDate(item.getCreateTime()) + " " + item.getCompany_name();
            time_Tv.setText(userDate);
        }
        follow_Count.setText(item.getLikedNum());
        mes_Count.setText(item.getCommentNum());
        isLike(item.getLiked_status(), item.getLikedNum(), follow_Img, follow_Count);

        showNineView(item.getImages(), mNineView);//多张图片

        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
        helper.addOnClickListener(R.id.mes_Img);
        helper.addOnClickListener(R.id.send_Mes);
        helper.addOnClickListener(R.id.group_Img);
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
     * 加载多图适配器
     *
     * @param imgString
     */
    private void showNineView(String imgString, RecyclerView mNineView) {
        if (!TextUtils.isEmpty(imgString)) {
            mNineView.setVisibility(View.VISIBLE);
            String[] imgS = imgString.split(",");
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
        } else {
            mNineView.setVisibility(View.GONE);
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


