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
	 * userId
	 */
	void setsouthID(String userId);
	String getsouthID();


	/**
	 * 用户姓名
	 */
	void setUserName(String userName);
	String getUserName();


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
	 * token
	 */


	void setToken(String token);
	@Default("")//必需要写在get方法上面
	String getToken();


	void setIsAuthentication(int isAuthentication);
	int getIsAuthentication();


	void setIsPayPassword(int isPayPassword);
	int getIsPayPassword();

	/**
	 * 本地保存用户信息
	 * @param user
	 */
	void setUserInfo(UserBean user);
	UserBean getUserInfo();



	/**
	 * 封禁提示语
	 */
	void setDeleteRemark(String remark);
	String getDeleteRemark();


	/**
	 * 封禁状态
	 */
	void setDeleteStatus(int deleteStatus);
	int getDeleteStatus();


	/**
	 * 极光推送收到的通知数组list
	 */
	void setJpushReceiverListJson(String json);
	String getJpushReceiverListJson();
	@Remove
	void removeJpushReceiverListJson();

}
