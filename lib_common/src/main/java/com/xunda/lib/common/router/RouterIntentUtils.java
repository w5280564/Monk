package com.xunda.lib.common.router;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * ================================================
 *
 * @Description: arouter跳转方法
 * @Author: Zhangliangliang
 * @CreateDate: 2021/9/1 16:35
 * <p>
 * ================================================
 */
public class RouterIntentUtils {

    /**
     * 不携带参数跳转
     *
     * @param path
     */
    public static void jumpTo(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    public static void jumpTo(String path, Bundle bundle) {
        ARouter.getInstance().build(path).with(bundle).navigation();
    }
}
