package com.qingbo.monk.person.mygroup;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MyTestHis_Bean;

import java.util.List;

/**
 *
 */
public class ProvinceItem extends TreeItemGroup<MyTestHis_Bean.DataDTO> {
//    @Override
//    protected void init() {
//        super.init();
//        setExpand(false);
//    }


    @Override
    public int getLayoutId() {
        return R.layout.itme_one;
    }

    @Override
    public boolean isCanExpand() {
        return true;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    protected List<TreeItem> initChild(MyTestHis_Bean.DataDTO data) {
        List<TreeItem> items = ItemHelperFactory.createItems(data.getList(), this);
        return items;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder) {
//        if (isExpand()) {
//            holder.setImageResource(R.id.iv_right, R.drawable.ic_keyboard_arrow_down_black_24dp);
//        } else {
//            holder.setImageResource(R.id.iv_right, R.drawable.ic_keyboard_arrow_right_black_24dp);
//        }
        String listName = "";
        if (data.getName().equals("today")){
            listName = "今天";
        }else if (data.getName().equals("yesterday")){
            listName = "昨天";
        }else if (data.getName().equals("earlier")){
            listName = "上周";
        }
        holder.setText(R.id.tv_name, listName);
//        holder.setText(R.id.tv_content, data.getCount()+"");
    }

    @Override
    public void onClick(ViewHolder viewHolder) {
        super.onClick(viewHolder);
//        if (isExpand()) {
//            viewHolder.setImageResource(R.id.iv_right, R.drawable.ic_keyboard_arrow_down_black_24dp);
//        } else {
//            viewHolder.setImageResource(R.id.iv_right, R.drawable.ic_keyboard_arrow_right_black_24dp);
//        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, RecyclerView.LayoutParams layoutParams, int position) {
        super.getItemOffsets(outRect, layoutParams, position);
        outRect.bottom = 1;
    }
}
