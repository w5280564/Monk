package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * 两个按钮蓝色按钮字体的弹出框
 */
public class TwoButtonDialogBlue_No_Finish extends Dialog implements
        View.OnClickListener {

    private ConfirmListener listener;
    private String title, content, left, right;
    private Context mContext;
    private TextView tv_content, tv_right, tv_left;
    private Integer leftColor, rightColor;

    public TwoButtonDialogBlue_No_Finish(Context context, String content, String left,
                                         String right, ConfirmListener confirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.listener = confirmListener;
        this.content = content;
        this.left = left;
        this.right = right;
        this.mContext = context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_two_button_blue);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();

    }

    private void initView() {
        LinearLayout ll_parent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth / 5f * 4);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
        layoutParams.width = parentWidth;
        ll_parent.setLayoutParams(layoutParams);

        tv_content = findViewById(R.id.tv_content);
        tv_right = findViewById(R.id.tv_right);
        tv_left = findViewById(R.id.tv_left);

        tv_content.setText(content);
        tv_right.setText(right);
        if (rightColor != null) {
            tv_right.setTextColor(ContextCompat.getColor(mContext, rightColor));
        }
        tv_left.setText(left);
        if (rightColor != null) {
            tv_left.setTextColor(ContextCompat.getColor(mContext, leftColor));
        }
    }

    private void initEvent() {
        tv_right.setOnClickListener(this);
        tv_left.setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */

    public interface ConfirmListener {

        void onClickRight();

        void onClickLeft();
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.tv_right) {
            listener.onClickRight();
            dismiss();
        } else if (id == R.id.tv_left) {
            listener.onClickLeft();
            dismiss();
        }
    }


    @Override
    public void onBackPressed() {

    }

}
