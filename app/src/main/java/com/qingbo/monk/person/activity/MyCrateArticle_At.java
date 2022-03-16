package com.qingbo.monk.person.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.lzj.sidebar.SideBarLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.MyConstant;
import com.qingbo.monk.base.baseview.AtEditText;
import com.qingbo.monk.base.livedatas.LiveDataBus;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.bean.FriendContactBigBean;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.message.adapter.ContactListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 发布动态-at好友列表
 */
public class MyCrateArticle_At extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.seek_ll)
    LinearLayout seek_ll;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.sideBarLayout)
    SideBarLayout sideBarLayout;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;

    private ContactListAdapter mContactListAdapter;
    private List<FriendContactBean> mList = new ArrayList();
    private List<FriendContactBean> resultMemberList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_at;
    }

    @Override
    protected void initView() {
        title_bar.setTitle("选择@的人");
        mContactListAdapter = new ContactListAdapter(mList);
        mContactListAdapter.setEmptyView(addEmptyView("暂无好友", 0));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mContactListAdapter);
    }


    @Override
    protected void getServerData() {
        friendList();
    }


    /**
     * 好友列表
     */
    private void friendList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.friendList, "好友列表", requestMap,
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
        HttpBaseList<FriendContactBigBean> objList = GsonUtil
                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<FriendContactBigBean>>() {
                        }.getType());
        createMainList(objList.getData());
    }


    private void createMainList(List<FriendContactBigBean> tempList) {
        if (!ListUtils.isEmpty(tempList)) {
            sideBarLayout.setVisibility(View.VISIBLE);
            for (FriendContactBigBean obj : tempList) {
                String letter = obj.getFirstLetter();
                FriendContactBean mLetterObj = new FriendContactBean();
                mLetterObj.setFirstLetter(StringUtil.isBlank(letter) ? "#" : letter);
                mLetterObj.setItemType(1);
                mList.add(mLetterObj);

                List<FriendContactBean> memberList = obj.getChildlist();
                if (!ListUtils.isEmpty(memberList)) {
                    for (FriendContactBean mMemberObj : memberList) {
                        mMemberObj.setFirstLetter(letter);
                        mMemberObj.setItemType(0);
                    }
                    mList.addAll(memberList);
                }
            }
        } else {
            sideBarLayout.setVisibility(View.GONE);
        }
        mContactListAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initEvent() {
        sideBarLayout.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                for (int i = 0; i < mList.size(); i++) {
                    if (word.equals(mList.get(i).getFirstLetter())) {
                        mRecyclerView.scrollToPosition(i);
                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();//必不可少
                        mLayoutManager.scrollToPositionWithOffset(i, 0);
                        break;
                    }
                }
            }
        });


        mContactListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FriendContactBean obj = (FriendContactBean) adapter.getItem(position);
                if (obj == null) {
                    return;
                }

                if (obj.getItemType() == 1) {
                    return;
                }
                LiveDataBus.get().with(MyConstant.FRIEND_DATA).setValue(obj);
                finish();
            }
        });

        seek_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seek_ll:
                skipAnotherActivity(MyCrateArticle_At_Seek.class);
                finish();
                break;
        }
    }


}
