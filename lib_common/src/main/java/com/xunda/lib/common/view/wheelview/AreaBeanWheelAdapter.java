package com.xunda.lib.common.view.wheelview;

import android.content.Context;

import com.xunda.lib.common.bean.AreaBean;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.List;

/**
 * ================================================
 *
 * @Description: 省市区三级联动
 * @Author: Zhangliangliang
 * @CreateDate: 2021/8/15 15:24
 * ================================================
 */
public class AreaBeanWheelAdapter extends AbstractWheelTextAdapter {

    // items
    private List<AreaBean> wheelDates;

    /**
     * Constructor
     */
    public AreaBeanWheelAdapter(Context context, List<AreaBean> wheelDates) {
        super(context);
        this.wheelDates = wheelDates;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (!ListUtils.isEmpty(wheelDates)) {
            String item = wheelDates.get(index).getName();
            return item;
        }else{
            return "";
        }
    }

    @Override
    public int getItemsCount() {
        if (!ListUtils.isEmpty(wheelDates)) {
            return wheelDates.size();
        }else{
            return 0;
        }
    }

}