package com.qingbo.monk.login.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.internal.FlowLayout;
import com.google.gson.Gson;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.Topic_Bean;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第3步
 */
public class StepThreeFragmentLogin extends BaseFragment {

    @BindView(R.id.topic_lin)
    LinearLayout topic_lin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_three_fragment_login;
    }

    @OnClick({R.id.tv_back, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE));
                break;
            case R.id.tv_next:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_THREE));
                break;

        }
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getTopicLab();
    }

    private void getTopicLab() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.interestedList, "感兴趣列表", requestMap, new MyOnHttpResListener() {
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                Topic_Bean topic_bean = GsonUtil.getInstance().json2Bean(json_data, Topic_Bean.class);
                if (topic_bean != null) {
                    questionList(mActivity, topic_lin, topic_bean);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    ArrayList<TextView> questiontxt_List = new ArrayList<>();
    ArrayList<LinearLayout> questionlin_List = new ArrayList<>();
    ArrayList<ImageView> arrow_img_List = new ArrayList<>();
    private List<Boolean> choiceList =new ArrayList<>();

    public void questionList(Context context, LinearLayout myFlex, Topic_Bean topic_bean) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = topic_bean.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.mainlogin_forgetpsw_questionlist, null);
            ConstraintLayout qusetion_Constraint = myView.findViewById(R.id.qusetion_Constraint);
            TextView questionTxt = myView.findViewById(R.id.questionone_txt);
            LinearLayout choice_lin = myView.findViewById(R.id.choice_lin);
            ImageView arrow_img = myView.findViewById(R.id.arrow_img);
            arrow_img.setImageResource(R.mipmap.topic_down);
            questionTxt.setText(topic_bean.getList().get(i).getName());
            questiontxt_List.add(questionTxt);
            questionlin_List.add(choice_lin);
            arrow_img_List.add(arrow_img);
            choiceList.add(false);
            qusetion_Constraint.setTag(i);

            myFlex.addView(myView);
            qusetion_Constraint.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                boolean onChoice = choiceList.get(tag);
                if (onChoice) {
                    choiceList.set(tag,false);
                    arrow_img_List.get(tag).animate().rotation(0);
                    choice_lin.removeAllViews();
                } else {
                    choiceList.set(tag,true);
                    arrow_img_List.get(tag).animate().rotation(-90);
                    lableChoiceList(context,choice_lin, topic_bean.getList().get(tag), tag);
                }
            });
        }
    }

    @SuppressLint("RestrictedApi")
    public void lableChoiceList(Context context,LinearLayout myFlex , Topic_Bean.ListDTO model, int textTag) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = model.getChildren().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.mainlogin_forgetpsw_questionlist, null);
//            TextView lable_Txt = myView.findViewById()

             FlowLayout mylin = new FlowLayout(context);
//            mylin.setOrientation(LinearLayout.VERTICAL);
            mylin.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            TextView projectTxt = new TextView(context);
            projectTxt.setTextColor(ContextCompat.getColor(context, R.color.app_yellow_color));
//            projectTxt.setGravity(Gravity.CENTER_VERTICAL);
//            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());
//            RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
//            int margisleft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
//            itemParams.setMargins(margisleft, 0, 0, 0);
//            projectTxt.setLayoutParams(itemParams);

//            View myview = new View(context);
//            myview.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//            int viewheight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
//            RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, viewheight);
//            myview.setLayoutParams(viewParams);

            projectTxt.setText(model.getChildren().get(i).getName());
            projectTxt.setTag(i);
            mylin.addView(projectTxt);
//            mylin.addView(myview);
            myFlex.addView(mylin);
            projectTxt.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                choiceList.set(textTag,false);
                arrow_img_List.get(textTag).animate().rotation(0);
//                questionTxt.setText(model.getData().get(tag).getQuestion());
                myFlex.removeAllViews();
            });
        }
    }


}
