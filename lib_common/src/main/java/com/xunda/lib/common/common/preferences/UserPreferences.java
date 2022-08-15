package com.xunda.lib.common.common.preferences;

import com.baoyz.treasure.Clear;
import com.baoyz.treasure.Default;
import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Remove;
import com.xunda.lib.common.bean.UserBean;


/**
 * 用户相关
 * @author ouyang
 *
 */
@Preferences
public interface UserPreferences {
	@Clear
	void clear();



	/**
	 * 用户姓名
	 */
	void setUserName(String userName);
	String getUserName();

	/**
	 * 是否是创作者
	 */
	void setIsOriginator(String isOriginator);
	String getIsOriginator();


	/**
	 * userId
	 */
	void setUserId(String userId);
	String getUserId();


	/**
	 * 用户头像
	 */
	void setUserHead(String userHead);
	String getUserHead();


	/**
	 * 是否绑定微信
	 */
	void setUserBandWX(int bandWX);
	int getUserBandWX();


	/**
	 * token
	 */
	void setToken(String token);
	@Default("")//必需要写在get方法上面
	String getToken();




	/**
	 * 本地保存用户信息
	 * @param user
	 */
	void setUserInfo(UserBean user);
	UserBean getUserInfo();





	/**
	 * 极光推送收到的通知数组list
	 */
	void setJpushReceiverListJson(String json);
	String getJpushReceiverListJson();
	@Remove
	void removeJpushReceiverListJson();

}
