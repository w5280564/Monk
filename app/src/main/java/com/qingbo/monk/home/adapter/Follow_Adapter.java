package com.qingbo.monk.home.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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

import java.util.Arrays;

public class Follow_Adapter extends BaseQuickAdapter<HomeFllowBean.DataDTO.ListDTO, BaseViewHolder> {
    private RecyclerView mNineView;

    public Follow_Adapter() {
        super(R.layout.follow_adapter);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeFllowBean.DataDTO.ListDTO item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        LinearLayout lable_Lin = helper.getView(R.id.lable_Lin);
        ImageView personHead_Img = helper.getView(R.id.personHead_Img);
        TextView nickName_Tv = helper.getView(R.id.nickName_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        mNineView = helper.getView(R.id.nine_grid);

        title_Tv.setText(item.getTitle());
        content_Tv.setText(item.getContent());
        time_Tv.setText(DateUtil.getUserDate(item.getCreateTime()));

        GlideUtils.loadCircleImage(mContext, group_Img, item.getAvatar());
        group_Name.setText(item.getTitle());

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
            personHead_Img.setVisibility(View.GONE);
            nickName_Tv.setVisibility(View.GONE);
            labelFlow(lable_Lin, mContext, item.getTagName());
        }


        String[] imgS = item.getImages().split(",");
        mNineView.setLayoutManager(new NineGridLayoutManager(mNineView.getContext()));
        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        nineGridAdapter.setNewData(Arrays.asList(imgS));
        mNineView.setAdapter(nineGridAdapter);
        nineGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mContext, String.format("Menu:%d", position), Toast.LENGTH_SHORT).show();
            }
        });
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
            setColor(i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
            label_Name.setOnClickListener(v -> {
            });
        }
    }

    private void setColor(int index, TextView tv) {
        int dex = index % 4;
        if (dex == 0) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_FF801F));
            changeShapColor(tv, ContextCompat.getColor(mContext, R.color.lable_color_ff801f));
        } else if (dex == 1) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_1F8FE5));
            changeShapColor(tv, ContextCompat.getColor(mContext, R.color.lable_color_1F8FE5));
        } else if (dex == 2) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_76AD45));
            changeShapColor(tv, ContextCompat.getColor(mContext, R.color.lable_color_76AD45));
        } else if (dex == 3) {
            tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_7622BD));
            changeShapColor(tv, ContextCompat.getColor(mContext, R.color.lable_color_7622BD));
        }
    }

    //修改shape背景颜色
    public static void changeShapColor(View v, int color) {
        GradientDrawable da = (GradientDrawable) v.getBackground();
        da.setColor(color);
    }


}


