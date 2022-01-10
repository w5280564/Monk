package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.UploadPictureBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 回答别人问题
 */
public class PublisherAnswerQuestionToPeopleActivity extends BaseActivity {
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.tv_to_name)
    TextView tv_to_name;
    @BindView(R.id.tv_remains_text)
    TextView tv_remains_text;
    private TwoButtonDialogBlue mDialog;
    private String mContent;
    private String to_name, to_id,shequn_id;


    public static void actionStart(Context context, String to_name, String to_id, String shequn_id) {
        Intent intent = new Intent(context, PublisherAnswerQuestionToPeopleActivity.class);
        intent.putExtra("to_name", to_name);
        intent.putExtra("to_id", to_id);
        intent.putExtra("shequn_id", shequn_id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer_question_to_people;
    }

    @Override
    protected void initLocalData() {
        to_name = getIntent().getStringExtra("to_name");
        to_id = getIntent().getStringExtra("to_id");
        shequn_id = getIntent().getStringExtra("shequn_id");
        tv_to_name.setText(StringUtil.getStringValue(to_name));
    }


    @Override
    protected void initEvent() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_remains_text.setText(String.format("%s/2000",StringUtil.getEditText(et_content).length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showBackDialog() {
        getPramsValue();

        if (!StringUtil.isBlank(mContent)) {
            if (mDialog == null) {
                mDialog = new TwoButtonDialogBlue(this, "返回将丢失填写的内容，确定返回吗", "取消", "确定",
                        new TwoButtonDialogBlue.ConfirmListener() {

                            @Override
                            public void onClickRight() {
                                finish();
                            }

                            @Override
                            public void onClickLeft() {

                            }
                        });
            }

            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } else {
            finish();
        }

    }


    private void getPramsValue() {
        mContent = StringUtil.getEditText(et_content);

    }

    /**
     * 回答社群问题
     */
    private void answerQuestion() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("id", to_id);
        baseMap.put("shequn_id", shequn_id);
        baseMap.put("content", mContent);

        HttpSender sender = new HttpSender(HttpUrl.answerQuestion, "回答社群问题", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            EventBus.getDefault().post(new FinishEvent(FinishEvent.PUBLISH_ANSWER));
                            showToastDialog("提交成功！");
                        }
                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendPost();
    }








    @Override
    public void onBackPressed() {
        showBackDialog();
    }


    @OnClick({R.id.rl_back, R.id.rl_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                showBackDialog();
                break;
            case R.id.rl_publish:
                getPramsValue();

                if (StringUtil.isBlank(mContent)) {
                    T.ss("请填写回答内容");
                    return;
                }

                answerQuestion();
                break;
        }
    }
}
