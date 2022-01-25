package com.qingbo.monk.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.WebSocketHelper;
import com.qingbo.monk.home.activity.MainActivity;
import com.qingbo.monk.login.activity.WelcomeActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xunda.lib.common.bean.BaseUserBean;
import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.WechatLoginEvent;
import com.xunda.lib.common.common.eventbus.WechatPayEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;

	private IWXAPI api;
	private MyHandler handler;

	private class MyHandler extends Handler {
		private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

		public MyHandler(WXEntryActivity wxEntryActivity){
			wxEntryActivityWeakReference = new WeakReference<WXEntryActivity>(wxEntryActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
				case LOGIN_SUCCESS:
					Bundle data = msg.getData();
					String json_data = data.getString("result");
					BaseUserBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseUserBean.class);
					saveUserInfo(obj);
					break;
				case LOGIN_FAILURE:
					String openid = (String) msg.obj;
					WelcomeActivity.actionStart(WXEntryActivity.this,openid,1);
					WXEntryActivity.this.finish();
					break;

			}
		}


		/**
		 * 保存用户信息
		 *
		 * @param baseUserBean 用户对象
		 */
		private void saveUserInfo(BaseUserBean baseUserBean) {
			if (baseUserBean!=null) {
				UserBean userObj = baseUserBean.getInfo();
				if (userObj==null) {
					return;
				}
				PrefUtil.saveUser(userObj,baseUserBean.getAccessToken());
				String interested = userObj.getInterested();
				if(StringUtil.isBlank(interested)) {//首次登陆
					WelcomeActivity.actionStart(WXEntryActivity.this,"",1);
				}else{
					MainActivity.actionStart(WXEntryActivity.this,"",1);
				}
				EventBus.getDefault().post(new WechatLoginEvent(WechatLoginEvent.WECHAT_Login_RESULT));
				WXEntryActivity.this.finish();
			}

		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APPID, false);
		api.registerApp(Constants.WECHAT_APPID);

		handler = new MyHandler(this);
		try {
			Intent intent = getIntent();
			api.handleIntent(intent, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq baseReq) {
		switch (baseReq.getType()) {
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

				break;
			default:
				break;
		}
	}



	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			String result = "";
			switch (resp.errCode) {//ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
				case BaseResp.ErrCode.ERR_OK:
					SendAuth.Resp authResp = (SendAuth.Resp)resp;
					String code = authResp.code;
					wechatAuthLogin(code);
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result = "您已取消授权微信登录";
					T.sl(result);
					finish();
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					result = "您已拒绝授权微信登录";
					T.sl(result);
					finish();
					break;
			}
		}else if(resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
			finish();
		}

	}


	/**
	 * 微信第三方登录
	 * @param code
	 */
	private void wechatAuthLogin(String code) {
		HashMap<String, String> baseMap = new HashMap<>();
		baseMap.put("type", "weixinApp");
		baseMap.put("code", code);
		HttpSender sender = new HttpSender(HttpUrl.authLogin, "微信第三方登录", baseMap,
				new MyOnHttpResListener() {
					@Override
					public void onComplete(String json_root, int code, String msg, String json_data) {
						Message mMessage = Message.obtain();
						if (code == Constants.REQUEST_SUCCESS_CODE) {
							T.ss("登录成功");
							Bundle data = new Bundle();
							data.putString("result", json_data);
							mMessage.what = LOGIN_SUCCESS;
							mMessage.setData(data);
							handler.sendMessage(mMessage);
						}else if(code == -10086){
							mMessage.what = LOGIN_FAILURE;
							String openid = GsonUtil.getInstance().getValue(json_data,"openid");
							mMessage.obj = openid;
							handler.sendMessage(mMessage);
						}else{
							T.ss(msg);
							finish();
						}
					}

				}, true);
		sender.setContext(this);
		sender.sendPost();
	}





}