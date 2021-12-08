package com.qingbo.monk.login.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.Topic_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
                if (choice_LableMap == null || choice_LableMap.size() < 3){
                    T.s("至少选择三个话题",3000);
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator i = choice_LableMap.keySet().iterator(); i.hasNext();) {
                    Object obj = i.next();
                    stringBuilder.append(choice_LableMap.get(obj)+",");
                }
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_THREE,true, stringBuilder.toString()));
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
               if (code == Constants.REQUEST_SUCCESS_CODE){
                   Topic_Bean topic_bean = GsonUtil.getInstance().json2Bean(json_data, Topic_Bean.class);
                   if (topic_bean != null) {
                       questionList(mActivity, topic_lin, topic_bean);
                   }
               }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    ArrayList<ImageView> arrow_img_List = new ArrayList<>();
    private List<Boolean> choiceList =new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void questionList(Context context, LinearLayout myFlex, Topic_Bean topic_bean) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = topic_bean.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.mainlogin_forgetpsw_questionlist, null);
            ConstraintLayout qusetion_Constraint = myView.findViewById(R.id.qusetion_Constraint);
            TextView questionTxt = myView.findViewById(R.id.questionone_txt);
            FlowLayout choice_flex = myView.findViewById(R.id.choice_flex);
            ImageView arrow_img = myView.findViewById(R.id.arrow_img);
            arrow_img.setEnabled(false);
            arrow_img.setImageResource(R.mipmap.topic_down);
            questionTxt.setText(topic_bean.getList().get(i).getName());
            arrow_img_List.add(arrow_img);
            choiceList.add(true);
            questionTxt.setTag(i);
            myFlex.addView(myView);
            lableChoiceList(context,choice_flex, topic_bean.getList().get(i));
            questionTxt.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                boolean onChoice = choiceList.get(tag);
                if (onChoice) {
                    choiceList.set(tag,false);
                    arrow_img_List.get(tag).animate().rotation(-90);
                    choice_flex.setVisibility(View.GONE);
                } else {
                    choiceList.set(tag,true);
                    arrow_img_List.get(tag).animate().rotation(0);
                    choice_flex.setVisibility(View.VISIBLE);
                }
            });

        }
    }
    HashMap<String,Object> choice_LableMap = new HashMap<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    public void lableChoiceList(Context context,FlowLayout myFlex , Topic_Bean.ListDTO model) {
         List<Boolean> choiceLable =new ArrayList<>();
         List<TextView> choiceTxt =new ArrayList<>();
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = model.getChildren().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.topic_lable, null);
            TextView lable_Txt = myView.findViewById(R.id.lable_Txt);
            lable_Txt.setText(model.getChildren().get(i).getName());
            choiceLable.add(false);
            choiceTxt.add(lable_Txt);
            lable_Txt.setTag(i);
            myFlex.addView(myView);
            lable_Txt.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                boolean onChoice = choiceLable.get(tag);
                if (onChoice){
                    choiceLable.set(tag,false);
                    choiceTxt.get(tag).setTextColor(ContextCompat.getColor(context,R.color.text_color_a1a1a1));
                    choiceTxt.get(tag).setBackgroundResource(R.drawable.label_stroke);
                    choice_LableMap.remove(model.getChildren().get(tag).getName(),model.getChildren().get(tag).getName());
                }else {
                    choiceLable.set(tag,true);
                    choiceTxt.get(tag).setTextColor(ContextCompat.getColor(context,R.color.text_color_444444));
                    choiceTxt.get(tag).setBackgroundResource(R.drawable.label_stroke_yellow);
                    choice_LableMap.put(model.getChildren().get(tag).getName(),model.getChildren().get(tag).getName());
                }
            });
        }
    }


}
