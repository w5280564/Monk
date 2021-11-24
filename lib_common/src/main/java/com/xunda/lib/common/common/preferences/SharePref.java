package com.xunda.lib.common.common.preferences;

import android.content.Context;

import com.baoyz.treasure.Treasure;
import com.xunda.lib.common.base.BaseApplication;

/**
 * preferences工具类
 * 
 * @author ouyang Treasure相关教程URL： https://github.com/baoyongzhang/Treasure
 */
public class SharePref {

	/**
	 * 与服务端相关的数据
	 * 
	 * @return
	 */
	public static ServerPreferences server() {
		Context context = BaseApplication.getInstance().getApplicationContext();
		return Treasure.get(context, ServerPreferences.class, "server");
	}


	/**
	 * 用户相关数据
	 */
	public static UserPreferences user() {
		Context context = BaseApplication.getInstance().getApplicationContext();
		return Treasure.get(context, UserPreferences.class, "user");
	}


	/**
	 * 本地设置的数据
	 *
	 * @return
	 */
	public static LocalPreferences local() {
		Context context = BaseApplication.getInstance().getApplicationContext();
		return Treasure.get(context, LocalPreferences.class, "local");
	}


}
