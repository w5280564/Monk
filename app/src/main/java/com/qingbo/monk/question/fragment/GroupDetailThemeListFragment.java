package com.qingbo.monk.question.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.bean.ThemeBean;
import com.qingbo.monk.question.adapter.GroupDetailThemeListAdapter;
import com.qingbo.monk.question.adapter.QuestionListAdapterMy;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * 预览主题列表
 */
public class GroupDetailThemeListFragment extends BaseLazyFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private String id;
    private GroupDetailThemeListAdapter mAdapter;

    public static GroupDetailThemeListFragment NewInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        GroupDetailThemeListFragment fragment = new GroupDetailThemeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_view;
    }


    @Override
    protected void initLocalData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            id = mBundle.getString("id");
        }
        registerEventBus();
    }

    @Subscribe
    public void onPublishSuccessEvent(FinishEvent event) {
        if(event.type == FinishEvent.CHOOSE_THEME){
            showTheme();
        }
    }

    @Override
    protected void initView(View mView) {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new GroupDetailThemeListAdapter();
        mAdapter.setEmptyView(addEmptyView("暂无主题", R.mipmap.zhuti));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ThemeBean mThemeBean = (ThemeBean) adapter.getItem(position);
                if (mThemeBean == null) {
                    return;
                }
                //删除
            }
        });


        mAdapter.setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });


    }



    @Override
    protected void loadData() {
        showTheme();
    }

    private void showTheme() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.showTheme, "预览主题", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleData(json);
                        }
                    }
                }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData(String json) {
        HttpBaseList<ThemeBean> objList = GsonUtil
                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<ThemeBean>>() {
                        }.getType());
        mAdapter.setNewData(objList.getData());
    }

}
