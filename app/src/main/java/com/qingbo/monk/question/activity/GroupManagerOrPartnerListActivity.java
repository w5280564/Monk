package com.qingbo.monk.question.activity;

import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.ChooseMemberBean;
import com.qingbo.monk.question.adapter.ChooseMemberAdapter;
import com.xunda.lib.common.common.itemdecoration.GridDividerItemDecoration;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 社群管理员或合伙人
 */
public class GroupManagerOrPartnerListActivity extends BaseActivity {
    private List<ChooseMemberBean> memberList = new ArrayList<>();
    private ChooseMemberAdapter mAdapter;
    @BindView(R.id.mRecycleView)
    RecyclerView mRecycleView;


    @Override
    protected int getLayoutId() {
        return R.layout.group_manager_or_partner_list_activity;
    }

    @Override
    protected void initLocalData() {
        initFirstAddData();
        initImageRecyclerViewAndAdapter();
    }


    /**
     * 添加添加图片这个对象
     */
    private void initFirstAddData() {
        ChooseMemberBean bean = new ChooseMemberBean();
        bean.setType(1);
        memberList.add(bean);
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
            skipAnotherActivity(SetGroupManagerOrPartnerListActivity.class);
        }else if(memberList.get(position).getType() == 2){//踢人
            skipAnotherActivity(SetGroupManagerOrPartnerListActivity.class);
        }else{

        }
    }


}
