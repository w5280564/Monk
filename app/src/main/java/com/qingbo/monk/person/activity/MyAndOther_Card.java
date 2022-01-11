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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.CustomCoordinatorLayout;
import com.qingbo.monk.base.myCardBean;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.InterestList_Bean;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.bean.UserBean;
import com.qingbo.monk.home.fragment.HomeFocus_Fragment;
import com.qingbo.monk.person.fragment.MyArchives_Fragment;
import com.qingbo.monk.person.fragment.MyDynamic_Fragment;
import com.qingbo.monk.question.activity.MyGroupListActivity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class MyAndOther_Card extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.back_Btn)
    Button back_Btn;
    @BindView(R.id.brief_Tv)
    TextView brief_Tv;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.head_Img)
    ImageView head_Img;
    @BindView(R.id.tv_follow_number)
    TextView tv_follow_number;
    @BindView(R.id.tv_fans_number)
    TextView tv_fans_number;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.label_Lin)
    LinearLayout label_Lin;
    @BindView(R.id.address_Tv)
    TextView address_Tv;
    @BindView(R.id.industry_Tv)
    TextView industry_Tv;
    @BindView(R.id.job_Tv)
    TextView job_Tv;
    @BindView(R.id.groupName_Tv)
    TextView groupName_Tv;
    @BindView(R.id.joinCount_Tv)
    TextView joinCount_Tv;
    @BindView(R.id.interestName_Tv)
    TextView interestName_Tv;
    @BindView(R.id.interestJoinCount_Tv)
    TextView interestJoinCount_Tv;
    @BindView(R.id.interestHead_Lin)
    LinearLayout interestHead_Lin;
    @BindView(R.id.group_Con)
    ConstraintLayout group_Con;
    @BindView(R.id.interest_Con)
    ConstraintLayout interest_Con;


    private String userID;

    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyAndOther_Card.class);
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
        return R.layout.activity_myandother_card;
    }

    @Override
    protected void initLocalData() {
        userID = getIntent().getStringExtra("userID");
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.card_Tab);
        mViewPager = findViewById(R.id.card_ViewPager);
        NestedScrollView top_Src = findViewById(R.id.top_Src);
        CustomCoordinatorLayout headLayout = findViewById(R.id.headLayout);
        LinearLayout bot_Lin = findViewById(R.id.bot_Lin);
        headLayout.setmMoveView(top_Src, bot_Lin);
        headLayout.setmZoomView(iv_img);


        viewTouchDelegate.expandViewTouchDelegate(back_Btn, 50);
        initMenuData();


    }

    @Override
    protected void getServerData() {
        getUserData(userID, true);
        getMyGroup(userID, false);
        getInterestData(userID, false);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        back_Btn.setOnClickListener(this);
        brief_Tv.setOnClickListener(this);
        group_Con.setOnClickListener(this);
        interest_Con.setOnClickListener(this);

    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        if (isMe()) {
            tabName.add("我的动态");
        } else {
            tabName.add("他的动态");
        }
        tabName.add("档案");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(MyDynamic_Fragment.newInstance(userID));
        fragments.add(MyArchives_Fragment.newInstance(userID));
        initViewPager(0);

    }


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
                    UserBean userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        GlideUtils.loadCircleImage(mActivity, iv_img, userBean.getCoverImage());
                        GlideUtils.loadCircleImage(mActivity, head_Img, userBean.getAvatar());
                        tv_name.setText(userBean.getNickname());
                        labelFlow(label_Lin, mActivity, userBean.getTagName());
                        tv_follow_number.setText(userBean.getFollowNum());
                        tv_fans_number.setText(userBean.getFansNum());
//
                        originalValue(userBean.getDescription(), "暂未填写", "", brief_Tv);
                        originalValue(userBean.getCity(), "暂未填写", "城市：", address_Tv);
                        originalValue(userBean.getIndustry(), "暂未填写", "行业：", industry_Tv);
                        originalValue(userBean.getWork(), "暂未填写", "工作经验：", job_Tv);


                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private void getMyGroup(String userid, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userid", userid + "");
        requestMap.put("page", "1");
        requestMap.put("limit", "1");
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我的社群—Pc", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            myCardBean myCardBean = GsonUtil.getInstance().json2Bean(json_root, myCardBean.class);
                            if (myCardBean != null) {
                                String groupString = "";
                                if (isMe()) {
                                    groupString = "我的社群";
                                } else {
                                    groupString = "他的社群";
                                }
                                groupName_Tv.setText(groupString);
                                String format = String.format("加入%1$s个社群", myCardBean.getData().getCount());
                                joinCount_Tv.setText(format);
                            }
                        }
                    }

                }, isShow);

        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void getInterestData(String id, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userid", id);
        requestMap.put("page", "1");
        requestMap.put("limit", "3");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_My, "我的兴趣圈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                InterestList_Bean interestList_bean = new Gson().fromJson(json_data, InterestList_Bean.class);
                if (interestList_bean != null) {
                    String groupString = "";
                    if (isMe()) {
                        groupString = "我的兴趣圈";
                    } else {
                        groupString = "他的兴趣圈";
                    }
                    interestName_Tv.setText(groupString);
                    String format = String.format("加入%1$s个兴趣圈", interestList_bean.getCount());
                    interestJoinCount_Tv.setText(format);
                    InterestHeadFlow(interestHead_Lin, mActivity, interestList_bean);
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    /**
     * 是否是我自己
     *
     * @return
     */
    private boolean isMe() {
        String id = PrefUtil.getUser().getId();
        if (TextUtils.equals(userID, id)) {
            return true;
        }
        return false;
    }


    /**
     * 加载完成后
     * 解决-->Tablayout+viewpager 刷新数据后不能滑动问题
     */
    private void initAppBarStatus(AppBarLayout mAppBarLayout) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_Btn:
                finish();
                break;
            case R.id.brief_Tv:
                if (brief_Tv.getMaxLines() == 2) {
                    brief_Tv.setMaxLines(Integer.MAX_VALUE);
                } else {
                    brief_Tv.setMaxLines(2);
                }
                break;
            case R.id.group_Con:
                if (isMe()) {
                    MyGroupList_Activity.actionStart(mActivity, userID);
                }
                break;
            case R.id.interest_Con:
                if (isMe()) {
                    MyInterestList_Activity.actionStart(mActivity, userID);
                }
                break;

        }
    }

    /**
     * 我的标签
     *
     * @param myFlow
     * @param mContext
     * @param tag
     */
    public void labelFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
        }
    }

    /**
     * 我的兴趣圈
     *
     * @param myFlow
     * @param mContext
     */
    public void InterestHeadFlow(LinearLayout myFlow, Context mContext, InterestList_Bean interestList_bean) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        int length = interestList_bean.getList().size();
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.interest_head, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            String groupImage = interestList_bean.getList().get(i).getGroupImage();
            GlideUtils.loadRoundImage(mContext, head_Img, groupImage, 5);
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

}