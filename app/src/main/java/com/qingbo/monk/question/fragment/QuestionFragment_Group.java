package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.login.adapter.HaveAdapter;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 社群问答
 */
public class QuestionFragment_Group extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private QuestionGroupAdapter mQuestionGroupAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_group;
    }


    @Override
    public void onResume() {
        super.onResume();
        tvName.setText(StringUtil.getStringValue(SharePref.user().getUserName()));
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mQuestionGroupAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mQuestionGroupAdapter);
        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mQuestionGroupAdapter.setNewData(mList);
    }


}
