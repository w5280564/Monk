package com.qingbo.monk.home.fragment;

import android.widget.TextView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.StringUtil;

import butterknife.BindView;

/**
 * 社群问答
 */
public class QuestionFragment_Group extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tvName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_group;
    }


    @Override
    public void onResume() {
        super.onResume();
        tvName.setText(StringUtil.getStringValue(SharePref.user().getUserName()));
    }
}
