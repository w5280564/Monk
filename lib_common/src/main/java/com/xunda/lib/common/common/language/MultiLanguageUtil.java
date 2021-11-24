package com.xunda.lib.common.common.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import com.xunda.lib.common.common.utils.StringUtil;

import java.util.Locale;

/**
 * Created by HARRY on 2018/6/5 0005.
 */

public class MultiLanguageUtil {

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            setConfiguration(context);
            return context;
        }
    }

    /**
     * 设置语言
     *
     * @param context
     */
    public static void setConfiguration(Context context) {
        Locale appLocale = getAppLocale(context);

        //如果本地有语言信息，以本地为主，如果本地没有使用默认Locale
        Locale locale = null;
        String spLanguage = SpUtil.getStringInContext(context, ConstantGlobal.LOCALE_LANGUAGE);
        String spCountry = SpUtil.getStringInContext(context, ConstantGlobal.LOCALE_COUNTRY);
        if (!TextUtils.isEmpty(spLanguage) && !TextUtils.isEmpty(spCountry)) {
            if (isSameLocal(appLocale, spLanguage, spCountry)) {
                locale = appLocale;
            } else {
                locale = new Locale(spLanguage, spCountry);
            }
        } else {
            locale = appLocale;
        }

        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale appLocale = getAppLocale(context);

        //如果本地有语言信息，以本地为主，如果本地没有使用默认Locale
        Locale locale = null;
        String spLanguage = SpUtil.getStringInContext(context, ConstantGlobal.LOCALE_LANGUAGE);
        String spCountry = SpUtil.getStringInContext(context, ConstantGlobal.LOCALE_COUNTRY);
        if (!TextUtils.isEmpty(spLanguage) && !TextUtils.isEmpty(spCountry)) {
            if (isSameLocal(appLocale, spLanguage, spCountry)) {
                locale = appLocale;
            } else {
                locale = new Locale(spLanguage, spCountry);
            }
        } else {
            locale = appLocale;
        }

        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
     * 更改应用语言
     *
     * @param
     * @param locale      语言地区
     * @param persistence 是否持久化
     */
    public static void changeAppLanguage(Context context, Locale locale, boolean persistence) {
        new WebView(context).destroy();
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, metrics);

        if (persistence) {
            saveLanguageSetting(context, locale);
        }
    }

    //保存多语言信息到sp中
    public static void saveLanguageSetting(Context context, Locale locale) {
        String country = locale.getCountry();
        SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_LANGUAGE, locale.getLanguage());
        SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_COUNTRY, country);

        if(!StringUtil.isBlank(country)){//0简体 1繁体 2英文
            if("CN".equals(country)){
                SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_COUNTRY_TYPE, "0");
            }else if("TW".equals(country)){
                SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_COUNTRY_TYPE, "1");
            }else if("US".equals(country)){
                SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_COUNTRY_TYPE, "2");
            }
        }else{
            SpUtil.saveStringInContext(context, ConstantGlobal.LOCALE_COUNTRY_TYPE, "");
        }
    }


    //获取语言国家type值，放请求头里
    public static String getCountryType(Context context) {
        Locale locale = getAppLocale(context);
        String localCountryType = SpUtil.getStringInContext(context, ConstantGlobal.LOCALE_COUNTRY_TYPE);//本地语言国家type
        if(!StringUtil.isBlank(localCountryType)){
            return localCountryType;
        }else{
            String systemCountry = locale.getCountry();//系统语言
            if("TW".equals(systemCountry)){//0简体 1繁体 2英文
                return "1";
            }else if("US".equals(systemCountry)){
                return "2";
            }else{
                return "0";
            }
        }
    }



    //获取本地应用的实际的多语言信息
    public static Locale getAppLocale(Context context) {
        //获取应用语言
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);
        } else {
            locale = configuration.locale;
        }
        return locale;
    }

    //判断sp中和app中的多语言信息是否相同
    public static boolean isSameWithSetting(Context context) {
        Locale locale = getAppLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();

        String sp_language = SpUtil.getString(ConstantGlobal.LOCALE_LANGUAGE);
        String sp_country = SpUtil.getString(ConstantGlobal.LOCALE_COUNTRY);
        if (language.equals(sp_language) && country.equals(sp_country)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameLocal(Locale appLocale, String sp_language, String sp_country) {
        String appLanguage = appLocale.getLanguage();
        String appCountry = appLocale.getCountry();
        if (appLanguage.equals(sp_language) && appCountry.equals(sp_country)) {
            return true;
        } else {
            return false;
        }
    }
}
