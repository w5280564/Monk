package com.qingbo.monk.question.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.UploadPictureBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.glide.RoundedCornersTransform;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.DisplayUtil;
import java.util.List;

public class ChooseImageAdapter extends BaseQuickAdapter<UploadPictureBean, BaseViewHolder> {

    public ChooseImageAdapter(List<UploadPictureBean> imageList) {
        super(R.layout.item_choose_image,imageList);
    }





    @Override
    protected void convert(BaseViewHolder helper, UploadPictureBean item) {
        ImageView iv_image = helper.getView(R.id.iv_image);
        LinearLayout ll_delete = helper.getView(R.id.ll_delete);
        FrameLayout fl_layout = helper.getView(R.id.fl_layout);


        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWith = (screenWidth - DisplayUtil.dip2px(mContext, 94))/3;//dp要转成px
        ViewGroup.LayoutParams llParams = fl_layout.getLayoutParams();
        llParams.width = parentWith;
        fl_layout.setLayoutParams(llParams);

        ViewGroup.LayoutParams imageParams = iv_image.getLayoutParams();
        imageParams.height = parentWith;
        iv_image.setLayoutParams(imageParams);

        helper.addOnClickListener(R.id.ll_delete);

        if (item.getType() == 1) {//添加照片
            iv_image.setImageResource(R.mipmap.tianjia);
            ll_delete.setVisibility(View.GONE);
        }else {//网络图片
            ImgLoader.getInstance().displayCrop(mContext,iv_image,item.getImageUrl(),R.mipmap.img_pic_none_square);
            ll_delete.setVisibility(View.VISIBLE);
        }
    }

}
