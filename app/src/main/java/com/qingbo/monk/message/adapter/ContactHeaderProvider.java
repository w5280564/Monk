package com.qingbo.monk.message.adapter;

import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerBigAdapter;


public class ContactHeaderProvider extends BaseItemProvider<FriendContactBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return GroupManagerOrPartnerBigAdapter.TYPE_HEADER;
    }

    @Override
    public int layout() {
        return R.layout.item_contact_list_letter;
    }

    @Override
    public void convert(BaseViewHolder helper, FriendContactBean item, int position) {
        TextView tv_letter = helper.getView(R.id.tv_letter);
        tv_letter.setText(item.getFirstLetter());
    }

}
