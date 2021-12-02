package com.flyco.banner.transform;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

import com.nineoldandroids.view.ViewHelper;

public class FlowTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        ViewHelper.setRotationY(page, position * -30f);
    }
}
