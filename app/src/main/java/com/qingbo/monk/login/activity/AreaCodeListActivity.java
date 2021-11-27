package com.qingbo.monk.login.activity;


import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.BigAreaCodeBean;
import com.qingbo.monk.bean.MiddleAreaCodeBean;
import com.qingbo.monk.bean.SmallAreaCodeBean;
import com.qingbo.monk.login.adapter.AreaCodeAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.SideBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 区号列表
 */
public class AreaCodeListActivity extends BaseActivity implements AreaCodeAdapter.OnChooseItemListener {
    private List<SmallAreaCodeBean> mList = new ArrayList<>();
    private AreaCodeAdapter mAdapter;
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


    private void handleData(BigAreaCodeBean bigObj) {
        if (bigObj!=null) {
            if (!ListUtils.isEmpty(bigObj.getList())) {
                mSidBar.setVisibility(View.VISIBLE);
                mList.clear();
                for (MiddleAreaCodeBean obj:bigObj.getList()) {
                    String letter = obj.getFirstLetter();
                    if(!StringUtil.isBlank(letter)){
                        SmallAreaCodeBean mLetterObj = new SmallAreaCodeBean();
                        mLetterObj.setArea(letter);
                        mLetterObj.setClassification(1);
                        mList.add(mLetterObj);
                    }
                    List<SmallAreaCodeBean> codeList = obj.getChildlist();
                    if(!ListUtils.isEmpty(codeList)){
                        for (SmallAreaCodeBean mCodeObj:codeList) {
                            mCodeObj.setClassification(2);
                        }
                        mList.addAll(codeList);
                    }
                }
                mAdapter = new AreaCodeAdapter(mActivity, mList,this);
                mListView.setAdapter(mAdapter);
            } else {
                mSidBar.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onChooseItem(String code) {
        T.ss(code);
        finish();
    }
}
