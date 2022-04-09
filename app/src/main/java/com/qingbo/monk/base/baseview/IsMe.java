package com.qingbo.monk.base.baseview;

import android.text.TextUtils;

import com.xunda.lib.common.common.preferences.PrefUtil;

public class IsMe {

    /**
     * 是否是自己
     *
     * @param authorId2
     * @return
     */
    public static boolean isMy(String authorId2) {
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {
            return true;
        }
        return false;
    }
}
