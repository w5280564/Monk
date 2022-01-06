package com.qingbo.monk.question.adapter;

import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.GroupMemberBean;


public class HeaderProvider extends BaseItemProvider<GroupMemberBean,BaseViewHolder> {

    @Override
    public int viewType() {
        return GroupManagerOrPartnerBigAdapter.TYPE_HEADER;
    }

    @Override
    public int layout() {
        return R.layout.item_group_member_list_main;
    }

    @Override
    public void convert(BaseViewHolder helper, GroupMemberBean item, int position) {
        TextView header = helper.getView(R.id.header);
        int letterShow = item.getLetterShow();
        if (letterShow==1) {
            header.setVisibility(View.GONE);
        }else{
            header.setVisibility(View.VISIBLE);
            header.setText(item.getFirstLetter());
        }
    }

}
