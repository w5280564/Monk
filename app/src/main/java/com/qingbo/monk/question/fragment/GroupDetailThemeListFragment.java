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
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue_No_Finish;

import org.greenrobot.eventbus.EventBus;
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
    private String id,role;
    private GroupDetailThemeListAdapter mAdapter;

    public static GroupDetailThemeListFragment NewInstance(String id, String role) {
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("role", role);
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
            role = mBundle.getString("role");
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
        mAdapter = new GroupDetailThemeListAdapter(role);
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
                showToastDialog(mThemeBean.getArticleId(),position);

            }
        });


        mAdapter.setOnItemImgClickLister(new QuestionListAdapterMy.OnItemImgClickLister() {
            @Override
            public void OnItemImgClickLister(int position, List<String> strings) {
                jumpToPhotoShowActivity(position, strings);
            }
        });


    }

    private void showToastDialog(String id,int position) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(mActivity,"确定删除该条预览主题吗","取消","确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                editTheme(id,position);
            }

            @Override
            public void onClickLeft() {
            }
        });
        mDialog.show();
    }

    private void editTheme(String id,int position) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("status", "0");
        HttpSender sender = new HttpSender(HttpUrl.editTheme, "编辑主题", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int code, String description, String data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            mAdapter.remove(position);
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
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
