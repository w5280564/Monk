package com.qingbo.monk.home.fragment;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;

/**
 * 宇宙
 */
public class UniverseFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_universe;
    }

    @Override
    public void initImmersionBar() {
     setStatusBar();
    }
    /**
     * 设置状态栏
     */
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }
}
