package com.qingbo.monk.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.SmallAreaCodeBean;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.PinnedSectionListView;
import java.util.ArrayList;
import java.util.List;

/**
 * 区号列表
 */
public class AreaCodeAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter , SectionIndexer {
    private List<TypeItem> list = new ArrayList<TypeItem>();;
    private Context context;
    private OnChooseItemListener clickListener;

    public AreaCodeAdapter(Context context, List<SmallAreaCodeBean> mTitleAndContList, OnChooseItemListener clickListener){
        this.context = context;
        this.clickListener = clickListener;
        generateItem(mTitleAndContList);
    }

    private void generateItem(List<SmallAreaCodeBean> mTitleAndContList){
        if(mTitleAndContList == null || mTitleAndContList.size() == 0){
            return;
        }
        // 遍历数据分组
        for(SmallAreaCodeBean bean : mTitleAndContList){
            String name = bean.getArea();
            TypeItem item = null;
            if(bean.getClassification()==1){
                item = new TypeItem(TypeItem.SECTION, name);
            }else{
                item = new TypeItem(TypeItem.ITEM, name);
            }
            item.bean = bean;
            list.add(item);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }


    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == TypeItem.SECTION;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).bean.getArea();
            char firstChar = sortStr.charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).bean.getArea().charAt(0);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SectionHolder sectionHolder = null;
        ItemHolder itemHolder = null;
        int type = getItemViewType(i);
        if(view == null){
            switch(type){
                case TypeItem.SECTION:
                    view = LayoutInflater.from(context).inflate(R.layout.item_code_list_middle, null);
                    sectionHolder = new SectionHolder();
                    sectionHolder.titleView = (TextView) view.findViewById(R.id.tv_Letter);
                    view.setTag(sectionHolder);
                    break;
                case TypeItem.ITEM:
                    view = LayoutInflater.from(context).inflate(R.layout.item_code_list_small, null);
                    itemHolder = new ItemHolder();
                    itemHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                    itemHolder.tv_area_code = (TextView) view.findViewById(R.id.tv_area_code);
                    itemHolder.itemLayout = (LinearLayout) view.findViewById(R.id.ll_item);
                    view.setTag(itemHolder);
                    break;
            }
        }else{
            switch(type){
                case TypeItem.SECTION:
                    sectionHolder = (SectionHolder) view.getTag();
                    break;
                case TypeItem.ITEM:
                    itemHolder = (ItemHolder) view.getTag();
                    break;
            }
        }

        TypeItem item = list.get(i);
        SmallAreaCodeBean bean = item.bean;
        switch(type){
            case TypeItem.SECTION:
                sectionHolder.titleView.setText(item.name);
                break;
            case TypeItem.ITEM:
                setItemClickListener(itemHolder.itemLayout, bean.getCode());
                itemHolder.tv_name.setText(StringUtil.getStringValue(bean.getArea()));
                itemHolder.tv_area_code.setText(StringUtil.getStringValue("+"+bean.getCode()));
                break;
        }
        return view;
    }

    public void setItemClickListener(LinearLayout layout, final String area_code){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onChooseItem(area_code);
                }
            }
        });
    }



    class ItemHolder{
        public TextView tv_name;
        public TextView tv_area_code;
        public LinearLayout itemLayout;
    }

    class SectionHolder{
        public TextView titleView;
    }


    public static class TypeItem{
        public static final int ITEM = 0;
        public static final int SECTION = 1;


        public int type;
        public String name;

        public TypeItem(int type, String name) {
            this.type = type;
            this.name = name;
        }
        public SmallAreaCodeBean bean;
    }


    public interface OnChooseItemListener{
        void onChooseItem(String code);
    }
}
