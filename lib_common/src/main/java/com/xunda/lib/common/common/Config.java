package com.xunda.lib.common.common;

import java.io.File;

/**
 * 全局配置类
 *
 * @author ouyang
 */
public class Config {

    /**
     * 全局配置
     */
    public static class Setting {

        // 开发环境
        public static final boolean DEBUG = false;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  ;

        // 是否输出日志
        public static final boolean IS_LOG = false;


        // 图片保存地址
        public static final String IMG_CACHE = "huan" + File.separator

                + "Download";
    }


    /**
     *
     * URL配置
     */
    public static class Link {

        /**
         * 开发环境
         */
        public static String URL_WHOLE = "https://shjr.gsdata.cn/";




        /**
         * 生产环境
         */
        static {
            if (!Setting.DEBUG) {
                URL_WHOLE = "https://shjr.gsdata.cn/";
            }
        }

        public static String getWholeUrl() {
            return URL_WHOLE;
        }


    }


}
