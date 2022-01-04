package com.qingbo.monk.Slides.adapter;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.HomeInterestBean;
import com.qingbo.monk.bean.InterestBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;
import java.util.List;

public class Interest_Adapter extends BaseQuickAdapter<InterestBean, BaseViewHolder> {
    public Interest_Adapter() {
        super(R.layout.interest_adapter);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InterestBean item) {
        ImageView head_Img = helper.getView(R.id.head_Img);
        TextView Interest_Name = helper.getView(R.id.Interest_Name);
        TextView add_Tv = helper.getView(R.id.add_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        DiscussionAvatarView headListView = helper.getView(R.id.headListView);
        TextView join_Tv = helper.getView(R.id.join_Tv);

        GlideUtils.loadCircleImage(mContext, head_Img, item.getGroupImage());
        Interest_Name.setText(item.getGroupName());
        String s = item.getJoinNum() + "人加入";
        add_Tv.setText(s);
        content_Tv.setText(item.getGroupDes());
        if (item.getDetail().get(0) != null) {
            String avatar = item.getDetail().get(0).getAvatar();
            groupHead(avatar, headListView);

        }
        isJoin(item.getJoinStatus(), join_Tv);
        helper.addOnClickListener(R.id.join_Tv);

    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    /**
     * 横向多个头像
     *
     * @param userS
     */
    private void groupHead(String userS, DiscussionAvatarView avatarView) {
        if (!TextUtils.isEmpty(userS)) {
            String[] imgS = userS.split(",");
            List<String> mList = new ArrayList<>();
            int length = imgS.length;
            if (length > 3) {
                length = 3;
            }
            for (int i = 0; i < length; i++) {
                mList.add(imgS[i]);
            }
            avatarView.initDatas(mList);
        }
    }

    /**
     *
     * @param state 1是已加入  其他都是未加入
     * @param join_Tv
     */
    public void isJoin(String state, TextView join_Tv) {
        if (TextUtils.equals(state,"1")) {
            join_Tv.setText("已加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            join_Tv.setText("加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }

}
