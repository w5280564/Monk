package com.qingbo.monk.question.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.UploadPictureBean_Local;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.DisplayUtil;

import java.util.List;

public class DynamicImageAdapter extends BaseQuickAdapter<UploadPictureBean_Local, BaseViewHolder> {

    public DynamicImageAdapter(List<UploadPictureBean_Local> imageList) {
        super(R.layout.item_choose_dynamic_image,imageList);
    }





    @Override
    protected void convert(BaseViewHolder helper, UploadPictureBean_Local item) {
        ImageView iv_image = helper.getView(R.id.iv_image);
        LinearLayout ll_layout = helper.getView(R.id.ll_layout);

        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWith = (screenWidth- DisplayUtil.dip2px(mContext,4))/3;//dp要转成px
        ViewGroup.LayoutParams llParams = ll_layout.getLayoutParams();
        llParams.width = parentWith;
        ll_layout.setLayoutParams(llParams);


        ViewGroup.LayoutParams imageParams = iv_image.getLayoutParams();
        imageParams.width = parentWith;
        imageParams.height = (int) (parentWith* 1.33);;
        iv_image.setLayoutParams(imageParams);


        ImageView img_delete = helper.getView(R.id.img_delete);

        helper.addOnClickListener(R.id.img_delete);

        if (item.getType() == 1) {//添加照片
            iv_image.setImageResource(R.mipmap.add);
            img_delete.setVisibility(View.GONE);
        }else {//网络图片
            Glide.with(mContext).load(item.getImageUri()).into(iv_image);
            img_delete.setVisibility(View.VISIBLE);
        }
    }

}
