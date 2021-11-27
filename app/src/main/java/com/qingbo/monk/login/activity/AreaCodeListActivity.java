package com.qingbo.monk.login.activity;


import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MiddleAreaCodeBean;
import com.qingbo.monk.bean.BigAreaCodeBean;
import com.qingbo.monk.login.adapter.AreaCodeListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.PinnedSectionListView;
import com.xunda.lib.common.view.SideBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 区号列表
 */
public class AreaCodeListActivity extends BaseActivity implements AreaCodeListAdapter.OnChooseItemListener {
    private List<MiddleAreaCodeBean> mList = new ArrayList<>();
    private AreaCodeListAdapter mAdapter;
    private ListView mListView;
    private SideBar mSidBar;
    private TextView mDialogTextView;//中部展示的字母提示



    @Override
    protected int getLayoutId() {
        return R.layout.activity_code_list;
    }

    @Override
    protected void initView() {
        mListView = findViewById(R.id.listview);
        mSidBar = findViewById(R.id.sidrbar);
        mDialogTextView = findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
        mAdapter = new AreaCodeListAdapter(mActivity, mList,this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void getServerData() {
        getAreaCodeList();
    }

    @Override
    protected void initEvent() {
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }


    /**
     * 获取区号列表
     */
    private void getAreaCodeList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.getAreaCodeList, "获取区号列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BigAreaCodeBean obj = GsonUtil.getInstance().json2Bean(json_data, BigAreaCodeBean.class);
                            handleData(obj);
                        } else {
                            T.ss(msg);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData(BigAreaCodeBean obj) {
        if (obj!=null) {
            if (!ListUtils.isEmpty(obj.getList())) {
                mSidBar.setVisibility(View.VISIBLE);
                mList.clear();
                mList.addAll(obj.getList());
            } else {
                mList.clear();
                mSidBar.setVisibility(View.GONE);
            }

            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void chooseItem(String code) {

    }
}
