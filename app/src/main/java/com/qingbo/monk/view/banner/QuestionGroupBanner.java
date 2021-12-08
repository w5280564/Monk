package com.qingbo.monk.view.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.common.glide.RoundedCornersTransform;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

public class QuestionGroupBanner extends BaseIndicatorBanner<MyGroupBean, QuestionGroupBanner> {
    private int drawable;

    public QuestionGroupBanner(Context context) {
        this(context, null, 0);
    }

    public QuestionGroupBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuestionGroupBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public QuestionGroupBanner placeholder(int resId) {
        if (resId > 0) {
            drawable = resId;
        }
        return this;
    }


    @Override
    public QuestionGroupBanner setSource(List<MyGroupBean> list) {
        if (list == null || list.size() <= 1) {
            setIndicatorShow(false);
            setAutoScrollEnable(false);
        } else {
            setIndicatorShow(true);
            setAutoScrollEnable(true);
        }
        return super.setSource(list);
    }

    @Override
    public void onTitleSelect(TextView tv, int position) {

    }

    @Override
    public View onCreateItemView(int position) {//可根据不同的位置创建不同的视图
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_group_banner, null);

        ImageView iv_image = layout.findViewById(R.id.iv_img);
        TextView tv_group_name = layout.findViewById(R.id.tv_group_name);
        TextView tv_name = layout.findViewById(R.id.tv_name);
        ImageView iv_img_top = layout.findViewById(R.id.iv_img_top);
        MyGroupBean item = mDatas.get(position);

        tv_group_name.setText(StringUtil.getStringValue(item.getShequnName()));
        tv_name.setText(StringUtil.getStringValue(item.getNickname()));
        RoundedCornersTransform transform = new RoundedCornersTransform(mContext, DisplayUtil.dip2px(mContext, 8));
        transform.setNeedCorner(true, true, false, false);
                Glide.with(mContext).load(item.getShequnImage()).placeholder(R.mipmap.bg).transforms(transform).into(iv_img_top);
        return layout;
    }
}