package com.qingbo.monk.login.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.BaseIndustryBean;
import com.qingbo.monk.bean.IndustryBean;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.view.flowlayout.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * 选择行业
 */
public class ChooseIndustryActivity extends BaseActivity {
    @BindView(R.id.topic_lin)
    LinearLayout topic_lin;

    private List<IndustryBean> mList = new ArrayList<>();



    @Override
    protected int getLayoutId() {
       return R.layout.activity_choose_industry;
    }


    @Override
    protected void getServerData() {
        getIndustryList();
    }



    /**
     * 获取行业列表
     */
    private void getIndustryList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.industryList, "获取行业列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseIndustryBean mObj = GsonUtil.getInstance().json2Bean(json_data, BaseIndustryBean.class);
                            handleObj(mObj);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleObj(BaseIndustryBean mObj) {
        if (mObj!=null) {
            if (!ListUtils.isEmpty(mObj.getList())) {
                mList.addAll(mObj.getList());
                questionList();
            }
        }
    }



    ArrayList<ImageView> arrow_img_List = new ArrayList<>();
    private List<Boolean> choiceList =new ArrayList<>();
    public void questionList() {
        topic_lin.removeAllViews();
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(mContext).inflate(R.layout.mainlogin_forgetpsw_questionlist, null);
            TextView questionTxt = myView.findViewById(R.id.questionone_txt);
            FlowLayout choice_flex = myView.findViewById(R.id.choice_flex);
            ImageView arrow_img = myView.findViewById(R.id.arrow_img);
            arrow_img.setEnabled(false);
            arrow_img.setImageResource(R.mipmap.topic_down);
            questionTxt.setText(mList.get(i).getName());
            arrow_img_List.add(arrow_img);
            choiceList.add(true);
            questionTxt.setTag(i);
            topic_lin.addView(myView);
            labelChoiceList(choice_flex, mList.get(i));
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


    public void labelChoiceList(FlowLayout myFlex , IndustryBean model) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        }
        int size = model.getChildren().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(mContext).inflate(R.layout.topic_lable, null);
            TextView label_Txt = myView.findViewById(R.id.lable_Txt);
            String name = model.getChildren().get(i).getName();
            label_Txt.setText(name);
            myFlex.addView(myView);
            label_Txt.setOnClickListener(v -> {
                Intent mIntent = new Intent();
                mIntent.putExtra("name",name);
                setResult(RESULT_OK,mIntent);
                finish();
            });
        }
    }


}