package com.xunda.lib.common.common.preferences;

import com.xunda.lib.common.bean.UserBean;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 缓存数据操作的组合封装类
 *
 * @author ouyang
 */
public class PrefUtil {


    /**
     * 是否已登录
     */
    public static boolean isLogin() {
        String user_id = SharePref.user().getUserId();
        if (StringUtil.isBlank(user_id)) {
            return false;
        }
        return true;
    }

    /**
     * 保存用户个人资料数据
     *
     * @param user
     */
    public static void saveUser(UserBean user) {

        if (user != null) {
            SharePref.user().setUserInfo(user);
        }

        if (!StringUtil.isBlank(user.getNickname())) {
            SharePref.user().setUserName(user.getNickname());
        }

        if (!StringUtil.isBlank(user.getAvatar())) {
            SharePref.user().setUserHead(user.getAvatar());
        }

        if (!StringUtil.isBlank(user.getId())) {
            SharePref.user().setUserId(user.getId());
        }

        if (!StringUtil.isBlank(user.getToken())) {
            SharePref.user().setToken(user.getToken());
        }

    }



    /**
     * 清除本地数据和用户数据
     */
    public static void clearSharePrefInfo() {
        SharePref.user().clear();
        SharePref.server().clear();
    }

    /**
     * 获取本地用户数据
     *
     * @return
     */
    public static UserBean getUser() {
        return SharePref.user().getUserInfo();
    }
}
