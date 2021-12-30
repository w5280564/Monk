package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.CheckOtherGroupBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 预览社群详情
 */
public class PreviewGroupDetailActivity extends BaseActivity {
    @BindView(R.id.ll_container_bottom)
    LinearLayout llContainerBottom;
    @BindView(R.id.iv_group_bag)
    ImageView ivGroupBag;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_host_des)
    TextView tvHostDes;
    @BindView(R.id.iv_header_host)
    ImageView ivHeaderHost;
    @BindView(R.id.tv_group_host_name)
    TextView tvGroupHostName;
    @BindView(R.id.tv_group_des)
    TextView tvGroupDes;
    @BindView(R.id.tv_time_online)
    TextView tvTimeOnline;
    @BindView(R.id.tv_jieshao)
    TextView tvJieshao;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.view_line1)
    View viewLine1;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.actiivty_preview_group_detail;
    }

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, PreviewGroupDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void getServerData() {
        getPreviewGroupDetail();
    }


    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }

    /**
     * 未加入的社群详情
     */
    private void getPreviewGroupDetail() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.getPreviewGroupDetail, "社群预览", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
//                            CheckOtherGroupBean groupBean = GsonUtil.getInstance().json2Bean(data, CheckOtherGroupBean.class);
//                            handleData(groupBean);
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(CheckOtherGroupBean groupBean) {
        if (groupBean != null) {
//            tvGroupName.setText(groupBean.getShequnName());
//            tvGroupId.setText(String.format("群ID：%s",groupBean.getSqId()));
//            GlideUtils.loadRoundImage(mContext, ivHeader, groupBean.getAvatar(),R.mipmap.bg_create_group, DisplayUtil.dip2px(mContext,9));
//            tv_tag.setText(groupBean.getTags());
//            tv_theme_num.setText(groupBean.getThemeCount());
//            tv_member_num.setText(groupBean.getMemberCount());
//            tv_question_num.setText(groupBean.getTopicCount());
//            tvDes.setText(groupBean.getShequnDes());
//
//            tvGroupHostName.setText(groupBean.getNickname());
//            GlideUtils.loadRoundImage(mContext, ivHeaderHost, groupBean.getAvatar(), DisplayUtil.dip2px(mContext,9));
//            tvCreateTime.setText(String.format("创建%1$s天，%2$s活跃过", groupBean.getCreateDay(), groupBean.getLastLogin()));
//
//            fee_type = groupBean.getType();
//            if ("0".equals(fee_type)) {
//                tv_money.setText("免费");
//                tv_fee_type.setText("免费");
//                tv_money.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_1F8FE5));
//                tv_fee_type.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_1F8FE5));
//            }else{
//                tv_money.setText("￥"+groupBean.getShequnFee());
//                tv_fee_type.setText("￥"+groupBean.getShequnFee());
//                tv_money.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_FC0000));
//                tv_fee_type.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_FC0000));
//            }
//
//            tv_pay_notice.setText(groupBean.getPayNotice());
        }
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
