package com.xunda.lib.common.common.utils;

import android.content.Context;
import android.util.Log;

import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Config;

/**
 * Log封装类
 * @author ouyang
 *
 */
public class L {

	private static final String TAG = "Monk";
	private static Context mContext;
	
	/**
	 * 禁止实例化
	 */
	private L(){}
	
	/**
	 * 打印消息类传入的字符串
	 * @param msg
	 */
	public static void i(String msg) {
		i(TAG, msg);
	}

	/**
	 * 打印DEBUG类传入的字符串
	 * @param msg
	 */
	public static void d(String msg) {
		d(TAG, msg);
	}

	/**
	 * 打印ERROR类传入的字符串
	 * @param msg
	 */
	public static void e(String msg) {
		e(TAG, msg);
	}

	/**
	 * 打印详细类传入的字符串
	 * @param msg
	 */
	public static void v(String msg) {
		v(TAG, msg);
	}
	
	/**
	 * 打印消息类传入的字符串
	 * @param msg
	 */
	public static void i(int msg) {
		i(TAG, msg);
	}
	
	/**
	 * 打印DEBUG类传入的字符串
	 * @param msg
	 */
	public static void d(int msg) {
		d(TAG, msg);
	}
	
	/**
	 * 打印ERROR类传入的字符串
	 * @param msg
	 */
	public static void e(int msg) {
		e(TAG, msg);
	}
	
	/**
	 * 打印详细类传入的字符串
	 * @param msg
	 */
	public static void v(int msg) {
		v(TAG, msg);
	}
	
	/**
	 * 打印消息类传入的字符串
	 * @param tag
	 * @param message
	 */
	public static void i(String tag, int message) {
		String str = getContext().getResources().getString(message);
		i(tag, str);
	}
	
	/**
	 * 打印DEBUG类传入的字符串
	 * @param tag
	 * @param message
	 */
	public static void d(String tag, int message) {
		String str = getContext().getResources().getString(message);
		d(tag, str);
	}
	
	/**
	 * 打印ERROR类传入的字符串
	 * @param tag
	 * @param message
	 */
	public static void e(String tag, int message) {
		String str = getContext().getResources().getString(message);
		e(tag, str);
	}
	
	/**
	 * 打印详细类传入的字符串
	 * @param tag
	 * @param message
	 */
	public static void v(String tag, int message) {
		String str = getContext().getResources().getString(message);
		v(tag, str);
	}
	
	/**
	 * 打印消息类传入的字符串
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (Config.Setting.IS_LOG)
			Log.i(tag, unicodeToUTF_8(msg));
	}

	/**
	 * 打印DEBUG类传入的字符串
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (Config.Setting.IS_LOG)
			Log.d(tag, msg);
	}

	/**
	 * 打印ERROR类传入的字符串
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (Config.Setting.IS_LOG)
			Log.e(tag, unicodeToUTF_8(msg));
	}

	/**
	 * 打印详细类传入的字符串
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (Config.Setting.IS_LOG)
			Log.v(tag, msg);
	}

	private static Context getContext() {
		if(mContext == null) 
			mContext = BaseApplication.getInstance().getContext();
		return mContext;
	}



	public static String unicodeToUTF_8(String src) {
		if (StringUtil.isBlank(src)) {
			return null;
		}
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < src.length();) {
			char c = src.charAt(i);
			if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
				String hex = src.substring(i + 2, i + 6);
				try {
					out.append((char) Integer.parseInt(hex, 16));
				} catch (NumberFormatException nfe) {
					nfe.fillInStackTrace();
				}
				i = i + 6;
			} else {
				out.append(src.charAt(i));
				++i;
			}
		}
		return out.toString();

	}
}
