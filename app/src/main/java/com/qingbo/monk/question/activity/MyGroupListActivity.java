package com.qingbo.monk.question.activity;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.question.adapter.QuestionGroupAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import java.util.HashMap;
import butterknife.BindView;

/**
 * 我的社群问答
 */
public class MyGroupListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private QuestionGroupAdapterMy mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_group;
    }


    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        initRecyclerView();
    }


    private void initRecyclerView() {
        mAdapter = new QuestionGroupAdapterMy();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()==R.id.tv_join) {
                    MyGroupBean obj = (MyGroupBean) adapter.getItem(position);
                    if (obj==null) {
                        return;
                    }
                    GroupDetailActivity.actionStart(mActivity, obj.getId());
                }
            }
        });
    }

    @Override
    protected void getServerData() {
        getMyGroup();
    }

    private void getMyGroup() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.myGroup, "我的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleData(json_root);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }



    private void handleData(String json) {
        HttpBaseList<MyGroupBean> objList = GsonUtil
                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<MyGroupBean>>() {
                        }.getType());
        mAdapter.setNewData(objList.getData());

    }


}
