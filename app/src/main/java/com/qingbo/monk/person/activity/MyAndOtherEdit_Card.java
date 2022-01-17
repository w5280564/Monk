package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.gyf.barlibrary.ImmersionBar;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.base.baseview.MyCardEditView;
import com.qingbo.monk.base.viewTouchDelegate;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.EditStringDialog;
import com.xunda.lib.common.view.MyArrowItemView;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑自己资料页
 */
public class MyAndOtherEdit_Card extends BaseCameraAndGalleryActivity_Single implements View.OnClickListener {
    @BindView(R.id.back_Btn)
    Button back_Btn;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.head_Img)
    ImageView head_Img;
    @BindView(R.id.nickName_MyView)
    MyArrowItemView nickName_MyView;
    @BindView(R.id.address_MyView)
    MyArrowItemView address_MyView;
    @BindView(R.id.brief_Tv)
    TextView brief_Tv;
    @BindView(R.id.explain_Con)
    ConstraintLayout explain_Con;
    @BindView(R.id.home_Con)
    ConstraintLayout home_Con;
    @BindView(R.id.urlLabel_Lin)
    LinearLayout urlLabel_Lin;

    @BindView(R.id.interestEdit_Tv_)
    TextView interestEdit_Tv_;
    @BindView(R.id.interest_Flow)
    FlowLayout interest_Flow;
    @BindView(R.id.good_EditView)
    MyCardEditView good_EditView;
    @BindView(R.id.resources_EditView)
    MyCardEditView resources_EditView;
    @BindView(R.id.achievement_EditView)
    MyCardEditView achievement_EditView;
    @BindView(R.id.learn_EditView)
    MyCardEditView learn_EditView;
    @BindView(R.id.harvest_EditView)
    MyCardEditView harvest_EditView;


    private String userID;
    private boolean isHead = true;
    HashMap<String, String> requestMap = new HashMap<>();

    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyAndOtherEdit_Card.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.myandother_edit_card;
    }

    @Override
    protected void initLocalData() {
        userID = getIntent().getStringExtra("userID");
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(back_Btn, 50);
        interestEdit_Tv_.setVisibility(View.VISIBLE);
        good_EditView.getEdit_Tv().setVisibility(View.VISIBLE);
        resources_EditView.getEdit_Tv().setVisibility(View.VISIBLE);
        achievement_EditView.getEdit_Tv().setVisibility(View.VISIBLE);
        learn_EditView.getEdit_Tv().setVisibility(View.VISIBLE);
        harvest_EditView.getEdit_Tv().setVisibility(View.VISIBLE);

        initCity();
    }

    @Override
    protected void initEvent() {
        back_Btn.setOnClickListener(this);
        head_Img.setOnClickListener(this);
        iv_img.setOnClickListener(this);
        nickName_MyView.setOnClickListener(this);
        address_MyView.setOnClickListener(this);
        explain_Con.setOnClickListener(this);
        home_Con.setOnClickListener(this);
        interestEdit_Tv_.setOnClickListener(this);
        good_EditView.getEdit_Tv().setOnClickListener(this);
        resources_EditView.getEdit_Tv().setOnClickListener(this);
        achievement_EditView.getEdit_Tv().setOnClickListener(this);
        learn_EditView.getEdit_Tv().setOnClickListener(this);
        harvest_EditView.getEdit_Tv().setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData(userID, true);
    }

    UserBean userBean;
    private void getUserData(String userId, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
//                if (refresh_layout.isRefreshing()) {
//                    refresh_layout.setRefreshing(false);
//                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        GlideUtils.loadImage(mActivity, iv_img, userBean.getCover_image());
                        GlideUtils.loadCircleImage(mActivity, head_Img, userBean.getAvatar());
                        nickName_MyView.getTvContent().setText(userBean.getNickname());
                        String s = userBean.getProvince() + " " + userBean.getCity() + " " + userBean.getCounty();
                        address_MyView.getTvContent().setText(s);
                        originalValue(userBean.getDescription(), "暂未填写", "", brief_Tv);
                        interestLabelFlow(interest_Flow, mActivity, userBean.getInterested());

                        originalValue(userBean.getDomain(), "暂未填写", "", good_EditView.getContent_Tv());
                        originalValue(userBean.getResource(), "暂未填写", "", resources_EditView.getContent_Tv());
                        originalValue(userBean.getAchievement(), "暂未填写", "", achievement_EditView.getContent_Tv());
                        originalValue(userBean.getResearch(), "暂未填写", "", learn_EditView.getContent_Tv());
                        originalValue(userBean.getGetResource(), "暂未填写", "", harvest_EditView.getContent_Tv());

                        List<UserBean.ColumnDTO> column = userBean.getColumn();
                        urlLabelFlow(urlLabel_Lin, mActivity, column);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private void edit_Info() {
//        requestMap.put("userId",userId +"");
        requestMap.put("nickname", userBean.getNickname());
        HttpSender httpSender = new HttpSender(HttpUrl.Edit_Info, "修改个人信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    UserBean obj = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    saveUserInfo(obj);
                    onResume();
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * 我的兴趣
     */
    public void interestLabelFlow(FlowLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        for (String s : tagS) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.changeShapColor(label_Name, ContextCompat.getColor(mContext, com.xunda.lib.common.R.color.lable_color_1F8FE5));
            label_Name.setText(s);
            myFlow.addView(view);
        }
    }


    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + (CharSequence) value);
        }
    }

    /**
     * 保存用户信息
     *
     * @param userObj 用户对象
     */
    private void saveUserInfo(UserBean userObj) {
        if (userObj != null) {
            PrefUtil.saveUser(userObj, "");
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (userBean == null){
            return;
        }
        switch (v.getId()) {
            case R.id.back_Btn:
                finish();
                break;
            case R.id.head_Img:
                isHead = true;
                checkGalleryPermission(); //修改头像
                break;
            case R.id.iv_img:
                isHead = false;
                checkGalleryPermission();//修改背景
                break;
            case R.id.nickName_MyView:
                Edit_ChangeName.actionStart(mActivity,userBean.getNickname()); //修改昵称
                break;
            case R.id.address_MyView:
                mPicker.showCityPicker();//修改居住地
                break;
            case R.id.explain_Con:
                Edit_ChangeExplain.actionStart(mActivity,userBean.getNickname());//修改个人说明
                break;
            case R.id.home_Con:
                Edit_ChangePage.actionStart(mActivity,userBean.getNickname());//修改头像
                break;
            case R.id.interestEdit_Tv_:
                String interested = userBean.getInterested();
                Edit_Change_Interest.actionStart(mActivity,userBean.getNickname(),interested);
                break ;
        }
        if (v.equals(good_EditView.getEdit_Tv())){
            String domain = userBean.getDomain();
            Edit_Change_Industry.actionStart(mActivity,userBean.getNickname(),domain);
        }else if (v.equals(resources_EditView.getEdit_Tv())){
            String resource = userBean.getResource();
            Edit_Change_Resources.actionStart(mActivity,userBean.getNickname(),resource);
        }else if (v.equals(achievement_EditView.getEdit_Tv())){
            Edit_Change_Achievement.actionStart(mActivity,userBean);
        }else if (v.equals(learn_EditView.getEdit_Tv())){
            String research = userBean.getResearch();
            Edit_Change_Learn.actionStart(mActivity,userBean.getNickname(),research);
        }else if (v.equals(harvest_EditView.getEdit_Tv())){
            String getResource = userBean.getGetResource();
            Edit_Change_Harvest.actionStart(mActivity,userBean.getNickname(),getResource);
        }
    }



    @Override
    protected void onUploadSuccess(String imageString) {
        if (isHead) {
            requestMap.put("avatar", imageString);
        } else {
            requestMap.put("cover_image", imageString);
        }
        edit_Info();
    }

    @Override
    protected void onUploadFailure(String error_info) {
    }

    CityPickerView mPicker = new CityPickerView();
    private void initCity() {
        //等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃。
        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(this);
        //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        mPicker.setOnCityItemClickListener(new mPickerCityItemClick());
    }

    private class mPickerCityItemClick extends OnCityItemClickListener {
        @Override
        public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
            //省份province 城市city 地区district
            String cityStr = province + " " + city + " " + district;

            requestMap.put("province", province.getName());
            requestMap.put("city", city.getName());
            requestMap.put("county", district.getName());
            edit_Info();
        }

        @Override
        public void onCancel() {
//            ToastUtils.showLongToast(UserDetail_Set.this, "已取消");
        }
    }

    /**
     * 我的专栏
     */
    public void urlLabelFlow(LinearLayout myFlow, Context mContext, List<UserBean.ColumnDTO> urlList) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (ListUtils.isEmpty(urlList)) {
            return;
        }
        for (UserBean.ColumnDTO columnDTO : urlList) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.user_page, null);
            TextView name_Tv = view.findViewById(R.id.name_Tv);
            TextView contentUrl_Tv = view.findViewById(R.id.contentUrl_Tv);

            name_Tv.setText(columnDTO.getColumnName());
            contentUrl_Tv.setText(columnDTO.getColumnUrl());
            myFlow.addView(view);
        }
    }



}