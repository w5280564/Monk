package com.qingbo.monk.Slides.widget.adapter;

import android.content.Context;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qingbo.monk.R;
import com.xunda.lib.common.common.glide.GlideUtils;

/**
 * Created by wensefu
 */
public class ImageCardItem extends BaseCardItem {
    private static final String TAG = "ImageCardItem";

    private String url;
    private String label;

    public ImageCardItem(Context context, String url, String label) {
        super(context);
        this.url = url;
        this.label = label;
    }

    public static class ViewHolder {
        public ImageView left;
        public ImageView right;
        public ImageView up;
        public ImageView down;
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.find_adapter, null);
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView labelview = convertView.findViewById(R.id.label);
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
//        Glide.with(mContext).load(url).placeholder(R.mipmap.icon_logo).centerCrop().into(imageView);
//        GlideUtils.loadImage(mContext,imageView,url);
        GlideUtils.loadImage(mContext,imageView,url);
        labelview.setText(label);
        return convertView;
    }
}
