package com.xunda.lib.common.view;

import android.content.Context;
import android.util.AttributeSet;
import com.liys.lswitch.LSwitch;

public class MyLSwitch extends LSwitch {
    public MyLSwitch(Context context) {
        super(context);
    }

    public MyLSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public boolean isCheck(){
        return isChecked;
    }

    public void setCheck(boolean isChecked){
        this.isChecked = isChecked;
    }

}
