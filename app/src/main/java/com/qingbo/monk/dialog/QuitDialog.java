package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * 退出或关闭弹窗
 */
public class QuitDialog extends Dialog implements View.OnClickListener {

    private ConfirmListener listener;
    private String title, content, left, right;
    private Context mContext;
    private TextView close_Tv, quit_Tv,content_Tv;
    private Integer leftColor, rightColor;

    public QuitDialog(Context context, String content, String left,
                      String right, ConfirmListener confirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.listener = confirmListener;
        this.content = content;
        this.left = left;
        this.right = right;
        this.mContext = context;
    }

    public QuitDialog(Context context, String content, String left,
                      String right, int leftColor,
                      int rightColor, ConfirmListener confirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.listener = confirmListener;
        this.content = content;
        this.left = left;
        this.right = right;
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_quit);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();

    }

    private void initView() {
        ConstraintLayout Con_parent = findViewById(R.id.Con_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = screenWidth;//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = Con_parent.getLayoutParams();
        layoutParams.width = parentWidth;
        Con_parent.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);// 弹出框在底部位置

                content_Tv = findViewById(R.id.content_Tv);
        close_Tv = findViewById(R.id.close_Tv);
        quit_Tv = findViewById(R.id.quit_Tv);

        content_Tv.setText(content);
        quit_Tv.setText(right);
        if (rightColor != null) {
            quit_Tv.setTextColor(ContextCompat.getColor(mContext, rightColor));
        }
        close_Tv.setText(left);
        if (rightColor != null) {
            close_Tv.setTextColor(ContextCompat.getColor(mContext, leftColor));
        }
    }

    private void initEvent() {
        quit_Tv.setOnClickListener(this);
        close_Tv.setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */

    public interface ConfirmListener {

        void onClickClose();

        void onClickQuit();
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.close_Tv) {
            listener.onClickClose();
            dismiss();
        } else if (id == R.id.quit_Tv) {
            listener.onClickQuit();
            dismiss();

        }
    }
}
