package com.xunda.lib.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.AreaBean;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.view.wheelview.AreaWheelMain;

import java.util.List;

/**
 * ================================================
 *
 * @Description: 城市选择
 * ================================================
 */
public class PickCityDialog extends Dialog {
    private View view;
    private Activity activity;
    private CityChooseThreeIdCallback cityChooseCallback;
    private List<AreaBean> provinceList;

    public PickCityDialog(Activity activity, List<AreaBean> provinceList, CityChooseThreeIdCallback cityChooseCallback) {
        super(activity, R.style.bottomrDialogStyle);
        this.activity = activity;
        this.cityChooseCallback = cityChooseCallback;
        this.provinceList = provinceList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = getLayoutInflater().inflate(R.layout.wheel_area_choose, null);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        TextView tv_ok = view.findViewById(R.id.tv_ok);
        TextView tv_close = view.findViewById(R.id.tv_close);
        final AreaWheelMain wheelMain = new AreaWheelMain(view, activity);

        wheelMain.setProvinceList(provinceList);
        wheelMain.initProvinceCityPicker();
        tv_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                callback(wheelMain.getAreaNameList(),wheelMain.getAreaIdList());
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
        WindowManager.LayoutParams lp = window.getAttributes();
        int screenWidth = AndroidUtil.getScreenWidth(activity);//屏幕的宽度
        int dialogWidth = (int) (screenWidth / 5f * 4);//弹出框的宽度
        lp.width = dialogWidth;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }


    /**
     * 点击确定
     * @param nameList
     * @param idList
     */
    private void callback(List<String> nameList, List<Integer> idList) {
        if (cityChooseCallback != null) {
            cityChooseCallback.onConfirm(nameList,idList);
        }
    }


    public interface CityChooseThreeIdCallback{
        void onConfirm(List<String> nameList,List<Integer> idList);
    }

}
