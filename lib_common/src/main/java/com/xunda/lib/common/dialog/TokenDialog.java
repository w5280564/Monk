package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * Token过期的弹出框（多覆盖了个onBackPressed方法）
 */
public class TokenDialog extends Dialog implements
        View.OnClickListener {

    private DialogConfirmListener listener;
    private String content,button_text;
    private Context mContext;
    private TextView tv_button;

    public TokenDialog(Context context,String content, String button_text, DialogConfirmListener confirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.listener = confirmListener;
        this.content = content;
        this.button_text = button_text;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_token);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();

    }


    private void initView() {
        LinearLayout ll_parent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth/3f * 2);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
        layoutParams.width = parentWidth;
        ll_parent.setLayoutParams(layoutParams);

        tv_button = findViewById(R.id.tv_button);
        TextView tv_content = findViewById(R.id.tv_content);

        tv_button.setText(button_text);
        tv_content.setText(content);
    }


    private void initEvent() {
        tv_button.setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */

    public interface DialogConfirmListener {

        void onConfirmClick();
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.tv_button) {
            listener.onConfirmClick();
            dismiss();
        }
    }


    @Override
    public void onBackPressed() {

    }


}
