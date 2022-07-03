package com.qingbo.monk.Slides.widget.carditem;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingbo.monk.R;
import com.qingbo.monk.Slides.widget.carditem.BaseCardItem;
import com.qingbo.monk.bean.FindBean;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * Created by wensefu
 */
public class ImageCardItem extends BaseCardItem {
    private static final String TAG = "ImageCardItem";
    private int pos;
    private  FindBean findBean;

    public ImageCardItem(Context context, FindBean findBean) {
        super(context);
        this.findBean = findBean;
    }

    public static class ViewHolder {
        public ImageView left;
        public ImageView right;
        public ImageView up;
        public ImageView down;
    }

    public FindBean getFindBean() {
        return findBean;
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.find_adapter, null);
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView nickName_Tv = convertView.findViewById(R.id.nickName_Tv);
        TextView city_Tv = convertView.findViewById(R.id.city_Tv);
        LinearLayout lable_Lin = convertView.findViewById(R.id.lable_Lin);
        TextView content_Tv = convertView.findViewById(R.id.content_Tv);
        ImageView left = convertView.findViewById(R.id.left);
        ImageView right = convertView.findViewById(R.id.right);
        ImageView up = convertView.findViewById(R.id.up);
        ImageView down = convertView.findViewById(R.id.down);
        ViewHolder vh = new ViewHolder();
        vh.left = left;
        vh.right = right;
        vh.up = up;
        vh.down = down;
        convertView.setTag(vh);

        GlideUtils.loadCircleImage(mContext, imageView, findBean.getAvatar());
        nickName_Tv.setText(findBean.getNickname());
        city_Tv.setText(findBean.getCity());
        content_Tv.setText(findBean.getDescription());

        String tag = findBean.getIndustry()+","+findBean.getWork();
        labelFlow(lable_Lin, mContext, tag);

        convertView.setOnClickListener(v -> {
            String id = findBean.getId();
            MyAndOther_Card.actionStart(mContext, id);
        });
        return convertView;
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

}
