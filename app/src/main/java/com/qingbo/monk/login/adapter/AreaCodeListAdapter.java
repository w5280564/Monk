package com.qingbo.monk.login.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.qingbo.monk.R;
import com.qingbo.monk.bean.MiddleAreaCodeBean;
import com.qingbo.monk.bean.SmallAreaCodeBean;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.List;

public class AreaCodeListAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List<MiddleAreaCodeBean> list;
    private OnChooseItemListener mOnChooseItemListener;

    public AreaCodeListAdapter(Context context, List<MiddleAreaCodeBean> list,OnChooseItemListener mOnChooseItemListener) {
        this.context = context;
        this.list = list;
        this.mOnChooseItemListener = mOnChooseItemListener;
    }


    @Override
    public int getCount() {
        if (list != null) return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list == null)
            return null;

        if (position >= list.size())
            return null;

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final MiddleAreaCodeBean item = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_code_list_middle, parent, false);
            viewHolder.tv_letter = convertView.findViewById(R.id.tv_Letter);
            viewHolder.ll_container_list = convertView.findViewById(R.id.ll_container_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tv_letter.setVisibility(View.VISIBLE);
            String letterFirst = item.getFirstLetter();
            if (!TextUtils.isEmpty(letterFirst)) {
                if (!isLetterDigitOrChinese(letterFirst)) {
                    letterFirst = "#";
                }else {
                    letterFirst = String.valueOf(letterFirst.toUpperCase().charAt(0));
                }
            }
            viewHolder.tv_letter.setText(letterFirst);
            createBottomList(item.getChildlist(),viewHolder.ll_container_list);
        } else {
            viewHolder.tv_letter.setVisibility(View.GONE);
        }
        return convertView;
    }



    private void createBottomList(List<SmallAreaCodeBean> childList,LinearLayout ll_container_list) {
        if (!ListUtils.isEmpty(childList)) {
            ll_container_list.setVisibility(View.VISIBLE);
            ll_container_list.removeAllViews();
            for (int i = 0; i < childList.size(); i++) {
                final SmallAreaCodeBean obj = childList.get(i);
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_code_list_small, null);
                TextView tv_name = itemView.findViewById(R.id.tv_name);
                TextView tv_area_code = itemView.findViewById(R.id.tv_area_code);
                tv_name.setText(StringUtil.getStringValue(obj.getArea()));
                tv_area_code.setText(StringUtil.getStringValue("+"+obj.getCode()));
                ll_container_list.addView(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code = obj.getCode();
                        if (mOnChooseItemListener!=null) {
                            mOnChooseItemListener.chooseItem(code);
                        }
                    }
                });
            }
        }else{
            ll_container_list.setVisibility(View.GONE);
        }

    }


    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            char firstChar = sortStr.charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getFirstLetter().charAt(0);
    }


    final static class ViewHolder {
        /**
         * 首字母
         */
        TextView tv_letter;

        /**
         * 列表
         */
        LinearLayout ll_container_list;
    }

    private boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }


    public interface OnChooseItemListener {
        void chooseItem(String code);
    }
}
