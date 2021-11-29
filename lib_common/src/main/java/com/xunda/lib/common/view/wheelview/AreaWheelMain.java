package com.xunda.lib.common.view.wheelview;

import android.app.Activity;
import android.view.View;

import com.xunda.lib.common.R;
import com.xunda.lib.common.bean.AreaBean;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ================================================
 *
 * @Description: 城市选择utils
 * ================================================
 */
public class AreaWheelMain {
    private View view;
    private Activity activity;
    private WheelView wv_province, wv_city;
    private List<AreaBean> provinceList = new ArrayList<>();
    private List<AreaBean> cityList = new ArrayList<>();
    private String[] name = new String[2];
    private Integer[] id = new Integer[2];




    public AreaWheelMain(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
    }


    public void setProvinceList(List<AreaBean> provinceList) {
        this.provinceList = provinceList;
    }

    public void initProvinceCityPicker() {
        wv_province = (WheelView) view.findViewById(R.id.wv_province);
        wv_province.setVisibleItems(7);
        wv_city = (WheelView) view.findViewById(R.id.wv_city);
        wv_city.setVisibleItems(7);
        initProvinceWheelView();
    }


    /**
     * 初始化省份联动
     */
    private void initProvinceWheelView() {
        wv_province.setViewAdapter((new AreaBeanWheelAdapter(activity, provinceList)));
        if(!ListUtils.isEmpty(provinceList)){
            wv_province.setCurrentItem(0);
            initProvinceWheel(0);

            wv_province.addChangingListener(new OnWheelChangedListener() {


                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    initProvinceWheel(newValue);
                }

                @Override
                public void onStopChanged(WheelView wheel, int currentItem) {

                }
            });
        }



    }

    private void initProvinceWheel(int position) {
        name[0] = provinceList.get(position).getName();
        id[0] = provinceList.get(position).getId();
        if (!ListUtils.isEmpty(provinceList.get(position).getSub())) {
            initCityWheelView(provinceList.get(position).getSub());
        } else {
            initCityWheelView(null);
            name[1] = "";
            id[1] = 0;
        }
    }


    /**
     * 初始化城市联动
     * @param mTempCityList
     */
    private void initCityWheelView(List<AreaBean> mTempCityList) {
        cityList.clear();
        if (!ListUtils.isEmpty(mTempCityList)) {
            cityList.addAll(mTempCityList);
        }
        wv_city.setViewAdapter((new AreaBeanWheelAdapter(activity, cityList)));
        if(!ListUtils.isEmpty(cityList)){
            wv_city.setCurrentItem(0);
            initCityWheel(0);

            wv_city.addChangingListener(new OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    initCityWheel(newValue);
                }

                @Override
                public void onStopChanged(WheelView wheel, int currentItem) {

                }
            });
        }
    }

    private void initCityWheel(int position) {
        name[1] = cityList.get(position).getName();
        id[1] = cityList.get(position).getId();
    }



    public List<String> getAreaNameList() {
        return Arrays.asList(name);
    }

    public List<Integer> getAreaIdList() {
        return Arrays.asList(id);
    }

}
