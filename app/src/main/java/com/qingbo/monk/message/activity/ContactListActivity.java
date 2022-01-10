package com.qingbo.monk.message.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.lzj.sidebar.SideBarLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.FriendContactBean;
import com.qingbo.monk.bean.FriendContactBigBean;
import com.qingbo.monk.message.adapter.ContactListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * 通讯录列表
 */
public class ContactListActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.sideBarLayout)
    SideBarLayout sideBarLayout;
    private ContactListAdapter mContactListAdapter;
    private List<FriendContactBean> mList = new ArrayList();
    private List<FriendContactBean> resultMemberList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_list;
    }

    @Override
    protected void initView() {
        mContactListAdapter = new ContactListAdapter(mList);
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
            for (FriendContactBigBean obj:tempList) {
                String letter = obj.getFirstLetter();
                FriendContactBean mLetterObj = new FriendContactBean();
                mLetterObj.setFirstLetter(StringUtil.isBlank(letter)?"#":letter);
                mLetterObj.setItemType(1);
                mList.add(mLetterObj);

                List<FriendContactBean> memberList = obj.getChildlist();
                if(!ListUtils.isEmpty(memberList)){
                    for (FriendContactBean mMemberObj:memberList) {
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
                if (obj==null) {
                    return;
                }

                if (obj.getItemType()==1) {
                    return;
                }

                ChatActivity.actionStart(mActivity,obj.getId(),obj.getNickname(),obj.getAvatar());
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {//打开软键盘
                    imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                }
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchMemberList();
                }
                return false;
            }
        });

        addEditTextListener();
    }


    /**
     * 给editext添加监听
     */
    private void addEditTextListener() {
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                searchMemberList();
            }
        });
    }


    private void searchMemberList() {
        String searchStr = et_search.getText().toString().trim();
        if (StringUtil.isBlank(searchStr)) {
            mContactListAdapter.setNewData(mList);
        }else{
            getMemberListByName(searchStr);
        }
    }

    private void getMemberListByName(String searchStr) {
        resultMemberList.clear();
        if (!ListUtils.isEmpty(mList)) {
            for (FriendContactBean mObj : mList) {
                String nickname = mObj.getNickname();
                if (!StringUtil.isBlank(nickname)) {
                    if (nickname.contains(searchStr)) {
                        resultMemberList.add(mObj);
                    }
                }
            }
            mContactListAdapter.setNewData(resultMemberList);
        }
    }

}
