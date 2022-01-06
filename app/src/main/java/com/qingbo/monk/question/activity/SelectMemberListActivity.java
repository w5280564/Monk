package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.qingbo.monk.bean.GroupMemberBean;
import com.qingbo.monk.bean.GroupMemberBigBean;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerBigAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择好友
 */
public class SelectMemberListActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_header_container)
    LinearLayout ll_header_container;
    @BindView(R.id.sideBarLayout)
    SideBarLayout sideBarLayout;
    private String id;//
    private int type;
    private List<GroupMemberBean> mChooseList = new ArrayList<>();
    private List<GroupMemberBean> mList = new ArrayList();
    private List<GroupMemberBean> resultMemberList = new ArrayList<>();
    private GroupManagerOrPartnerBigAdapter mGroupMemberListAdapter;

    public static void actionStart(Context context, String id, int type) {
        Intent intent = new Intent(context, SelectMemberListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_member_list;
    }


    @Override
    protected void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager mManager  = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mGroupMemberListAdapter = new GroupManagerOrPartnerBigAdapter(mList);
        mGroupMemberListAdapter.setEmptyView(addEmptyView("暂无数据",0));
        mRecyclerView.setAdapter(mGroupMemberListAdapter);
    }


    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",1);
    }

    @Override
    protected void getServerData() {
        followMutualList();
    }


    /**
     * 群主相互关注的列表
     */
    private void followMutualList() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.followMutualList, "群主相互关注的列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleData(json_root);
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }

    private void handleData(String json) {
        HttpBaseList<GroupMemberBigBean> objList = GsonUtil
                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<GroupMemberBigBean>>() {
                        }.getType());
        createMainList(objList.getData());
    }



    private void createMainList(List<GroupMemberBigBean> tempList) {
        if (!ListUtils.isEmpty(tempList)) {
            sideBarLayout.setVisibility(View.VISIBLE);
            for (GroupMemberBigBean obj:tempList) {
                String letter = obj.getFirstLetter();
                GroupMemberBean mLetterObj = new GroupMemberBean();
                mLetterObj.setFirstLetter(StringUtil.isBlank(letter)?"#":letter);
                mLetterObj.setItemType(1);
                mList.add(mLetterObj);

                List<GroupMemberBean> memberList = obj.getChildlist();
                if(!ListUtils.isEmpty(memberList)){
                    for (GroupMemberBean mMemberObj:memberList) {
                        if ("-1".equals(mMemberObj.getRole())) {//role=-1的才能邀请
                            mMemberObj.setFirstLetter(letter);
                            mMemberObj.setItemType(0);
                            mList.add(mMemberObj);
                        }else{
                            if (memberList.size()==1) {
                                mLetterObj.setLetterShow(1);
                            }
                        }
                    }
                }
            }
        } else {
            sideBarLayout.setVisibility(View.GONE);
        }
        mGroupMemberListAdapter.notifyDataSetChanged();
    }




    @OnClick({R.id.et_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                break;

        }
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


        mGroupMemberListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupMemberBean obj = (GroupMemberBean) adapter.getItem(position);
                if (obj==null) {
                    return;
                }
                if (obj.getItemType()==1) {
                    return;
                }

                if (!obj.isCheck()) {
                    obj.setCheck(true);
                    mChooseList.add(obj);
                    addHeaderView(obj);
                }else{
                    obj.setCheck(false);
                    deleteHeaderView(obj);
                    mChooseList.remove(obj);
                }
                titleBar.setRightText(mChooseList.size()==0?"完成":String.format("完成(%s)",mChooseList.size()));
                mGroupMemberListAdapter.notifyDataSetChanged();
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
     * 取消选中
     */
    private void deleteHeaderView(GroupMemberBean obj) {
        for (int i = 0; i < mChooseList.size(); i++) {
            String tempId = mChooseList.get(i).getId();
            String currentId = obj.getId();
            if (!StringUtil.isBlank(tempId)&&!StringUtil.isBlank(currentId)) {
                if (tempId.equals(currentId)) {
                    ll_header_container.removeViewAt(i);
                    break;
                }
            }
        }
    }



    /**
     * 选中
     */
    private void addHeaderView(GroupMemberBean obj) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_group_member_list_top, null);
        ImageView iv_header = itemView.findViewById(R.id.iv_header);
        GlideUtils.loadCircleImage(mContext, iv_header, obj.getAvatar());
        ll_header_container.addView(itemView);

        iv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj.setCheck(false);
                deleteHeaderView(obj);
                mChooseList.remove(obj);
                titleBar.setRightText(mChooseList.size()==0?"完成":String.format("完成(%s)",mChooseList.size()));
                mGroupMemberListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onRightClick() {
        if (ListUtils.isEmpty(mChooseList)) {
            T.ss("请选择好友");
            return;
        }
        List<String> mSubmitList = new ArrayList();
        for (GroupMemberBean submitObj:mChooseList) {
            mSubmitList.add(submitObj.getId());
        }

        invite(StringUtil.listToString(mSubmitList));
    }

    private void invite(String user_id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("user_id", user_id);
        HttpSender sender = new HttpSender(HttpUrl.invite, "邀请好友", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            showCommonToastDialog("邀请成功",1);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendPost();
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
            mGroupMemberListAdapter.setNewData(mList);
        }else{
            getMemberListByName(searchStr);
        }
    }

    private void getMemberListByName(String searchStr) {
        resultMemberList.clear();
        if (!ListUtils.isEmpty(mList)) {
            for (GroupMemberBean mObj : mList) {
                String nickname = mObj.getNickname();
                if (!StringUtil.isBlank(nickname)) {
                    if (nickname.contains(searchStr)) {
                        resultMemberList.add(mObj);
                    }
                }
            }
            mGroupMemberListAdapter.setNewData(resultMemberList);
        }
    }
}
