package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xunda.lib.common.R;
import com.xunda.lib.common.base.GridAdapter;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.DisplayUtil;
import java.util.ArrayList;
import java.util.List;


public class GridDialog extends Dialog {
    private Context mContext;
    private List<NameIdBean> mList = new ArrayList<>();
    private OnSelectItemListener mOnSelectItemListener;
    private GridAdapter mAdapter;

    public GridDialog(Context context, List<NameIdBean> mTempList,OnSelectItemListener mOnSelectItemListener) {
        super(context, R.style.CenterDialogStyle);
        this.mContext = context;
        this.mOnSelectItemListener = mOnSelectItemListener;
        mList.addAll(mTempList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_grid);
        setCanceledOnTouchOutside(true);
        initView();
    }

    public void resetList(List<NameIdBean> mTempList){
        mList.clear();
        mList.addAll(mTempList);
        mAdapter.setNewData(mList);
    }

    private void initView() {
        LinearLayout llParent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth / 6f * 5);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = llParent.getLayoutParams();
        layoutParams.width = parentWidth;
        llParent.setLayoutParams(layoutParams);

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);
        mAdapter= new GridAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(DisplayUtil.dip2px(mContext, 15), mContext.getResources().getColor(R.color.white)));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NameIdBean mNameIdBean = (NameIdBean) adapter.getItem(position);
                if (mNameIdBean!=null) {
                    mOnSelectItemListener.onSelectItem(position);
                    dismiss();
                }
            }
        });
    }


    public interface OnSelectItemListener{
        void onSelectItem(int position);
    }
}
