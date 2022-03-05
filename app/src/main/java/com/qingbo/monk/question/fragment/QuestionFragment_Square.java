package com.qingbo.monk.question.fragment;

import android.graphics.Typeface;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragmentWithSon;

import butterknife.BindView;

/**
 * 问答广场
 */
public class QuestionFragment_Square extends BaseFragmentWithSon implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_all)
    RadioButton rb_all;
    @BindView(R.id.rb_me)
    RadioButton rb_me;

    private int fragmentId = R.id.fl_question_square;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_square;
    }

    @Override
    protected void initView() {
        addFragment(new QuestionListFragmentAll());
        addFragment(new QuestionListFragmentMy());
        showFragment(0, fragmentId);
    }


    @Override
    protected void initEvent() {
        rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int view_id) {
        switch (view_id){
            case R.id.rb_all:
                rb_all.setTypeface(Typeface.DEFAULT_BOLD);
                rb_me.setTypeface(Typeface.DEFAULT);
                showFragment(0, fragmentId);
                break;
            case R.id.rb_me:
                rb_me.setTypeface(Typeface.DEFAULT_BOLD);
                rb_all.setTypeface(Typeface.DEFAULT);
                showFragment(1, fragmentId);
                break;
        }
    }
}
