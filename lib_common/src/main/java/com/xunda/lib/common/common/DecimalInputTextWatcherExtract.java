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

    private static final String Period = ".";
    private static final String Zero = "0";

    /**
     * 需要设置该 DecimalInputTextWatcher 的 EditText
     */
    private EditText editText = null;
    private Button btn_charge = null;
    private TextView tv_beyond_below_toast = null;
    private double maxSingleRechargeAmount,minSingleRechargeAmount,remains_money;
    /**
     * 默认  小数的位数   2 位
     */
    private static final int DEFAULT_DECIMAL_DIGITS = 2;

    private int decimalDigits;// 小数的位数

    /**
     * @param editText      editText
     * @param decimalDigits 小数的位数
     */
    public DecimalInputTextWatcherExtract(EditText editText, int decimalDigits, double maxSingleRechargeAmount, double minSingleRechargeAmount,double remains_money
            , Button btn_charge, TextView tv_beyond_below_toast) {
        if (editText == null) {
            throw new RuntimeException("editText can not be null");
        }
        this.maxSingleRechargeAmount = maxSingleRechargeAmount;
        this.minSingleRechargeAmount = minSingleRechargeAmount;
        this.remains_money = remains_money;
        this.editText = editText;
        this.btn_charge = btn_charge;
        this.tv_beyond_below_toast = tv_beyond_below_toast;
        if (decimalDigits <= 0)
            throw new RuntimeException("decimalDigits must > 0");
        this.decimalDigits = decimalDigits;
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
            //限制最大长度
//            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(totalDigits)});

            if (s.contains(Period)) {
                //超过小数位限定位数,只保留限定小数位数
                if (s.length() - 1 - s.indexOf(Period) > decimalDigits) {
                    s = s.substring(0,
                            s.indexOf(Period) + decimalDigits + 1);
                    editable.replace(0, editable.length(), s.trim());
                }
            }
            //如果首位输入"."自动补0
            if (s.trim().equals(Period)) {
                s = Zero + s;
                editable.replace(0, editable.length(), s.trim());
            }
            //首位输入0时,不再继续输入
            if (s.startsWith(Zero)
                    && s.trim().length() > 1) {
                if (!s.substring(1, 2).equals(Period)) {
                    editable.replace(0, editable.length(), Zero);
                }
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
        if (StringUtil.isBlank(StringUtil.getEditText(editText))){
            btn_charge.setBackgroundResource(R.drawable.btn_shape_gray_round);
            btn_charge.setEnabled(false);
            tv_beyond_below_toast.setVisibility(View.GONE);
        }else if(Double.parseDouble(value)<minSingleRechargeAmount){
            btn_charge.setBackgroundResource(R.drawable.btn_shape_gray_round);
            btn_charge.setEnabled(false);
            tv_beyond_below_toast.setVisibility(View.VISIBLE);
            tv_beyond_below_toast.setText("*未达到单笔提现最低金额"+StringUtil.DoubleToAmountString(minSingleRechargeAmount)+"元");
        }else if(Double.parseDouble(value)>remains_money){
            btn_charge.setBackgroundResource(R.drawable.btn_shape_gray_round);
            btn_charge.setEnabled(false);
            tv_beyond_below_toast.setVisibility(View.VISIBLE);
            tv_beyond_below_toast.setText("*超出可提现零钱余额");
        } else if(Double.parseDouble(value)>maxSingleRechargeAmount){
            btn_charge.setBackgroundResource(R.drawable.btn_shape_gray_round);
            btn_charge.setEnabled(false);
            tv_beyond_below_toast.setVisibility(View.VISIBLE);
            tv_beyond_below_toast.setText("*超出单笔提现金额上限"+StringUtil.DoubleToAmountString(maxSingleRechargeAmount)+"元");
        } else {
            btn_charge.setBackgroundResource(R.drawable.selector_btn_round);
            btn_charge.setEnabled(true);
            tv_beyond_below_toast.setVisibility(View.GONE);
        }
    }
}
