package com.xunda.lib.common.common.http;


/**
 *
 * @author ouyang 服务器返回数据监听
 */
public interface OnHttpResListener {

	/**
	 *
	 * @param json_root 返回数据的根json
	 * @param code 服务器返回码
	 * @param msg  返回信息（提示语）
	 * @param json_data 返回数据的结果json
	 */
	 void onComplete(String json_root, int code, String msg, String json_data);


}