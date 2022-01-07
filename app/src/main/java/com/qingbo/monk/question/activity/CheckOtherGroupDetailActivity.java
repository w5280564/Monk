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
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.CheckOtherGroupBean;
import com.qingbo.monk.bean.ThemeBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.DisplayUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
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
            tvGroupId.setText(String.format("群ID：%s",id));
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
            createList(groupBean.getTheme());
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
     * 部分主题预览
     */
    private void createList(List<ThemeBean> mList) {
        if (!ListUtils.isEmpty(mList)) {
            llContainerBottom.removeAllViews();
            for (ThemeBean item:mList) {
                View itemView = LayoutInflater.from(mActivity).inflate(R.layout.item_group_detail_bottom, null);
                TextView tv_group_host_name = itemView.findViewById(R.id.tv_group_host_name);
                TextView tv_des = itemView.findViewById(R.id.tv_des);
                TextView tv_create_time = itemView.findViewById(R.id.tv_create_time);
                ImageView iv_header_host = itemView.findViewById(R.id.iv_header_host);
                LinearLayout ll_container_answer = itemView.findViewById(R.id.ll_container_answer);
                RecyclerView mNineView = itemView.findViewById(R.id.nine_grid);

                if (!TextUtils.isEmpty(item.getCreateTime())) {
                    String userDate = DateUtil.getUserDate(item.getCreateTime());
                    tv_create_time.setText(userDate);
                }



                String topicType = item.getTopicType();
                if ("1".equals(topicType)) {//1是话题2是问答
                    handleCommonData(item.getAvatar(),item.getNickname(),item.getContent()
                            ,iv_header_host,tv_group_host_name,tv_des);
                    handleImageList(item, mNineView);
                    ll_container_answer.setVisibility(View.GONE);
                }else{
                    List<ThemeBean.DetailDTO> details = item.getDetail();
                    if (!ListUtils.isEmpty(details)) {
                        ll_container_answer.setVisibility(View.VISIBLE);
                        ThemeBean.DetailDTO answerObj = details.get(0);
                        handleCommonData(answerObj.getAvatar(),answerObj.getNickname(),answerObj.getAnswerContent()
                                ,iv_header_host,tv_group_host_name,tv_des);
                        createQuestionList(ll_container_answer,item);
                    }else{
                        handleCommonData(item.getAvatar(),item.getNickname(),item.getContent()
                                ,iv_header_host,tv_group_host_name,tv_des);
                        handleImageList(item, mNineView);
                        ll_container_answer.setVisibility(View.GONE);
                    }
                }

                llContainerBottom.addView(itemView);
            }
        }
    }

    private void handleCommonData(String headImg,String headName,String content
            ,ImageView iv_headImg ,TextView tv_headName,TextView tv_content) {
        GlideUtils.loadCircleImage(mContext, iv_headImg, headImg);
        tv_headName.setText(headName);

        if (!StringUtil.isBlank(content)) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }else{
            tv_content.setVisibility(View.GONE);
        }
    }

    /**
     * 提问
     */
    private void createQuestionList(LinearLayout ll_container_answer, ThemeBean item ) {
        ll_container_answer.removeAllViews();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_answer, null);
        TextView tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
        TextView tv_answer_content = itemView.findViewById(R.id.tv_answer_content);
        RecyclerView mNineView = itemView.findViewById(R.id.nine_grid_answer);
        tv_answer_name.setText(item.getNickname());
        tv_answer_content.setText("提问："+item.getContent());

        handleImageList(item, mNineView);
        ll_container_answer.addView(itemView);
    }

    private void handleImageList(ThemeBean item, RecyclerView mNineView) {
        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        List<String> strings = new ArrayList<>();
        mNineView.setLayoutManager(new NineGridLayoutManager(mContext));
        mNineView.setAdapter(nineGridAdapter);
        //多张图片
        if (!TextUtils.isEmpty(item.getImages())) {
            mNineView.setVisibility(View.VISIBLE);
            String[] imgS = item.getImages().split(",");
            strings.addAll(Arrays.asList(imgS));
            nineGridAdapter.setNewData(strings);
            nineGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    if (onItemImgClickLister != null) {
//                        onItemImgClickLister.OnItemImgClickLister(position, strings);
//                    }
                }
            });
        } else {
            mNineView.setVisibility(View.GONE);
            nineGridAdapter.setNewData(null);
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
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.JOIN_GROUP));
                            GroupDetailActivity.actionStart(mActivity, id);
                            finish();
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }
}
