package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.R;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

/**
 * 两个按钮蓝色按钮字体的弹出框
 */
public class LinkDialog extends Dialog implements
        View.OnClickListener {

    private ConfirmListener listener;
    private String title, content, left, right;
    private Context mContext;
    private TextView tv_content, tv_right, tv_left;
    private Integer leftColor, rightColor;
    private EditText name_edit,pageUrl_edit;

    public LinkDialog(Context context, String content, String left,
                      String right, ConfirmListener confirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.listener = confirmListener;
        this.content = content;
        this.left = left;
        this.right = right;
        this.mContext = context;
    }

    public LinkDialog(Context context, String content, String left,
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
        setContentView(R.layout.dialog_link);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();

    }

    private void initView() {
        ConstraintLayout ll_parent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth / 5f * 4);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
        layoutParams.width = parentWidth;
        ll_parent.setLayoutParams(layoutParams);

//        tv_content = findViewById(R.id.tv_content);
        name_edit = findViewById(R.id.name_edit);
        pageUrl_edit = findViewById(R.id.pageUrl_edit);
        tv_right = findViewById(R.id.tv_right);
        tv_left = findViewById(R.id.tv_left);

//        tv_content.setText(content);
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

        void onClickRight(String name,String Url);

        void onClickLeft();
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.tv_right) {
            if (TextUtils.isEmpty(name_edit.getText().toString())){
                T.s("名称不能为空",2000);
                return;
            }
            if (!StringUtil.isHttpUrl(pageUrl_edit.getText().toString())){
                T.s("请输入正确的主页地址",2000);
                return;
            }
            if (!pageUrl_edit.getText().toString().contains("http://")){
                pageUrl_edit.setText("http://"+pageUrl_edit.getText().toString());
            }
            String s = name_edit.getText().toString();
            String s1 = pageUrl_edit.getText().toString();
            listener.onClickRight(s,s1);
            dismiss();
        } else if (id == R.id.tv_left) {
            listener.onClickLeft();
            dismiss();
        }
    }
}
