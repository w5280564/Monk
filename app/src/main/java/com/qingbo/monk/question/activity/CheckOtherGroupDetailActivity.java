package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.CheckOtherGroupBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看未加入的社群详情
 */
public class CheckOtherGroupDetailActivity extends BaseActivity {
    @BindView(R.id.ll_container_bottom)
    LinearLayout llContainerBottom;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_group_id)
    TextView tvGroupId;
    @BindView(R.id.lable_Lin)
    LinearLayout tag_list;
    @BindView(R.id.tv_theme_num)
    TextView tv_theme_num;
    @BindView(R.id.tv_member_num)
    TextView tv_member_num;
    @BindView(R.id.tv_question_num)
    TextView tv_question_num;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_jieshao)
    TextView tvJieshao;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.iv_header_host)
    ImageView ivHeaderHost;
    @BindView(R.id.tv_group_host_name)
    TextView tvGroupHostName;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_fee_type)
    TextView tv_fee_type;
    @BindView(R.id.tv_pay_notice)
    TextView tv_pay_notice;
    private String id;
    private String fee_type = "1";

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
    }


    @Override
    protected void getServerData() {
        getShequnDetail();
    }

    /**
     * 未加入的社群详情
     */
    private void getShequnDetail() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.checkOtherGroupDetail, "未加入的社群详情", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            CheckOtherGroupBean groupBean = GsonUtil.getInstance().json2Bean(data, CheckOtherGroupBean.class);
                            handleData(groupBean);
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData(CheckOtherGroupBean groupBean) {
        if (groupBean != null) {
            tvGroupName.setText(groupBean.getShequnName());
            tvGroupId.setText(String.format("群ID：%s",groupBean.getSqId()));
            GlideUtils.loadRoundImage(mContext, ivHeader, groupBean.getAvatar(),R.mipmap.bg_create_group, DisplayUtil.dip2px(mContext,9));
            labelFlow(groupBean.getTags());
            tv_theme_num.setText(groupBean.getThemeCount());
            tv_member_num.setText(groupBean.getMemberCount());
            tv_question_num.setText(groupBean.getTopicCount());
            tvDes.setText(groupBean.getShequnDes());

            tvGroupHostName.setText(groupBean.getNickname());
            GlideUtils.loadRoundImage(mContext, ivHeaderHost, groupBean.getAvatar(), DisplayUtil.dip2px(mContext,9));
            tvCreateTime.setText(String.format("创建%1$s天，%2$s活跃过", groupBean.getCreateDay(), groupBean.getLastLogin()));

            fee_type = groupBean.getType();
            if ("0".equals(fee_type)) {
                tv_money.setText("免费");
                tv_fee_type.setText("免费");
                tv_money.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_1F8FE5));
                tv_fee_type.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_1F8FE5));
            }else{
                tv_money.setText("￥"+groupBean.getShequnFee());
                tv_fee_type.setText("￥"+groupBean.getShequnFee());
                tv_money.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_FC0000));
                tv_fee_type.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_FC0000));
            }

            tv_pay_notice.setText(groupBean.getPayNotice());
        }
    }

    public void labelFlow(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_tag, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, DisplayUtil.dip2px(mContext,8), 0);
            view.setLayoutParams(itemParams);
            TextView tv_tag = view.findViewById(R.id.tv_tag);
            tv_tag.setText(tagS[i]);
            tag_list.addView(view);
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


    @OnClick(R.id.tv_join)
    public void onClick() {
        if ("0".equals(fee_type)) {
            joinGroup();
        }else{
            T.ss("微信支付");
        }
    }

    /**
     * 加入社群
     */
    private void joinGroup() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.joinGroup, "加入社群", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("加入成功");
                            GroupDetailActivity.actionStart(mActivity, id);
                            finish();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }
}
