package com.qingbo.monk.person.activity;

import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.MyConstant;
import com.qingbo.monk.base.livedatas.LiveDataBus;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.bean.HomeSeekUser_Bean;
import com.qingbo.monk.bean.HomeSeekUser_ListBean;
import com.qingbo.monk.home.adapter.HomeSeek_User_Adapter;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.SearchEditText;

import java.util.HashMap;

import butterknife.BindView;

public class MyCrateArticle_At_Seek extends BaseRecyclerViewSplitActivity {

    @BindView(R.id.query_Edit)
    public SearchEditText query_Edit;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.mycrate_articleat_seek;
    }

    @Override
    protected void onRefreshData() {
        page = 1;
        String word = query_Edit.getText().toString();
        getExpertList(word, false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        String word = query_Edit.getText().toString();
        getExpertList(word, false);
    }

    @Override
    protected void initView() {
        HideIMEUtil.wrap(this, query_Edit);
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void initEvent() {
        query_Edit.addTextChangedListener(new query_EditTextChangeListener());
    }

    private class query_EditTextChangeListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getExpertList(s.toString(), false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    HomeSeekUser_ListBean homeSeekUser_listBean;

    public void getExpertList(String word, boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("word", word);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.Search_User, "搜索用户", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            homeSeekUser_listBean = GsonUtil.getInstance().json2Bean(json_data, HomeSeekUser_ListBean.class);
                            if (homeSeekUser_listBean != null) {
                                ((HomeSeek_User_Adapter) mAdapter).setFindStr(word);
                                handleSplitListData(homeSeekUser_listBean, mAdapter, limit);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void initRecyclerView() {
        mAdapter = new HomeSeek_User_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeSeekUser_Bean item = (HomeSeekUser_Bean) adapter.getItem(position);
            if (item == null) {
                return;
            }
            FriendContactBean friendContactBean = new FriendContactBean();
            friendContactBean.setId(item.getId());
            friendContactBean.setNickname(item.getNickname());
            friendContactBean.setDescription(item.getTagName());
            LiveDataBus.get().with(MyConstant.FRIEND_DATA).setValue(friendContactBean);
            finish();
        });

        mAdapter.setOnItemChildClickListener(new MyFollow_Adapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                HomeSeekUser_Bean item = (HomeSeekUser_Bean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String likeId = item.getId();
                        postFollowData(likeId, position);
                        break;
                    case R.id.send_Mes:
                        ChatActivity.actionStart(mActivity, item.getId(), item.getNickname(), item.getAvatar());
                        break;
                    case R.id.head_Img:
                        String id = item.getId();
                        MyAndOther_Card.actionStart(mActivity, id);
                        break;
                }
            }
        });
    }

    private void postFollowData(String otherUserId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
                    TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
                    ((HomeSeek_User_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}