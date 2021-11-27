package com.qingbo.monk.login.fragment;

import android.view.View;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.T;
import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.HashMap;
import butterknife.OnClick;

/**
 * 登录填写更多信息第1步
 */
public class StepOneFragmentLogin extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment_login;
    }




    @Override
    protected void getServerData() {
        getIndustryListList();
    }


    /**
     * 获取行业列表
     */
    private void getIndustryListList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.industryList, "获取行业列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {

                        } else {
                            T.ss(msg);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    @OnClick({R.id.iv_header,R.id.arrowItemView_industry,R.id.arrowItemView_year,R.id.arrowItemView_city,R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_header:
//                showChoosePhotoDialog();
                break;
            case R.id.arrowItemView_industry:

                break;
            case R.id.arrowItemView_year:

                break;
            case R.id.arrowItemView_city:

                break;
            case R.id.tv_next:
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE));
                break;

        }
    }

    /**
     * 上传头像
     */
    private void uploadImage(final File mFile) {
//        HashMap<String, String> baseMap = new HashMap<String, String>();
//        baseMap.put("file", "file");
//        HttpSender sender = new HttpSender(HttpUrl.liveUploadImage, "上传直播封面", baseMap,
//                new MyOnHttpResListener() {
//
//                    @Override
//                    public void onComplete(String json, int status, String description, String data) {
//                        if (status == Constants.REQUEST_SUCCESS_STATUS) {
//                            T.ss(getString(R.string.Image_SCCG));
//                            haveUploadImg = true;
//                            Uri uri = Uri.fromFile(mFile);
//                            Glide.with(mActivity).load(uri).into(iv_live);
//                        } else {
//                            T.ss(description);
//                        }
//
//                    }
//                },true);
//        sender.setContext(mActivity);
//        sender.sendPostImage(mFile);
    }
}
