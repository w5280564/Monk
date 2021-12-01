package com.xunda.lib.common.common.preferences;

import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Remove;

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
     * 用户昵称
     */
    void setUserNickName(String nickName);
    String getUserNickName();
    @Remove
    void removeUserNickName();






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