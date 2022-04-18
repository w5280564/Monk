package com.qingbo.monk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.qingbo.monk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 评论 转发 编辑 删除
 *
 * @author Administrator 欧阳
 */
public class MesMore_Dialog extends Dialog implements OnClickListener {
    private boolean haveForWard ,haveEdit, haveDele;
    private Context context;
    private List<Map<Object, String>> platformList = new ArrayList<>();


    public void setMoreClickLister(MesMore_Dialog.moreClickLister moreClickLister) {
        this.moreClickLister = moreClickLister;
    }

    private moreClickLister moreClickLister;


    public MesMore_Dialog(Context context) {
        super(context, R.style.bottomrDialogStyle);
        this.context = context;
    }

    public MesMore_Dialog(Activity context, boolean haveEdit, boolean haveDele) {
        super(context, R.style.bottomrDialogStyle);
        this.haveForWard = haveForWard;
        this.haveEdit = haveEdit;
        this.haveDele = haveDele;
        this.context = context;
    }

    /**
     *
     * @param context
     * @param haveForWard 是否能分享
     * @param haveEdit 是否能编辑
     * @param haveDele 是否能删除
     */
    public MesMore_Dialog(Activity context, boolean haveForWard, boolean haveEdit, boolean haveDele) {
        super(context, R.style.bottomrDialogStyle);
        this.haveForWard = haveForWard;
        this.haveEdit = haveEdit;
        this.haveDele = haveDele;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mes_more);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setCanceledOnTouchOutside(true);

        TextView forward_Tv = findViewById(R.id.forward_Tv);
        View view13 = findViewById(R.id.view13);
        TextView edit_Tv = findViewById(R.id.edit_Tv);
        TextView dele_Tv = findViewById(R.id.dele_Tv);
        TextView cancel_Tv = findViewById(R.id.cancel_Tv);

        forward_Tv.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
        view13.setVisibility(haveForWard ? View.VISIBLE : View.GONE);
        edit_Tv.setVisibility(haveEdit ? View.VISIBLE : View.GONE);
        dele_Tv.setVisibility(haveDele ? View.VISIBLE : View.GONE);

        forward_Tv.setOnClickListener(this);
        edit_Tv.setOnClickListener(this);
        dele_Tv.setOnClickListener(this);
        cancel_Tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_Tv:
                moreClickLister.onClickForWard();
                break;
            case R.id.edit_Tv:
                moreClickLister.onClickEdit();
                break;
            case R.id.dele_Tv:
                moreClickLister.onClickDelete();
                break;
            case R.id.cancel_Tv:
                dismiss();
                break;
        }
        dismiss();
    }


    /**
     * 添加
     */
    public interface moreClickLister {
        void onClickForWard();

        void onClickEdit();

        void onClickDelete();
    }


}
