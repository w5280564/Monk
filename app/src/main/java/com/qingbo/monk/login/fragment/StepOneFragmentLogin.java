package com.qingbo.monk.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.bumptech.glide.Glide;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryFragment;
import com.qingbo.monk.login.activity.AreaCodeListActivity;
import com.qingbo.monk.login.activity.ChooseIndustryActivity;
import com.xunda.lib.common.bean.AreaBean;
import com.xunda.lib.common.bean.BaseAreaBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.LoginMoreInfoEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.FileUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.PickCityDialog;
import com.xunda.lib.common.dialog.PickStringDialog;
import com.xunda.lib.common.view.MyArrowItemView;
import com.zhihu.matisse.Matisse;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录填写更多信息第1步
 */
public class StepOneFragmentLogin extends BaseCameraAndGalleryFragment {
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.arrowItemView_industry)
    MyArrowItemView arrowItemView_industry;
    @BindView(R.id.arrowItemView_year)
    MyArrowItemView arrowItemView_year;
    @BindView(R.id.arrowItemView_city)
    MyArrowItemView arrowItemView_city;
    private boolean haveUploadImg;
    private ActivityResultLauncher mActivityResultLauncher;
    private String[] yearList = {"1-3年","3-5年","5-10年","10-15年"};
    private List<AreaBean> mAreaList = new ArrayList<>();
    private String industryName,city,county,work;
    private String avatar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_step_one_fragment_login;
    }


    @Override
    protected void initView() {
        haveUploadImg = !StringUtil.isBlank(SharePref.user().getUserHead());
        GlideUtils.loadCircleImage(mContext,iv_header, SharePref.user().getUserHead());
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result!=null) {
                    int resultCode = result.getResultCode();
                    if (resultCode==Activity.RESULT_OK) {
                        industryName = result.getData().getStringExtra("name");
                        arrowItemView_industry.getTip().setVisibility(View.GONE);
                        arrowItemView_industry.getTvContent().setText(industryName);
                    }
                }
            }
        });
    }

    private void showCityDialog() {
        PickCityDialog mPickCityDialog = new PickCityDialog(mActivity,mAreaList, new PickCityDialog.CityChooseIdCallback() {
            @Override
            public void onConfirm(List<String> nameList, List<Integer> idList) {
                if (ListUtils.isEmpty(nameList)) {
                    return;
                }
                if (nameList.size()<2) {
                    return;
                }
                arrowItemView_city.getTip().setVisibility(View.GONE);
                city = nameList.get(0);
                county = nameList.get(1);
                arrowItemView_city.getTvContent().setText(city+"-"+county);
            }
        });
        mPickCityDialog.show();
    }

    private void showPickStringDialog() {
        PickStringDialog mPickStringDialog = new PickStringDialog(mActivity, Arrays.asList(yearList),"", new PickStringDialog.ChooseCallback() {
            @Override
            public void onConfirm(String value) {
                arrowItemView_year.getTip().setVisibility(View.GONE);
                work = value;
                arrowItemView_year.getTvContent().setText(work);
            }
        });
        mPickStringDialog.show();
    }

    @Override
    protected void getServerData() {
        getCityList();
    }

    /**
     * 获取城市列表
     */
    private void getCityList() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.cityList, "获取城市列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseAreaBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseAreaBean.class);
                            if (obj!=null) {
                                mAreaList.addAll(obj.getList());
                            }
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
                checkGalleryPermission(1);
                break;
            case R.id.arrowItemView_industry:
                Intent intent = new Intent(mActivity, ChooseIndustryActivity.class);
                mActivityResultLauncher.launch(intent);
                break;
            case R.id.arrowItemView_year:
                showPickStringDialog();
                break;
            case R.id.arrowItemView_city:
                showCityDialog();
                break;
            case R.id.tv_next:
                if(!haveUploadImg){
                    T.ss("请上传照片");
                    return;
                }

                String nickname = StringUtil.getEditText(et_name);

                if(StringUtil.isBlank(nickname)){
                    T.ss("请填写名称");
                    return;
                }

                if(StringUtil.isBlank(industryName)){
                    T.ss("请选择行业");
                    return;
                }


                if(StringUtil.isBlank(work)){
                    T.ss("请选择工作年限");
                    return;
                }


                if(StringUtil.isBlank(city)){
                    T.ss("请选择城市");
                    return;
                }

                JSONObject jsonObject  = new JSONObject();
                try {
                    jsonObject.put("nickname", nickname);
                    jsonObject.put("city", city);
                    jsonObject.put("county", county);
                    jsonObject.put("work", work);
                    jsonObject.put("industry", industryName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharePref.local().setUserNickName(nickname);
                EventBus.getDefault().post(new LoginMoreInfoEvent(LoginMoreInfoEvent.LOGIN_SUBMIT_MORE_INFO_STEP_ONE,true,avatar,nickname,city,county,work,industryName));
                break;

        }
    }



    @Override
    protected void onUploadSuccess(String imageString) {
        haveUploadImg = true;
        avatar = imageString;
        GlideUtils.loadImage(mContext,iv_header,avatar);
        SharePref.user().setUserHead(avatar);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }

}
