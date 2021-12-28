package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xunda.lib.common.R;

/**
 * 设置或取消管理员弹窗
 */
public class SetManagerDialog extends Dialog implements View.OnClickListener {

    private ConfirmListener listener;
    private Context mContext;
    private TextView submit, tv_join_time, tv_name;
    private ImageView iv_header;

    public SetManagerDialog(Context context,ConfirmListener confirmListener) {
        super(context, R.style.bottomrDialogStyle);
        this.listener = confirmListener;
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_manager);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();

    }

    private void initView() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);// 弹出框在底部位置
        iv_header = findViewById(R.id.iv_header);
        submit = findViewById(R.id.tv_submit);
        tv_join_time = findViewById(R.id.tv_join_time);
        tv_name = findViewById(R.id.tv_name);

    }

    private void initEvent() {
        submit.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */

    public interface ConfirmListener {
        void onSet();
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_submit) {
            listener.onSet();
            dismiss();
        }
    }
}
