package com.qingbo.monk.question.adapter;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.GroupMemberBean;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.view.RoundImageView;
import java.util.List;

public class ChooseMemberAdapter extends BaseQuickAdapter<GroupMemberBean, BaseViewHolder> {

    public ChooseMemberAdapter(List<GroupMemberBean> imageList) {
        super(R.layout.item_choose_member,imageList);
    }




    @Override
    protected void convert(BaseViewHolder helper, GroupMemberBean item) {
        RoundImageView iv_image = helper.getView(R.id.iv_image);
        FrameLayout fl_layout = helper.getView(R.id.fl_layout);


        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWith = (screenWidth - DisplayUtil.dip2px(mContext, 94))/4;//dp要转成px
        ViewGroup.LayoutParams llParams = fl_layout.getLayoutParams();
        llParams.width = parentWith;
        fl_layout.setLayoutParams(llParams);

        ViewGroup.LayoutParams imageParams = iv_image.getLayoutParams();
        imageParams.height = parentWith;
        iv_image.setLayoutParams(imageParams);

        int type = item.getType();
        if (type == 1) {//添加
            iv_image.setImageResource(R.mipmap.tianjia_group);
        }else if (type == 2) {//踢人
            iv_image.setImageResource(R.mipmap.shanchu);
        }else {//网络图片
            ImgLoader.getInstance().displayCrop(mContext,iv_image,item.getAvatar(),R.mipmap.img_pic_none_square);
        }
    }

}
