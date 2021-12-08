package com.qingbo.monk.base;

import android.content.Context;
import android.content.res.Configuration;
import com.baoyz.treasure.Treasure;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Config;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.OkhttpInitUtil;
import com.xunda.lib.common.common.language.MultiLanguageUtil;
import com.xunda.lib.common.common.preferences.GsonConverterFactory;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * BaseApplication
 *
 */
public class MonkApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Treasure.setConverterFactory(new GsonConverterFactory());//Treasure：数据保存类
        OkhttpInitUtil.init(instance);//初始化网络请求类
    }




    /**
     * 同意隐私政策才能初始化的SDK
     */
    @Override
    public void initPrivatePolicySDK() {
        CrashReport.initCrashReport(getContext(), Constants.TANCENT_BUGLY_APPID, Config.Setting.DEBUG);//腾讯bugly 建议在测试阶段设置成true，发布时设置为false。
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


}
