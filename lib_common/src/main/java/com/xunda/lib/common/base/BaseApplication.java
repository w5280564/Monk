package com.xunda.lib.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import androidx.fragment.app.FragmentActivity;
import androidx.multidex.MultiDexApplication;
import com.xunda.lib.common.common.language.MultiLanguageUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import java.util.LinkedList;
import java.util.List;


/**
 * BaseApplication
 */
public class BaseApplication extends MultiDexApplication {
    public static BaseApplication instance;

    /**
     * 将所有FragmentActivity存放在链表中，便于管理
     */
    private List<FragmentActivity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        int isAgree = SharePref.local().getIsAgreePrivacyPolicy();
        if(isAgree==1){//已同意过隐私政策
            initPrivatePolicySDK();
        }
    }



    /**
     * 获取一个Application对象
     */
    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    public Context getContext() {
        return instance.getApplicationContext();
    }

    /**
     * 添加一个Activity到数组里
     *
     * @param activity
     */
    public void addActivity(FragmentActivity activity) {
        if (this.activityList == null) {
            activityList = new LinkedList<FragmentActivity>();
        }
        this.activityList.add(activity);
    }

    /**
     * 删除一个Activity在数组里
     *
     * @param activity
     */
    public void delActivity(FragmentActivity activity) {
        if (activityList != null) {
            activityList.remove(activity);
        }
    }

    public int getActivityCount() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

    public void clearActivity() {
        if (activityList != null) {
            for (Activity activity : activityList) {
                activity.finish();
            }
            activityList.clear();
        }
    }

    /**
     * 删除某些activity
     */
    public void deleteSomeActivity() {
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity == activityList.get(activityList.size() - 1) || activity == activityList.get(activityList.size() - 2)) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 同意隐私政策才能初始化的SDK
     */
    public void initPrivatePolicySDK(){

    }


    @Override
    protected void attachBaseContext(Context base) {
        //系统语言等设置发生改变时会调用此方法，需要要重置app语言
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(base));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
