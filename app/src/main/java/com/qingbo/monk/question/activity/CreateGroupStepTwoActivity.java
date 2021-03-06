package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.DecimalInputTextWatcherExtract;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.RadiusImageWidget;
import com.xunda.lib.common.view.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import java.util.HashMap;
import butterknife.BindView;

/**
 * 创建社群第二步
 */
public class CreateGroupStepTwoActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.iv_header_group)
    RoundImageView ivHeaderGroup;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_group_type)
    TextView tvGroupType;
    @BindView(R.id.tv_money_logo)
    TextView tvMoneyLogo;
    @BindView(R.id.et_price_number)
    EditText etPriceNumber;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_beyond_below_toast)
    TextView tv_beyond_below_toast;
    private String group_name,group_header;
    private boolean isFree;

    public static void actionStart(Context context, String group_name, String group_header) {
        Intent intent = new Intent(context, CreateGroupStepTwoActivity.class);
        intent.putExtra("group_name",group_name);
        intent.putExtra("group_header",group_header);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_group_step_two;
    }


    @Override
    protected void initLocalData() {
        group_name = getIntent().getStringExtra("group_name");
        group_header = getIntent().getStringExtra("group_header");
        tvName.setText(StringUtil.getStringValue(group_name));
        if (StringUtil.isBlank(group_header)) {
            ivHeaderGroup.setImageResource(R.mipmap.bg_create_group);
        }else{
            ImgLoader.getInstance().displayCrop(mContext,ivHeaderGroup,group_header,R.mipmap.bg_group_top);
        }

    }

    @Override
    protected void initEvent() {
        cbAgree.setOnCheckedChangeListener(this);
        addEditTextListener_money();
    }

    /**
     * 给editext添加监听
     */
    private void addEditTextListener_money() {
        DecimalInputTextWatcherExtract mDecimalInputTextWatcherExtract = new DecimalInputTextWatcherExtract(etPriceNumber,tv_beyond_below_toast);
        etPriceNumber.addTextChangedListener(mDecimalInputTextWatcherExtract);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            isFree = true;
            tvGroupType.setText("免费入群");
            tvMoneyLogo.setVisibility(View.GONE);
            tv_beyond_below_toast.setVisibility(View.GONE);
            etPriceNumber.setText("");
            etPriceNumber.setHint("设置后入群无需付费");
            etPriceNumber.setEnabled(false);
        } else {
            isFree = false;
            tvGroupType.setText("付费入群");
            tvMoneyLogo.setVisibility(View.VISIBLE);
            etPriceNumber.setHint("50-6000的整数");
            etPriceNumber.setEnabled(true);
        }
    }

    @Override
    public void onRightClick() {
        String fee = StringUtil.getEditText(etPriceNumber);

        if (!isFree) {
            if (StringUtil.isBlank(fee)) {
                T.ss("请输入入群金额");
                return;
            }

            int int_fee = Integer.parseInt(fee);
            if (int_fee<50 || int_fee>6000) {
                T.sl("请输入50-6000的整数");
                return;
            }
        }

        createShequn(fee);
    }


    /**
     * 创建社群
     */
    private void createShequn(String fee) {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("shequn_name", group_name);
        if (isFree) {
            baseMap.put("fee", "0");
            baseMap.put("type", "0");
        }else{
            baseMap.put("fee", fee);
            baseMap.put("type", "1");//0免费 1安卓付费
        }
        baseMap.put("image", group_header);
        HttpSender sender = new HttpSender(HttpUrl.createShequn, "创建社群", baseMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            T.ss("创建成功");
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.CREATE_GROUP));
                            String shequn_id = GsonUtil.getInstance().getValue(json_data,"shequn_id");
                            GroupDetailActivity.actionStart(mActivity, shequn_id);
                            finish();
                        }
                    }

                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }
}
