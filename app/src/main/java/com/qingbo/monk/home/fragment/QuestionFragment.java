package com.qingbo.monk.home.fragment;

import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.question.fragment.QuestionFragment_Group;
import com.qingbo.monk.question.fragment.QuestionFragment_Square;
import com.xunda.lib.common.bean.AppMenuBean;

/**
 * 问答
 */
public class QuestionFragment extends BaseTabLayoutFragment {
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question;
    }

    @Override
    protected void initView(View mRootView) {
        mViewPager = mRootView.findViewById(R.id.viewpager);
        mTabLayout = mRootView.findViewById(R.id.tabs);
        initMenuData();
    }

    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(new QuestionFragment_Group());
                bean.setName("问答社群");
            } else{
                fragments.add(new QuestionFragment_Square());
                bean.setName("问答广场");
            }
            menuList.add(bean);
        }

        initChildViewPager(0);
    }
}
