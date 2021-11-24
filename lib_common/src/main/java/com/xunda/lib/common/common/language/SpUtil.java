package com.xunda.lib.common.common.language;

import android.content.Context;
import android.content.SharedPreferences;

import com.xunda.lib.common.base.BaseApplication;


public class SpUtil {

    private static String name = "sp_config.cfg";
    private static SharedPreferences sp;



    public static synchronized void saveString(String key, String value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }



    public static String getString(String key) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }



    /**
     * 存
     * @param ctx
     * @param key
     * @param value
     */
    public static synchronized void saveStringInContext(Context ctx, String key, String value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 取
     * @param ctx
     * @param key
     */
    public static String getStringInContext(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }





}
