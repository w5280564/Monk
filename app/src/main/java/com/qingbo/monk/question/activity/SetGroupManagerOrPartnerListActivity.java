package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
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
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加或删除社区管理员或合伙人
 */
public class SetGroupManagerOrPartnerListActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_header_container)
    LinearLayout ll_header_container;
    @BindView(R.id.sideBarLayout)
    SideBarLayout sideBarLayout;
    private String id,type;//3是管理员 4是合伙人
    private int edit_type;//1添加 2删除
    private String getRequestType,setRequestType;
    private List<GroupMemberBean> mChooseList = new ArrayList<>();
    private List<GroupMemberBean> mList = new ArrayList();
    private GroupManagerOrPartnerBigAdapter mGroupMemberListAdapter;

    public static void actionStart(Context context, String id, String type,int edit_type) {
        Intent intent = new Intent(context, SetGroupManagerOrPartnerListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("edit_type", edit_type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_or_partner_list_set;
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
        type = getIntent().getStringExtra("type");
        edit_type = getIntent().getIntExtra("edit_type",1);

        if (edit_type==1) {//添加
            getRequestType = "1";
            if ("3".equals(type)) {//管理员
                setRequestType = "1";
                title = "添加管理员";
            }else{
                setRequestType = "2";
                title = "添加合伙人";
            }
        }else{//删除
            setRequestType = "3";
            if ("3".equals(type)) {//管理员
                getRequestType = "2";
                title = "删除管理员";
            }else{
                getRequestType = "3";
                title = "删除合伙人";
            }
        }
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
        requestMap.put("type", getRequestType);
        HttpSender sender = new HttpSender(HttpUrl.commonUser, "群成员列表", requestMap,
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
                if(!StringUtil.isBlank(letter)){
                    GroupMemberBean mLetterObj = new GroupMemberBean();
                    mLetterObj.setFirstLetter(letter);
                    mLetterObj.setItemType(1);
                    mList.add(mLetterObj);
                }
                List<GroupMemberBean> memberList = obj.getChildlist();
                if(!ListUtils.isEmpty(memberList)){
                    for (GroupMemberBean mMemberObj:memberList) {
                        mMemberObj.setFirstLetter(letter);
                        mMemberObj.setItemType(0);
                    }
                    mList.addAll(memberList);
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

//        sideBarLayout.OnItemScrollUpdateText(mList.get(firstItemPosition).getWord());

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
            T.ss("请选择群成员");
            return;
        }
        List<String> mSubmitList = new ArrayList();
        for (GroupMemberBean submitObj:mChooseList) {
            mSubmitList.add(submitObj.getId());
        }

        setAdmins(StringUtil.listToString(mSubmitList));
    }

    private void setAdmins(String user_id) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", setRequestType);//	1添加管理员 2添加合伙人 3设置为一般用户
        requestMap.put("uids", user_id);
        HttpSender sender = new HttpSender(HttpUrl.setAdmins, "设置/取消管理员/合伙人", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.EDIT_MANAGER_PARTNER));
                            back();
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendPost();
    }
}
