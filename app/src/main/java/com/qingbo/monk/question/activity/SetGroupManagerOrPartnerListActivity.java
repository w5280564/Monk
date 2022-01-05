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
import com.qingbo.monk.bean.AskQuestionBean;
import com.qingbo.monk.bean.GroupMemberBean;
import com.qingbo.monk.bean.GroupMemberBigBean;
import com.qingbo.monk.bean.GroupMemberListBean;
import com.qingbo.monk.question.adapter.GroupManagerOrPartnerAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加或删除社区管理员或合伙人
 */
public class SetGroupManagerOrPartnerListActivity extends BaseActivity {
    @BindView(R.id.ll_main_container)
    LinearLayout ll_main_container;
    @BindView(R.id.ll_header_container)
    LinearLayout ll_header_container;
    @BindView(R.id.sideBarLayout)
    SideBarLayout sideBarLayout;
    private String id,type;//3是管理员 4是合伙人
    private List<GroupMemberBean> mChooseList = new ArrayList<>();

    public static void actionStart(Context context, String id, String type) {
        Intent intent = new Intent(context, SetGroupManagerOrPartnerListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manager_or_partner_list_set;
    }





    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
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



    private void createMainList(List<GroupMemberBigBean> mList) {
        if (!ListUtils.isEmpty(mList)) {
            ll_main_container.removeAllViews();
            for (GroupMemberBigBean mTempObj:mList) {
                View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_group_member_list_main, null);
                TextView header = itemView.findViewById(R.id.header);
                header.setText(mTempObj.getFirstLetter());
                RecyclerView mRecyclerView = itemView.findViewById(R.id.mRecyclerView);
                initRecyclerView(mRecyclerView,mTempObj.getChildlist());
                ll_main_container.addView(itemView);
            }
        }

    }

    public void initRecyclerView(RecyclerView mRecyclerView,List<GroupMemberBean> childList) {
        LinearLayoutManager mManager  = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setHasFixedSize(true);
        GroupManagerOrPartnerAdapter mGroupMemberListAdapter = new GroupManagerOrPartnerAdapter();
        mRecyclerView.setAdapter(mGroupMemberListAdapter);
        mGroupMemberListAdapter.setNewData(childList);

        mGroupMemberListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupMemberBean obj = (GroupMemberBean) adapter.getItem(position);
                if (obj==null) {
                    return;
                }
                if (!obj.isCheck()) {
                    mChooseList.add(obj);
                    obj.setCheck(true);
                    addHeaderView(obj,mGroupMemberListAdapter);
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
//                //根据自己业务实现
//                for (int i = 0; i < mList.size(); i++) {
//                    if (mList.get(i).getWord().equals(word)) {
//                        recyclerView.smoothScrollToPosition(i);
//                        break;
//                    }
//                }
            }
        });

    }


    /**
     * 取消选中
     */
    private void deleteHeaderView(GroupMemberBean obj) {
        for (int i = 0; i < mChooseList.size(); i++) {
            if (mChooseList.get(i).getId().equals(obj.getId())) {
                ll_header_container.removeViewAt(i);
            }
        }
    }

    /**
     * 选中
     */
    private void addHeaderView(GroupMemberBean obj,GroupManagerOrPartnerAdapter mGroupMemberListAdapter) {
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
}
