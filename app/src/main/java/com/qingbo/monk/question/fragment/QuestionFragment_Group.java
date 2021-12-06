package com.qingbo.monk.question.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.BaseSheQunBean;
import com.qingbo.monk.bean.SheQunBean;
import com.qingbo.monk.question.activity.CreateGroupStepOneActivity;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.qingbo.monk.view.banner.QuestionGroupBanner;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    private ActivityResultLauncher mActivityResultLauncher;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_group;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        initRecyclerView();
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result!=null) {
                    int resultCode = result.getResultCode();
                    if (resultCode== Activity.RESULT_OK) {

                    }
                }
            }
        });
    }

    private void initRecyclerView() {
        mQuestionGroupAdapter = new QuestionGroupAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mQuestionGroupAdapter);
    }

    @Override
    protected void initLocalData() {
        handleBanner();
    }

    @Override
    protected void getServerData() {
        getMyShequn();
        getAllShequn();
    }

    private void getMyShequn() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.myShequn, "我的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseSheQunBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseSheQunBean.class);
                            if (obj != null) {
                                List<SheQunBean> list = obj.getList();
                                mQuestionGroupAdapter.setNewData(list);
                            }
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void getAllShequn() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", "3");
        HttpSender sender = new HttpSender(HttpUrl.allShequn, "全部社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseSheQunBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseSheQunBean.class);
                            if (obj != null) {
                                List<SheQunBean> list = obj.getList();
                                mQuestionGroupAdapter.setNewData(list);
                            }
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
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

    @OnClick({R.id.tv_create, R.id.tv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create:
                Intent intent = new Intent(mActivity, CreateGroupStepOneActivity.class);
                mActivityResultLauncher.launch(intent);
                break;
            case R.id.tv_more:

                break;
        }
    }
}
