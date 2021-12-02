package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.qingbo.monk.view.banner.QuestionGroupBanner;
import com.xunda.lib.common.common.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 社群问答
 */
public class QuestionFragment_Group extends BaseFragment {

    @BindView(R.id.img_top_banner)
    QuestionGroupBanner img_top_banner;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_top_right)
    LinearLayout ll_top_right;
    private QuestionGroupAdapter mQuestionGroupAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_group;
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

    @Override
    protected void initLocalData() {
        handleBanner();
    }

    private void handleBanner() {
        List<String> mList2 = new ArrayList<>();
        mList2.add("");
        mList2.add("");
        mList2.add("");
        mList2.add("");
        img_top_banner.placeholder(R.mipmap.img_pic_none_square).setSource(mList2).startScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        img_top_banner.pauseScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        img_top_banner.goOnScroll();
    }

}
