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
	 * 本地配置信息
	 * @param configBean
	 */

	@Apply
	void setAppConfigBean(AppConfigBean configBean);
	AppConfigBean getAppConfigBean();


}
