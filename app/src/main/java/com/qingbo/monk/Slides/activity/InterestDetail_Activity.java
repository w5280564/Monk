package com.qingbo.monk.Slides.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.InterestDetail_All_Fragment;
import com.qingbo.monk.Slides.fragment.InterestDetail_My_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.InterestDetail_Bean;
import com.qingbo.monk.home.fragment.InterestDetail_Member_Fragment;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 兴趣组详情
 */
public class InterestDetail_Activity extends BaseTabLayoutActivity implements View.OnClickListener {

    private String isShowTop;
    private String id;
    @BindView(R.id.head_Img)
    ImageView head_Img;
    @BindView(R.id.Interest_Name)
    TextView Interest_Name;
    @BindView(R.id.content_Tv)
    TextView content_Tv;
    @BindView(R.id.add_Tv)
    TextView add_Tv;
    @BindView(R.id.join_Tv)
    TextView join_Tv;

    /**
     * @param context
     * @param id
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String isShowTop, String id) {
        Intent intent = new Intent(context, InterestDetail_Activity.class);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_interestdetail;
    }


    @Override
    protected void initLocalData() {
        isShowTop = getIntent().getStringExtra("isShowTop");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.card_Tab);
        mViewPager = findViewById(R.id.card_ViewPager);
        initMenuData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        join_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getDetail(true);
    }


    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("全部");
        tabName.add("我的发布");
        tabName.add("成员");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        fragments.add(InterestDetail_All_Fragment.newInstance(id));
        fragments.add(InterestDetail_My_Fragment.newInstance(id));
        fragments.add(InterestDetail_Member_Fragment.newInstance(id));
        initViewPager(0);
    }

    InterestDetail_Bean interestDetail_bean;

    private void getDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_Detail, "兴趣组详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    interestDetail_bean = GsonUtil.getInstance().json2Bean(json_data, InterestDetail_Bean.class);
                    if (interestDetail_bean != null) {
                        GlideUtils.loadCircleImage(mActivity, head_Img, interestDetail_bean.getGroupImage());
                        Interest_Name.setText(interestDetail_bean.getGroupName());
                        String s = interestDetail_bean.getCount() + "人加入";
                        add_Tv.setText(s);
                        content_Tv.setText(interestDetail_bean.getGroupDes());
                        isJoin(interestDetail_bean.getStatus_num(), join_Tv);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * @param state   1是已加入  其他都是未加入
     * @param join_Tv
     */
    public void isJoin(String state, TextView join_Tv) {
        if (TextUtils.equals(state, "1")) {
            join_Tv.setText("已加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            join_Tv.setText("加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }

    /**
     * 修改加入状态
     *
     * @param stateIndex 1已加入 其他都是未加入
     */
    private void changeJoin(String stateIndex) {
        if (interestDetail_bean != null) {
            if (TextUtils.equals(stateIndex, "1")) {
                stateIndex = "0";
            } else {
                stateIndex = "1";
            }
            interestDetail_bean.setStatus_num(stateIndex);
            isJoin(stateIndex, join_Tv);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_Tv:
                if (interestDetail_bean != null) {
                    String id = interestDetail_bean.getId();
                    String statusJoin = interestDetail_bean.getStatus_num();
                    changeJoin(statusJoin);
                    getJoin(id);
                }
                break;
        }
    }

    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    //    /**
//     * 是否是自己
//     * @param authorId2
//     * @return
//     */
//    public boolean isMy(String authorId2){
//        String id = PrefUtil.getUser().getId();
//        String authorId = authorId2;
//        if (TextUtils.equals(id, authorId)) {//是自己不能评论
//            return true;
//        }
//        return false;
//    }


    /**
     * 收起整个折叠页
     */
//    private void isChangeFold() {
//        if (TextUtils.equals(isShowTop, "1")) {
//            appLayout.setExpanded(false);
//        }
//    }


}