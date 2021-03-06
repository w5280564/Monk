package com.qingbo.monk.login.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.BaseHaveBean;
import com.qingbo.monk.bean.HaveBean;
import com.qingbo.monk.login.adapter.HaveAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第2步
 */
public class StepTwoFragmentLogin extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private HaveAdapter mHaveAdapter;
    private List<String> mChooseGetResourceList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_two_fragment_login;
    }

    @Override
    protected void getServerData() {
        getHaveList();
    }

    @Override
    protected void initView() {
        mHaveAdapter = new HaveAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(DisplayUtil.dip2px(mActivity, 23), getResources().getColor(R.color.app_background)));
        mRecyclerView.setAdapter(mHaveAdapter);
    }


    @Override
    protected void initEvent() {
        mHaveAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取收获列表
     */
    private void getHaveList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.harvestList, "获取收获列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseHaveBean mBaseHaveBean = GsonUtil.getInstance().json2Bean(json_data, BaseHaveBean.class);
                            if (mBaseHaveBean != null) {
                                handleData(mBaseHaveBean);
                            }
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(BaseHaveBean mBaseHaveBean) {
        mHaveAdapter.setNewData(mBaseHaveBean.getList());
    }

    @OnClick({R.id.tv_back, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ZERO));
                break;
            case R.id.tv_next:
                if (mChooseGetResourceList.size() < 3) {
                    T.ss("至少选择三个方向");
                    return;
                }
                if (mChooseGetResourceList.size() > 7){
                    T.s("方向不能多于7个", 3000);
                    return;
                }

                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_TWO, true, StringUtil.listToString(mChooseGetResourceList)));
                break;

        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HaveBean obj = (HaveBean) adapter.getItem(position);
        if (obj != null) {
            if (!obj.isCheck()) {
                mChooseGetResourceList.add(obj.getName());
                obj.setCheck(true);
            } else {
                mChooseGetResourceList.remove(obj.getName());
                obj.setCheck(false);
            }
            mHaveAdapter.notifyDataSetChanged();
        }

    }
}
