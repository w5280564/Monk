package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMemberBean;
import com.qingbo.monk.bean.GroupMemberListBean;
import com.qingbo.monk.question.adapter.ChooseMemberAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * 社群管理员或合伙人
 */
public class GroupManagerOrPartnerListActivity extends BaseActivity {
    private List<GroupMemberBean> memberList = new ArrayList<>();
    private ChooseMemberAdapter mAdapter;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_label)
    TextView tv_label;
    private String id,type;//3是管理员 4是合伙人

    public static void actionStart(Context context, String id,String type) {
        Intent intent = new Intent(context, GroupManagerOrPartnerListActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.group_manager_or_partner_list_activity;
    }

    @Override
    protected void initView() {
        registerEventBus();
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if(event.type == FinishEvent.EDIT_MANAGER_PARTNER){
            memberList.clear();
            initFirstAddData();
            groupUserList();
        }
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if ("3".equals(type)) {
            title = "管理员";
            tv_label.setText("添加管理员");
        }else{
            title = "合伙人";
            tv_label.setText("添加合伙人");
        }
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
    }


    /**
     * 添加添加图片这个对象
     */
    private void initFirstAddData() {
        GroupMemberBean beanAdd = new GroupMemberBean();
        beanAdd.setType(1);
        memberList.add(beanAdd);
        GroupMemberBean beanDelete = new GroupMemberBean();
        beanDelete.setType(2);
        memberList.add(beanDelete);
    }


    private void initImageRecyclerViewAndAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        GridDividerItemDecoration mItemDecoration = new GridDividerItemDecoration(false, DisplayUtil.sp2px(mContext,20),getResources().getColor(R.color.white));
        mRecycleView.addItemDecoration(mItemDecoration);
        mRecycleView.setLayoutManager(layoutManager);
        mAdapter = new ChooseMemberAdapter(memberList);
        mRecycleView.setAdapter(mAdapter);
    }


    @Override
    protected void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickItem(position);
            }
        });
    }

    /**
     * 点击item
     */
    private void clickItem(int position) {
        if (memberList.get(position).getType() == 1) {//添加
            SetGroupManagerOrPartnerListActivity.actionStart(mActivity,id,type,1,12-mAdapter.getData().size());
        }else if(memberList.get(position).getType() == 2){//踢人
            SetGroupManagerOrPartnerListActivity.actionStart(mActivity,id,type,2);
        }
    }


    @Override
    protected void getServerData() {
        groupUserList();
    }


    /**
     * 群管理员/合伙人列表
     */
    private void groupUserList() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", type);//3是管理员 4是合伙人
        HttpSender sender = new HttpSender(HttpUrl.groupUserList, "群管理员/合伙人列表", requestMap,
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
        memberList.addAll(0,obj.getList());
        mAdapter.notifyDataSetChanged();
    }

}
