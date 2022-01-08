package com.xunda.lib.common.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 设置入群费用
 */

public class DecimalInputTextWatcherExtract implements TextWatcher {

    private static final String Zero = "0";

    /**
     * 需要设置该 DecimalInputTextWatcher 的 EditText
     */
    private EditText editText = null;
    private TextView tv_beyond_below_toast = null;
    private int maxAmount = 6000;
    private int minAmount = 50;

    /**
     * @param editText      editText
     * @param tv_beyond_below_toast      tv_beyond_below_toast
     */
    public DecimalInputTextWatcherExtract(EditText editText,TextView tv_beyond_below_toast) {
        if (editText == null) {
            throw new RuntimeException("editText can not be null");
        }
        this.editText = editText;
        this.tv_beyond_below_toast = tv_beyond_below_toast;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            String s = editable.toString();
            editText.removeTextChangedListener(this);
            //首位输入0时,不再继续输入
            if (s.startsWith(Zero)
                    && s.trim().length() > 1) {
                editable.replace(0, editable.length(), Zero);
            }
            judgeButtonIsClickAble(s);
            editText.addTextChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 判断按钮是否可以点击
     */
    private void judgeButtonIsClickAble(String value) {
        if (StringUtil.isBlank(value)){
            tv_beyond_below_toast.setVisibility(View.GONE);
        }else if(Integer.parseInt(value)<minAmount || Integer.parseInt(value)>maxAmount){
            tv_beyond_below_toast.setVisibility(View.VISIBLE);
        }else{
            tv_beyond_below_toast.setVisibility(View.GONE);
        }
    }
}
