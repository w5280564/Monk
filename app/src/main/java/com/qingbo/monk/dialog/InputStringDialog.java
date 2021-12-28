package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.NameIdBean;
import com.xunda.lib.common.common.DecimalInputTextWatcherExtract;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import java.util.ArrayList;
import java.util.List;


/**
 * 输入内容
 */
public class InputStringDialog extends Dialog {
    private Context mContext;
    private String cost ;
    private OnConfirmListener mOnConfirmListener;
    private EditText et_content;
    private TextView tv_confirm;

    public InputStringDialog(Context context, String cost, OnConfirmListener mOnConfirmListener) {
        super(context, R.style.CenterDialogStyle);
        this.mContext = context;
        this.cost = cost;
        this.mOnConfirmListener = mOnConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_input_string);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();
    }


    private void initView() {
        LinearLayout llParent = findViewById(R.id.ll_parent);
        int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
        int parentWidth = (int) (screenWidth / 6f * 5);//弹出框的宽度
        ViewGroup.LayoutParams layoutParams = llParent.getLayoutParams();
        layoutParams.width = parentWidth;
        llParent.setLayoutParams(layoutParams);

        et_content = findViewById(R.id.et_content);
        et_content.setText(StringUtil.getStringValue(cost));
        tv_confirm = findViewById(R.id.tv_confirm);


    }

    private void initEvent() {
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = StringUtil.getEditText(et_content);

                if (StringUtil.isBlank(content)) {
                    T.ss("请输入金额");
                    return;
                }

                mOnConfirmListener.onConfirm(content);
                dismiss();
            }
        });

        addEditTextListener_money();
    }



    /**
     * 给editext添加监听
     */
    private void addEditTextListener_money() {
        DecimalInputTextWatcherExtract mDecimalInputTextWatcherExtract = new DecimalInputTextWatcherExtract(et_content);
        et_content.addTextChangedListener(mDecimalInputTextWatcherExtract);
    }


    public interface OnConfirmListener{
        void onConfirm(String content);
    }
}
