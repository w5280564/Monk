package com.qingbo.monk.person.activity;

import android.text.TextUtils;
import android.view.View;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.SystemReview_List_Bean;
import com.qingbo.monk.bean.System_MesCount_Bean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.view.MyArrowItemView;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 系统通知
 */
public class Person_system extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.system_MyView)
    MyArrowItemView system_MyView;
    @BindView(R.id.comment_MyView)
    MyArrowItemView comment_MyView;
    @BindView(R.id.focus_MyView)
    MyArrowItemView focus_MyView;
    @BindView(R.id.at_MyView)
    MyArrowItemView at_MyView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_system;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        system_MyView.setOnClickListener(this);
        comment_MyView.setOnClickListener(this);
        focus_MyView.setOnClickListener(this);
        at_MyView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNumList(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_MyView:
                skipAnotherActivity(Person_System_Review.class);
                break;
            case R.id.comment_MyView:
                skipAnotherActivity(Person_System_examine.class);
                break;
            case R.id.focus_MyView:
                skipAnotherActivity(Person_System_Liked.class);
                break;
            case R.id.at_MyView:
                skipAnotherActivity(Person_System_At.class);
                break;
        }
    }

    private void getNumList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.System_Mes_Count, "系统消息数", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            System_MesCount_Bean system_mesCount_bean = GsonUtil.getInstance().json2Bean(json_data, System_MesCount_Bean.class);
                            system_MyView.getCount_Tv().setVisibility(View.GONE);
                            if (!TextUtils.equals(system_mesCount_bean.getCheckCount(),"0")) {
                                system_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                                system_MyView.getCount_Tv().setText(system_mesCount_bean.getCheckCount() + "");
                            }
                            comment_MyView.getCount_Tv().setVisibility(View.GONE);
                            if (!TextUtils.equals(system_mesCount_bean.getCommentCount(),"0")) {
                                comment_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                                comment_MyView.getCount_Tv().setText(system_mesCount_bean.getCommentCount() + "");
                            }
                            focus_MyView.getCount_Tv().setVisibility(View.GONE);
                            if (!TextUtils.equals(system_mesCount_bean.getLikeCount(),"0")) {
                                focus_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                                focus_MyView.getCount_Tv().setText(system_mesCount_bean.getLikeCount() + "");
                            }
                            at_MyView.getCount_Tv().setVisibility(View.GONE);
                            if (!TextUtils.equals(system_mesCount_bean.getAlertCount(),"0")) {
                                at_MyView.getCount_Tv().setVisibility(View.VISIBLE);
                                at_MyView.getCount_Tv().setText(system_mesCount_bean.getAlertCount() + "");
                            }

                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }


}