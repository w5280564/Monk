package com.qingbo.monk.Slides.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.Character_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.bean.CharacterList_Bean;
import com.qingbo.monk.bean.Character_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 侧边栏 人物
 */
public class SideslipPersonList_Activity extends BaseRecyclerViewSplitActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;
    @BindView(R.id.query_Edit)
    SearchEditText query_Edit;
    private String nickname = "";

    /**
     * @param context
     */
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SideslipPersonList_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.slideslippersonlist_activity;
    }


    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.card_Recycler);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无人物", 0, false);
    }

    @Override
    protected void initEvent() {
        query_Edit.addTextChangedListener(new query_EditChangeListener());
        query_Edit.setOnEditorActionListener(new OnEditorLister());
    }


    @Override
    protected void getServerData() {
        getListData(true, nickname);
    }

    private void getListData(boolean isShow, String nickname) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("nickname", nickname);
        HttpSender httpSender = new HttpSender(HttpUrl.Character_List, "人物列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CharacterList_Bean characterList_bean = GsonUtil.getInstance().json2Bean(json_data, CharacterList_Bean.class);
                    handleSplitListData(characterList_bean, mAdapter, limit);

                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    @Override
    protected void onRefreshData() {

    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false, nickname);
    }


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new Character_Adapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Character_Bean item = (Character_Bean) adapter.getItem(position);
                String nickname = item.getNickname();
                String id = item.getId();
                SideslipPersonDetail_Activity.startActivity(mActivity, nickname, id, "0");
            }
        });
    }

    private class query_EditChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String keyword = s.toString();
            nickname = keyword;
            getListData(true, nickname);
        }
    }

    private class OnEditorLister implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        }
    }


}