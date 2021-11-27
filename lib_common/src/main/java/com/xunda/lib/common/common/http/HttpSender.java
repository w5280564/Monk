package com.xunda.lib.common.common.http;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.xunda.lib.common.R;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Config;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.AESEncrypt;
import com.xunda.lib.common.common.utils.Base64;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.RSAUtils;
import com.xunda.lib.common.common.utils.RsaEncodeMethod;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.LoadingDialog;
import com.xunda.lib.common.dialog.TokenDialog;
import com.xunda.lib.common.router.RouterActivityPath;
import com.xunda.lib.common.router.RouterIntentUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by ouyang on 2017/1/6 0006.
 */

public class HttpSender {

	private Context context;
	private OnHttpResListener mListener;
	private boolean isShowLoadAnimal;
	private String requestUrl = "",requestName="",dialogMessage="";
	private Map<String, Object> paramsMap;
	private LoadingDialog mDialog;
	private Map<String, String > headerMap = new HashMap<>() ;//添加header头


	public HttpSender(String requestUrl, String requestName, Object mRequestObj,
					  OnHttpResListener mListener, boolean isShowLoadAnimal) {
		super();
		this.requestUrl = Config.Link.getWholeUrl()+requestUrl;
		this.mListener = mListener;
		this.isShowLoadAnimal = isShowLoadAnimal;
		this.requestName = requestName;
		if (mRequestObj != null) {
			this.paramsMap = GsonUtil.getInstance().Obj2Map(mRequestObj);
		}

		if (!StringUtil.isBlank(SharePref.user().getToken())){
			headerMap.put("Authorization", SharePref.user().getToken());
		}
	}



	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}



	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}



	public void sendPost() {
		requestPost();
	}



	public void sendGet() {
		requestGet();
	}


	/**
	 * POST请求（文件上传不加密）
	 */
	public void sendPostImage() {
		this.dialogMessage = "努力加载中...";
		requestPost();
	}






	/**
	 * get请求
	 */
	private void requestGet() {
		HashMap<String, String> upLoadMap = getRequestData();
		OkHttpUtils.get().url(requestUrl)
				.params(upLoadMap)
				.headers(headerMap)
				.build().execute(new StringDialogCallback(isShowLoadAnimal));
	}



	/**
	 * POST请求（不加密）
	 */
	private void requestPost() {
		setRequestData_POST();
		String request_data = GsonUtil.getInstance().toJson(paramsMap);
		if (!StringUtil.isBlank(request_data)) {
			OkHttpUtils.postString()
					.url(requestUrl)
					.content(request_data)
					.mediaType(MediaType.parse("application/json; charset=utf-8"))
					.headers(headerMap)
					.build().execute(new StringDialogCallback(isShowLoadAnimal));
		}
	}




	/**
	 * POST请求(旧加密)
	 */
	private void requestPostEncryptOld() {
		setRequestData_POST();
//		L.e("加密前"+GsonUtil.getInstance().toJson(paramsMap));
		String encrypt_data = encryptRequestData(GsonUtil.getInstance().toJson(paramsMap));
		if (!StringUtil.isBlank(encrypt_data)) {
			OkHttpUtils.postString()
					.url(requestUrl)
					.content(encrypt_data)
					.mediaType(MediaType.parse("application/json; charset=utf-8"))
					.headers(headerMap)
					.build().execute(new StringDialogCallback(isShowLoadAnimal));
		}
	}



	/**
	 * POST请求(新加密)
	 */
	private void requestPostEncryptNew() {
		setRequestData_POST();
		String aesKey = AESEncrypt.getAesKey();
		String contentStr = AESEncrypt.encrypt(GsonUtil.getInstance().toJson(paramsMap), aesKey);
		String rsaKey = RsaEncodeMethod.rsaEncode(aesKey);
		Map<String, String> finalMap = new HashMap<>();
		finalMap.put("key",rsaKey);
		finalMap.put("content",contentStr);

		String request_data = GsonUtil.getInstance().toJson(finalMap);
		if (!StringUtil.isBlank(request_data)) {
			OkHttpUtils.postString()
					.url(requestUrl)
					.content(request_data)
					.mediaType(MediaType.parse("application/json; charset=utf-8"))
					.headers(headerMap)
					.build().execute(new StringDialogCallback(isShowLoadAnimal));
		}
	}





	private String encryptRequestData(String request_data) {
		//用公钥加密
		try {
			byte[] encrypt = RSAUtils.encryptByPublicKey(request_data.getBytes(), Base64.decode(RSAUtils.PUBLIC_KEY));
			return Base64.encode(encrypt);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}





	/**
	 * 获取请求的数据
	 * @return
	 */
	private HashMap<String, String> getRequestData() {
		HashMap<String, String> upLoadMap = new HashMap<>();
		if (StringUtil.isBlank(requestUrl)) {
			L.e(requestName + "请求 Url不对");
			return null;
		}
		if (paramsMap != null) {
			L.i("请求名称：" + requestName);
			L.i("请求Url：" + requestUrl);

			for (String key : paramsMap.keySet()) {
				Object requestParams = paramsMap.get(key);
				if(requestParams!=null){
					upLoadMap.put(key,requestParams.toString());
					L.i(key + " = " + requestParams.toString());
				}
			}
		}
		return upLoadMap;
	}


	/**
	 * 设置请求的数据(POST)
	 * @return
	 */
	private void setRequestData_POST() {
		if (StringUtil.isBlank(requestUrl)) {
			L.e(requestName + "请求 Url不对");
			return ;
		}
		if (Config.Setting.IS_LOG){
			if (paramsMap != null) {
				L.i("请求名称：" + requestName);
				L.i("请求Url：" + requestUrl);

				for (String key : paramsMap.keySet()) {
					Object requestParams = paramsMap.get(key);
					if(requestParams!=null){
						L.i(key + " = " + requestParams.toString());
					}

				}
			}
		}
	}






	private void backError(String failure_message,int failure_code){
		if (mListener != null) {
			mListener.onComplete("", failure_code, failure_message, null);
		}
	}





	public  class StringDialogCallback extends StringCallback {

		private boolean mShowLoadAnimal;// true的时候不弹出小人


		public StringDialogCallback(boolean mShowLoadAnimal) {
			this.mShowLoadAnimal = mShowLoadAnimal;
		}


		@Override
		public void onError(Call call, Exception e, int id) {
			String errorInfo =e.getMessage();
			L.e(requestName+"接口出现异常，异常信息：" +errorInfo);
			String errorMess = context.getString(R.string.HttpSender_ERROR_404);
			int failure_code = Constants.REQUEST_FAILURE_SERVER;
			if (!StringUtil.isBlank(errorInfo)) {
				if (errorInfo.contains("org.apache.http.conn.ConnectTimeoutException")||errorInfo.contains("SocketTimeoutException")||errorInfo.contains("timeout")) {
					errorMess = context.getString(R.string.HttpSender_FWQCS);
					failure_code = Constants.REQUEST_FAILURE_SERVER;
				}else if (errorInfo.contains("No address associated with hostname")||errorInfo.contains("Failed to connect to")) {
					errorMess = context.getString(R.string.HttpSender_FWQDW);
					failure_code = Constants.REQUEST_FAILURE_INTERNET;
				}else if (errorInfo.contains("request failed , reponse's code is : 404")) {
					errorMess = context.getString(R.string.HttpSender_ERROR_404);
					failure_code = Constants.REQUEST_FAILURE_SERVER;
				}
			}
			backError(errorMess,failure_code);
		}

		@Override
		public void onResponse(String json, int id) {
			L.i(requestName + "接口返回结果：" + json);
			String stringCode = GsonUtil.getInstance().getValue(json, "code");//获取状态码
			String msg = GsonUtil.getInstance().getValue(json, "msg");

			int code = (stringCode!=null)?Integer.parseInt(stringCode):500;
			if(code == Constants.REQUEST_FAILURE_TOKEN){//其他账号登录，被挤下线
				removeUserData();
				showTokenDialog(msg);
			}else{
				String data = GsonUtil.getInstance().getValue(json, "data");
				if (mListener != null) {
					mListener.onComplete(json, code, msg, StringUtil.isBlank(data)?"":data);
				}
				dismissDialog();
			}
		}


		@Override
		public void onBefore(Request request, int id) {
			super.onBefore(request, id);
			if (mShowLoadAnimal) {
				showDialog();
			}
		}

		@Override
		public void onAfter(int id) {
			super.onAfter(id);
			//网络请求结束后关闭对话框
			dismissDialog();
		}
	}

	/**
	 * 清除用户数据
	 */
	private void removeUserData() {
		PrefUtil.clearSharePrefInfo();
	}


	/**
	 * token失效提示框（被挤下线）
	 */
	private void showTokenDialog(String description) {
		if(!(context instanceof Activity)){
			return;
		}
		TokenDialog dialog = new TokenDialog(context, description, context.getString(R.string.Sure), new TokenDialog.DialogConfirmListener() {
			@Override
			public void onConfirmClick() {
				BaseApplication.getInstance().clearActivity();
				jumpToLoginActivity();
			}
		});
		dialog.show();
	}

	/**
	 * 跳到登录界面
	 */
	private void jumpToLoginActivity() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("fromExitAct", true);
		RouterIntentUtils.jumpTo(RouterActivityPath.Main.PAGER_LOGIN,bundle);
	}


	/**
	 * 显示等待对话框
	 */
	private void showDialog() {
		if (context == null) {
			return;
		}
		if (mDialog == null) {
			mDialog = new LoadingDialog(context,dialogMessage);
		}
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		if (!mDialog.isShowing()){
			if (context instanceof FragmentActivity) {
				if (!((FragmentActivity) context).isDestroyed()) {
					mDialog.show();
				}

			} else if (context instanceof Activity) {
				if (!((Activity) context).isDestroyed()) {
					mDialog.show();
				}

			}
		}

	}



	/**
	 * 关闭等待对话框
	 */
	private void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			if (context instanceof FragmentActivity) {
				if (!((FragmentActivity) context).isDestroyed()) {
					mDialog.dismiss();
				}

			} else if (context instanceof Activity) {
				if (!((Activity) context).isDestroyed()) {
					mDialog.dismiss();
				}

			}

		}

	}
}
