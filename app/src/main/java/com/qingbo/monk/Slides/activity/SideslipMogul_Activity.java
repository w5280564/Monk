package com.qingbo.monk.Slides.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.SideslipMogul_Fragment;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MogulTagListBean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;

/**
 * 侧边栏 大咖列表
 */
public class SideslipMogul_Activity extends BaseTabLayoutActivity {
    /**
     * @param context
     * @param articleId
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String articleId, String isShowTop) {
        Intent intent = new Intent(context, SideslipMogul_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sideslip_mogulr;
    }


    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
//        initMenuData();
    }

    @Override
    protected void getServerData() {
        getTagListData(true);
    }


    @SuppressLint("WrongConstant")
    private void initMenuData() {
        if (mogulTagListBean != null) {
            AppMenuBean allBean = new AppMenuBean();
            allBean.setName("全部");
            menuList.add(allBean);
            fragments.add(SideslipMogul_Fragment.newInstance(""));

            int size = mogulTagListBean.getTagList().size();
            for (int i = 0; i < size; i++) {
                AppMenuBean bean = new AppMenuBean();
                bean.setName(mogulTagListBean.getTagList().get(i).getTagName());
                String id = mogulTagListBean.getTagList().get(i).getId();
                fragments.add(SideslipMogul_Fragment.newInstance(id));
                menuList.add(bean);
            }
            initViewPager(0);
        }
    }

    MogulTagListBean mogulTagListBean;

    private void getTagListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.Leader_TagList, "侧滑—大佬标签列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    mogulTagListBean = GsonUtil.getInstance().json2Bean(json_data, MogulTagListBean.class);

                    initMenuData();
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onRightClick() {
        skipAnotherActivity(HomeSeek_Activity.class);
    }
}