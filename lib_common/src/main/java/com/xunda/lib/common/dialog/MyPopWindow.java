package com.xunda.lib.common.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xunda.lib.common.R;
import java.util.List;

import q.rorbin.badgeview.DisplayUtil;

/**
 * PopupWindow
 */
public class MyPopWindow extends PopupWindow implements View.OnClickListener {
    private OnPopWindowClickListener mListener;
    private boolean haveEdit;//是否能编辑
    private Activity context;

    public MyPopWindow(Activity context, boolean haveEdit, OnPopWindowClickListener mListener) {
        this.mListener = mListener;
        this.haveEdit = haveEdit;
        this.context = context;
        init(context);
    }

    private void init(Activity context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_layout, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        initView(content);
    }

    private void initView(View content_view) {
        TextView tv_edit = content_view.findViewById(R.id.tv_edit);
        TextView tv_delete = content_view.findViewById(R.id.tv_delete);
        tv_edit.setVisibility(haveEdit?View.VISIBLE:View.GONE);
        tv_edit.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.tv_edit) {
            mListener.onClickEdit();
        }else if(view.getId()==R.id.tv_delete){
            mListener.onClickDelete();
        }
        dismiss();
    }


    public interface OnPopWindowClickListener {
        void onClickEdit();
        void onClickDelete();
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            // 以下拉方式显示popupwindow
            showAsDropDown(parent, DisplayUtil.dp2px(context, -20), DisplayUtil.dp2px(context, -60));
        } else {
            dismiss();
        }
    }

}
