package com.qingbo.monk.person.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.TouchRegion;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.base.behavior.AppBarLayoutOverScrollViewBehavior;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.InterestList_Bean;
import com.qingbo.monk.bean.myCardBean;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.fragment.MyArchives_Fragment;
import com.qingbo.monk.person.fragment.MyCollect_Fragment;
import com.qingbo.monk.person.fragment.MyDynamic_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
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
import com.xunda.lib.common.dialog.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 我/他人 个人详情页面
 */
public class MyAndOther_Card extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.mesHomepage_Tv)
    TextView mesHomepage_Tv;
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
    @BindView(R.id.joinHead_Lin)
    LinearLayout joinHead_Lin;
    @BindView(R.id.interestHead_Lin)
    LinearLayout interestHead_Lin;
    @BindView(R.id.group_Con)
    ConstraintLayout group_Con;
    @BindView(R.id.interest_Con)
    ConstraintLayout interest_Con;
    @BindView(R.id.editUser_Tv_)
    TextView editUser_Tv_;
    @BindView(R.id.follow_Tv)
    TextView follow_Tv;
    @BindView(R.id.send_Mes)
    TextView send_Mes;
    @BindView(R.id.urlLabel_Lin)
    LinearLayout urlLabel_Lin;
    @BindView(R.id.iv_bianji)
    ImageView iv_bianji;
    @BindView(R.id.share_Btn)
    Button share_Btn;
    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_Layout)
    AppBarLayout appbar_Layout;
    @BindView(R.id.textview10)
    TextView textview10;
    @BindView(R.id.textview11)
    TextView textview11;
    @BindView(R.id.top_Con)
    ConstraintLayout top_Con;
    @BindView(R.id.toolbar_Head_Img)
    ImageView toolbar_Head_Img;
    @BindView(R.id.toolbar_name_Tv)
    TextView toolbar_name_Tv;
    @BindView(R.id.setting_Btn)
    Button setting_Btn;
    @BindView(R.id.sex_Img)
    ImageView sex_Img;

    private String userID;
    private boolean isExpert;//专家不显示关注

    public static void actionStart(Context context, String userID) {
        Intent intent = new Intent(context, MyAndOther_Card.class);
        intent.putExtra("userID", userID);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param userID
     * @param isExpert 专家不显示关注
     */
    public static void actionStart(Context context, String userID, boolean isExpert) {
        Intent intent = new Intent(context, MyAndOther_Card.class);
        intent.putExtra("userID", userID);
        intent.putExtra("isExpert", isExpert);
        context.startActivity(intent);
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this).titleBar(mToolbar)
                .statusBarDarkFont(false)
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
        isExpert = getIntent().getBooleanExtra("isExpert", false);
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.card_Tab);
        mViewPager = findViewById(R.id.card_ViewPager);

        TouchRegion touchRegion = new TouchRegion(top_Con);
        touchRegion.expandViewTouchRegion(tv_follow_number, 50);
        touchRegion.expandViewTouchRegion(tv_fans_number, 50);

        touchRegion.expandViewTouchRegion(back_Btn, 100);
        initMenuData();
        tv_name.setFilters(new InputFilter[]{new ByteLengthFilter(14)});
        if (isMe()) {
            iv_bianji.setVisibility(View.VISIBLE);
        }
        setting_Btn.setVisibility(View.GONE);
    }

    @Override
    protected void getServerData() {
        getMyGroup(userID, false);
        getInterestData(userID, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData(userID, true);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        back_Btn.setOnClickListener(this);
        send_Mes.setOnClickListener(this);
        share_Btn.setOnClickListener(this);
        group_Con.setOnClickListener(this);
        interest_Con.setOnClickListener(this);
        follow_Tv.setOnClickListener(this);
        editUser_Tv_.setOnClickListener(this);
        tv_follow_number.setOnClickListener(this);
        tv_fans_number.setOnClickListener(this);
        iv_bianji.setOnClickListener(this);
        textview10.setOnClickListener(this);
        textview11.setOnClickListener(this);

        appbar_Layout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float percent = (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
            if (percent == 0) {
                groupChange(1f, 1);
            } else if (percent == 1) {
                groupChange(1f, 2);
            } else {
                groupChange(percent, 0);
            }
        });

        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) appbar_Layout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener(new AppBarLayoutOverScrollViewBehavior.onProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
                if (progress == 0 && isRelease) {//进度条回到0 并且已释放 刷新一次数据
                    getUserData(userID, true);
                    initMenuData();
                }
            }
        });
    }

    private int lastState = 1;

    /**
     * @param alpha
     * @param state 0-正在变化 1展开 2 关闭
     */
    public void groupChange(float alpha, int state) {
        lastState = state;
        mToolbar.setAlpha(1);//一直需要展示的状态
        switch (state) {
            case 1://完全展开 显示白色
//                mToolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.transparent));
                ImmersionBar.with(mActivity).titleBar(mToolbar).statusBarDarkFont(false).init();//把标题栏和状态栏绑定在一块
                setTint(back_Btn, R.mipmap.icon_back, R.color.white);
                setTint(share_Btn, R.mipmap.person_zhaunfa, R.color.white);
//                mViewPager.setNoScroll(false);
                toolbar_Head_Img.setVisibility(View.GONE);
                toolbar_name_Tv.setVisibility(View.GONE);
                break;
            case 2://完全关闭 显示黑色
                ImmersionBar.with(mActivity).titleBar(mToolbar).statusBarDarkFont(true).init();
                setTint(back_Btn, R.mipmap.icon_back, R.color.text_color_6f6f6f);
                setTint(share_Btn, R.mipmap.person_zhaunfa, R.color.text_color_6f6f6f);
                toolbar_Head_Img.setVisibility(View.VISIBLE);
                toolbar_name_Tv.setVisibility(View.VISIBLE);
//                mViewPager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                if (lastState != 0) {
                    setTint(share_Btn, R.mipmap.person_zhaunfa, R.color.text_color_6f6f6f);
                }
                //为什么禁止滑动？在介于开关状态之间，不允许滑动，开启可能会导致不好的体验
//                mViewPager.setNoScroll(true);
                break;
        }
    }

    /**
     * 修改按钮背景色
     *
     * @param button
     * @param drawable
     * @param color
     */
    public void setTint(Button button, int drawable, int color) {
        Drawable originalDrawable = ContextCompat.getDrawable(this, drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, color));
        button.setBackground(wrappedDrawable);
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        if (fragments != null) {
            fragments.clear();
        }
        if (menuList != null) {
            menuList.clear();
        }
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("动态");
        if (isMe()) {
            tabName.add("收藏");
        }
        tabName.add("档案");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(MyDynamic_Fragment.newInstance(userID));
        if (isMe()) {
            fragments.add(MyCollect_Fragment.newInstance(userID));
        }
        fragments.add(MyArchives_Fragment.newInstance(userID));

        int selectedTabPosition = mTabLayout.getSelectedTabPosition();
        if (selectedTabPosition == -1) {
            selectedTabPosition = 0;
        }
        initViewPager(selectedTabPosition);
    }


    UserBean userBean;

    private void getUserData(String userId, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Info, "用户信息", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    userBean = GsonUtil.getInstance().json2Bean(json_data, UserBean.class);
                    if (userBean != null) {
                        iv_img.setTag("overScroll"); //BaseApplication中添加了ViewTarget
                        GlideUtils.loadImage(mActivity, iv_img, userBean.getCover_image(), R.mipmap.mycard_bg);

                        GlideUtils.loadCircleImage(mActivity, head_Img, userBean.getAvatar());
                        GlideUtils.loadCircleImage(mActivity, toolbar_Head_Img, userBean.getAvatar());
                        tv_name.setText(userBean.getNickname());
                        toolbar_name_Tv.setText(userBean.getNickname());
                        labelFlow(label_Lin, mActivity, userBean.getTagName());
                        tv_follow_number.setText(userBean.getFollowNum());
                        tv_fans_number.setText(userBean.getFansNum());
                        originalValue(userBean.getDescription(), "暂未填写", "个人说明：", brief_Tv);
//                        int width = com.qingbo.monk.base.baseview.ScreenUtils.getScreenWidth(mActivity) - com.qingbo.monk.base.baseview.ScreenUtils.dip2px(mActivity, 50);
//                        brief_Tv.initWidth(width);
//                        if (userBean.getDescription().isEmpty()) {
//                            brief_Tv.setText("暂未填写");
//                        } else {
//                        brief_Tv.setCloseText("个人说明：\n" + userBean.getDescription());
//                        }

                        String sex = userBean.getSex();
                        if (TextUtils.equals(sex,"女")){
                            sex_Img.setBackgroundResource(R.mipmap.nv);
                        }else {
                            sex_Img.setBackgroundResource(R.mipmap.nan);
                        }
                        originalValue(userBean.getCity(), "暂未填写", "城市：", address_Tv);
                        originalValue(userBean.getIndustry(), "暂未填写", "行业：", industry_Tv);
                        originalValue(userBean.getWork(), "暂未填写", "工作经验：", job_Tv);

                        if (isMe()) {
                            mesHomepage_Tv.setText("我的社交主页");
                            editUser_Tv_.setVisibility(View.VISIBLE);
                        } else {
                            mesHomepage_Tv.setText("他的社交主页");
                            isFollow(userBean.getFollow_status(), follow_Tv, send_Mes);
                        }
                        List<UserBean.ColumnDTO> column = userBean.getColumn();
                        urlLabelFlow(urlLabel_Lin, mActivity, column);
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
        if (isMe()) {
            requestMap.put("type", "4");
        } else {
            requestMap.put("type", "3");
        }
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我的社群—Pc", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            myCardBean myCardBean = GsonUtil.getInstance().json2Bean(json_root, myCardBean.class);
                            if (myCardBean != null) {
                                String groupString = "";
                                if (isMe()) {
                                    groupString = "我的问答社群";
                                } else {
                                    groupString = "他的社群";
                                }
                                groupName_Tv.setText(groupString);
                                String format = String.format("拥有%1$s个社群", myCardBean.getData().getUser_group_count());
                                joinCount_Tv.setText(format);
                                groupHeadFlow(joinHead_Lin, mActivity, myCardBean);
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
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_My, "我的兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                InterestList_Bean interestList_bean = new Gson().fromJson(json_data, InterestList_Bean.class);
                if (interestList_bean != null) {
                    String groupString = "";
                    if (isMe()) {
                        groupString = "我的兴趣组";
                    } else {
                        groupString = "他的兴趣组";
                    }
                    interestName_Tv.setText(groupString);
                    String format = String.format("加入%1$s个兴趣组", interestList_bean.getCount());
                    interestJoinCount_Tv.setText(format);
                    InterestHeadFlow(interestHead_Lin, mActivity, interestList_bean);
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void postFollowData(String otherUserId, TextView followView, View sendView) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    if (userBean != null) {
                        Integer followStatus = followStateBena.getFollowStatus();
                        userBean.setFollow_status(followStatus);
                        isFollow(followStatus, follow_Tv, send_Mes);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
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
            case R.id.tv_follow_number:
            case R.id.textview10:
                MyFollowActivity.actionStart(this, userID);
                break;
            case R.id.tv_fans_number:
            case R.id.textview11:
                MyFansActivity.actionStart(this, userID);
                break;
            case R.id.back_Btn:
                finish();
                break;
            case R.id.group_Con:
                MyGroupList_Activity.actionStart(mActivity, userID);
                break;
            case R.id.interest_Con:
                MyInterestList_Activity.actionStart(mActivity, userID);
                break;
            case R.id.follow_Tv:
                String id = userBean.getId();
                postFollowData(id, follow_Tv, send_Mes);
                break;
            case R.id.send_Mes:
                if (userBean == null) {
                    return;
                }
                ChatActivity.actionStart(mActivity, userBean.getId(), userBean.getNickname(), userBean.getAvatar());
                break;
            case R.id.editUser_Tv_:
                MyAndOtherEdit_Card.actionStart(mActivity, userID);
                break;
            case R.id.iv_bianji:
                if (isMe()) {
                    String isOriginator = PrefUtil.getUser().getIsOriginator();
                    MyCrateArticle_Avtivity.actionStart(mActivity, isOriginator);
                }
                break;
            case R.id.share_Btn:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        if (userBean != null) {
            String imgUrl = userBean.getAvatar();
            String downURl = String.format("https://shjr.gsdata.cn/share/get-auth?id=%1$s", userBean.getId());
            String title = String.format("分享 %1$s 的鹅先知主页", userBean.getNickname());
            String content = String.format("%1$s粉丝 %2$s关注", userBean.getFansNum(), userBean.getFollowNum());
            ShareDialog mShareDialog = new ShareDialog(this, downURl, imgUrl, title, content, "分享");
            mShareDialog.show();
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
            setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
        }
    }

    public static void setColor(Context context, int index, TextView tv) {
        tv.setTextColor(Color.WHITE);
        int dex = index % 4;
        if (dex == 0) {
            changeShapColor(tv, ContextCompat.getColor(context, R.color.lable_color_80ff801f));
        } else if (dex == 1) {
            changeShapColor(tv, ContextCompat.getColor(context, R.color.lable_color_801F8FE5));
        } else if (dex == 2) {
            changeShapColor(tv, ContextCompat.getColor(context, R.color.lable_color_8076AD45));
        } else if (dex == 3) {
            changeShapColor(tv, ContextCompat.getColor(context, R.color.lable_color_807622BD));
        }
    }

    /**
     * 我的社群
     *
     * @param myFlow
     * @param mContext
     */
    public void groupHeadFlow(LinearLayout myFlow, Context mContext, myCardBean interestList_bean) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        int length = interestList_bean.getData().getList().size();
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.interest_head, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            ImageView head_Img = view.findViewById(R.id.head_Img);
            String groupImage = interestList_bean.getData().getList().get(i).getShequnImage();
            GlideUtils.loadRoundImage(mContext, head_Img, groupImage, 5);
            myFlow.addView(view);
        }
    }

    /**
     * 我的兴趣组
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

    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        if (isExpert) {
            return;
        }
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_6f6f6f));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_6f6f6f));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
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
        int index = 0;
        for (UserBean.ColumnDTO columnDTO : urlList) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.user_page, null);
            TextView name_Tv = view.findViewById(R.id.name_Tv);
            TextView contentUrl_Tv = view.findViewById(R.id.contentUrl_Tv);
            name_Tv.setText(columnDTO.getColumnName());
            contentUrl_Tv.setText(columnDTO.getColumnUrl());
            contentUrl_Tv.setTag(index);
            myFlow.addView(view);
            index++;

            contentUrl_Tv.setOnClickListener(v -> {
                int tag = (int) v.getTag();
                String columnName = urlList.get(tag).getColumnName();
                String columnUrl = urlList.get(tag).getColumnUrl();
                jumpToWebView(columnName, columnUrl);
            });

            contentUrl_Tv.setOnLongClickListener(v -> {
                int tag = (int) v.getTag();
                String columnUrl = urlList.get(tag).getColumnUrl();
                StringUtil.copy(columnUrl, mContext);
                T.s("已复制到剪切板", 2000);
                return true;
            });
        }
    }


}