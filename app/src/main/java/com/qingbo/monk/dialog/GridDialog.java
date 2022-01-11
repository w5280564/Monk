package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.question.adapter.GridChooseAdapter;
import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.utils.AndroidUtil;
import java.util.ArrayList;
import java.util.List;


public class GridDialog extends Dialog {
    private Context mContext;
    private List<NameIdBean> mList = new ArrayList<>();
    private OnSelectItemListener mOnSelectItemListener;
    private GridChooseAdapter mAdapter;

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
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        LinearLayout llParent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth / 6f * 5);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = llParent.getLayoutParams();
        layoutParams.width = parentWidth;
        llParent.setLayoutParams(layoutParams);

        GridView gridView = findViewById(R.id.gridview_choose);
        mAdapter = new GridChooseAdapter((BaseActivity) mContext, mList);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NameIdBean mNameIdBean = mList.get(position);
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
