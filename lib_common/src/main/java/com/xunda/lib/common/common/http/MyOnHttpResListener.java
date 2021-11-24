package com.xunda.lib.common.common.http;


/**
 * ouyang
 */
public abstract class MyOnHttpResListener implements OnHttpResListener {

	@Override
	public abstract void onComplete(String json_root, int code, String msg, String json_data);


}
