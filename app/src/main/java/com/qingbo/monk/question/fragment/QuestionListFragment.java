package com.qingbo.monk.question.fragment;

import android.os.Bundle;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;

/**
 * 问答列表
 */
public class QuestionListFragment extends BaseFragment {

    private int type;

    public static QuestionListFragment NewInstance(int type) {
        QuestionListFragment mFragment = new QuestionListFragment();
        Bundle mBundle = new Bundle();
        mBundle.putInt("type", type);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    protected void initLocalData() {
        getDataFromArguments();
    }

    private void getDataFromArguments() {
        Bundle b = getArguments();
        if (b!=null) {
            type = b.getInt("type", 1);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_list;
    }


}
