package com.xunda.lib.common.common.preferences;

import com.baoyz.treasure.Apply;
import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Remove;
import com.xunda.lib.common.bean.AppConfigBean;

@Preferences
public interface LocalPreferences {



    /**
     * uuid
     */
    void setMyUUID(String uuid);
    String getMyUUID();





    /**
     * 省市区三级联动
     */
    void setAreaJson(String json);

    String getAreaJson();

    @Remove
    void removeAreaJson();




    /**
     * 本地配置信息
     * @param configBean
     */

    @Apply
    void setAppConfigBean(AppConfigBean configBean);
    AppConfigBean getAppConfigBean();



    /**
     * 是否已同意隐私政策
     */
    void setIsAgreePrivacyPolicy(int isAgree);
    int getIsAgreePrivacyPolicy();



    /**
     * 敏感词汇列表
     */
    void setSensitiveWordsListJson(String json);
    String getSensitiveWordsListJson();



}