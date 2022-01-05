package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMemberBean;
import com.qingbo.monk.bean.GroupMemberListBean;
import com.qingbo.monk.dialog.SetManagerDialog;
import com.qingbo.monk.question.adapter.GroupMemberListAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群成员列表
 */
public class GroupMemberListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_bar_center_txt)
    TextView title_tv;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;
    private String id;
    private List<GroupMemberBean> allMemberList = new ArrayList<>();
    private List<GroupMemberBean> resultMemberList = new ArrayList<>();
    private GroupMemberListAdapter mGroupMemberListAdapter;

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupMemberListActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_member_list;
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }



    public void initRecyclerView() {
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        mGroupMemberListAdapter = new GroupMemberListAdapter();
        mRecyclerView.setAdapter(mGroupMemberListAdapter);
    }

    @Override
    protected void getServerData() {
        groupUserList();
    }

    /**
     * 群成员列表
     */
    private void groupUserList() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", "1");
        HttpSender sender = new HttpSender(HttpUrl.groupUserList, "群成员列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            GroupMemberListBean obj = GsonUtil.getInstance().json2Bean(json_data, GroupMemberListBean.class);
                            if (obj==null) {
                                return;
                            }

                            handleData(obj);
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(GroupMemberListBean obj) {
        allMemberList.addAll(obj.getList());
        mGroupMemberListAdapter.setNewData(allMemberList);
        title_tv.setText(String.format("群成员（%s）",obj.getCount()));
    }

    @Override
    protected void initEvent() {
        mGroupMemberListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GroupMemberBean obj = (GroupMemberBean) adapter.getItem(position);
                if (obj==null) {
                    return;
                }
                showSetManagerDialog(obj,position);
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
                if(!StringUtil.isBlank(arg0.toString())){
                    ll_cancel.setVisibility(View.VISIBLE);
                }else{
                    ll_cancel.setVisibility(View.GONE);
                }
                searchMemberList();
            }
        });
    }


    private void searchMemberList() {
        String searchStr = et_search.getText().toString().trim();
        if (StringUtil.isBlank(searchStr)) {
            mGroupMemberListAdapter.setNewData(allMemberList);
        }else{
            getMemberListByName(searchStr);
        }
    }

    private void getMemberListByName(String searchStr) {
        resultMemberList.clear();
        if (!ListUtils.isEmpty(allMemberList)) {
            for (GroupMemberBean mObj : allMemberList) {
                if (mObj.getNickname().contains(searchStr)) {
                    resultMemberList.add(mObj);
                }
            }
            mGroupMemberListAdapter.setNewData(resultMemberList);
        }
    }

    private void showSetManagerDialog(GroupMemberBean obj,int position) {
        SetManagerDialog mSetManagerDialog = new SetManagerDialog(this,obj, new SetManagerDialog.ConfirmListener() {
            @Override
            public void onSet(String user_id,String submit_type,String current_personal_role) {
                String content ="";
                if("2".equals(current_personal_role)) {//1管理员2合伙人0一般用户3群主
                    content = "确定取消该成员的合伙人身份吗？";
                }else if("1".equals(current_personal_role)){
                    content = "确定取消该成员的管理员身份吗？";
                }else{
                    content = "确定把该成员设置成管理员吗？";
                }
                showCancelRoleDialog(user_id,submit_type,position,content);
            }
        });
        mSetManagerDialog.show();
    }

    private void showCancelRoleDialog(String user_id,String submit_type,int position,String content) {
        TwoButtonDialogBlue mDialog = new TwoButtonDialogBlue(this,content,"取消","确定", new TwoButtonDialogBlue.ConfirmListener() {
            @Override
            public void onClickRight() {
                setAdmins(user_id,submit_type,position);
            }

            @Override
            public void onClickLeft() {

            }
        });
        mDialog.show();
    }

    private void setAdmins(String user_id,String submit_type,int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", submit_type);
        requestMap.put("uids", user_id);
        HttpSender sender = new HttpSender(HttpUrl.setAdmins, "设置/取消管理员/合伙人", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            ImageView iv_more = (ImageView) mGroupMemberListAdapter.getViewByPosition(mRecyclerView, position, R.id.iv_more);
                            TextView tv_role = (TextView) mGroupMemberListAdapter.getViewByPosition(mRecyclerView, position, R.id.tv_role);
                            if ("3".equals(submit_type)) {
                                iv_more.setVisibility(View.VISIBLE);
                                tv_role.setVisibility(View.GONE);
                                mGroupMemberListAdapter.getItem(position).setRole("0");
                            }else if("1".equals(submit_type)){
                                iv_more.setVisibility(View.VISIBLE);
                                tv_role.setVisibility(View.VISIBLE);
                                tv_role.setText("管理员");
                                mGroupMemberListAdapter.getItem(position).setRole("1");
                            }
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendPost();
    }


    @OnClick({R.id.title_bar_left,R.id.ll_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                back();
                break;
            case R.id.ll_cancel:
                et_search.setText("");
                ll_cancel.setVisibility(View.GONE);
                break;

        }
    }

}
