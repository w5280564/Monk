package com.qingbo.monk.question.adapter;

import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseListViewAdapter;
import com.qingbo.monk.bean.ViewHolder;
import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.List;

/**
 * 标签筛选适配器
 */
public class GridChooseAdapter extends BaseListViewAdapter<NameIdBean> {

    public GridChooseAdapter(BaseActivity activity, List<NameIdBean> mList) {
        super(activity, mList, R.layout.item_screen_grid);
    }



    @Override
    public void getView(ViewHolder vh, int position, NameIdBean item) {
        TextView tv_name = vh.getView(R.id.tv_name);
        tv_name.setText(StringUtil.getStringValue(item.getName()));
        tv_name.setTextColor(ContextCompat.getColor(mActivity,item.isSelect()?R.color.text_color_444444:R.color.text_color_6f6f6f));
        tv_name.setBackgroundResource(item.isSelect()?R.drawable.shape_screen_bag_select:R.drawable.shape_screen_bag_normal);
    }
}
