package com.qingbo.monk.home.fragment;

import android.view.View;
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

        initViewPager(0);
    }
}
