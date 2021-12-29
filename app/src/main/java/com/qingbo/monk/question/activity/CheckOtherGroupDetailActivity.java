package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 查看未加入的社群详情
 */
public class CheckOtherGroupDetailActivity extends BaseActivity {
    @BindView(R.id.ll_container_bottom)
    LinearLayout llContainerBottom;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.actiivty_check_other_group_detail;
    }

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, CheckOtherGroupDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        List<String> mList = new ArrayList<>();
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        mList.add("");
        createList(mList);
    }


    /**
     * 细看更多宝贝
     */
    private void createList(List<String> mList) {
        llContainerBottom.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_group_detail_bottom, null);
//            TextView tv_group_host_name = itemView.findViewById(R.id.tv_group_host_name);
//            TextView tv_des = itemView.findViewById(R.id.tv_des);
//            TextView tv_create_time = itemView.findViewById(R.id.tv_create_time);
//            ImageView iv_header_host = itemView.findViewById(R.id.iv_header_host);
            llContainerBottom.addView(itemView);
        }
    }

}
