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
 * Description: 小数位数限定(提现)
 */

public class DecimalInputTextWatcherExtract implements TextWatcher {

    private static final String Zero = "0";

    /**
     * 需要设置该 DecimalInputTextWatcher 的 EditText
     */
    private EditText editText = null;
    private Button btn_charge = null;

    /**
     * @param editText      editText
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcherExtract(EditText editText) {
        if (editText == null) {
            throw new RuntimeException("editText can not be null");
        }
        this.editText = editText;
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
//            judgeButtonIsClickAble(s);
            editText.addTextChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 判断按钮是否可以点击
     */
    private void judgeButtonIsClickAble(String value) {
        if (StringUtil.isBlank(StringUtil.getEditText(editText))){
            btn_charge.setBackgroundResource(R.drawable.btn_shape_gray_round);
            btn_charge.setEnabled(false);
        }else {
            btn_charge.setBackgroundResource(R.drawable.selector_btn_round);
            btn_charge.setEnabled(true);
        }
    }
}
