package com.xunda.lib.common.common.preferences;

import com.baoyz.treasure.Apply;
import com.baoyz.treasure.Clear;
import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Remove;
import com.xunda.lib.common.bean.AppConfigBean;

@Preferences
public interface ServerPreferences {

	@Clear
	void clear();

	/**
	 * 购物车数量
	 */
	void setCartNumber(int number);
	int getCartNumber();
	@Remove
	void removeCartNumber();





	/**
	 * 用户昵称
	 */
	void setUserNickName(String nickName);
	String getUserNickName();
	@Remove
	void removeUserNickName();




}
