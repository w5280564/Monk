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
        public static final boolean IS_LOG = true;


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
        public static String URL_WHOLE = "http://122.9.184.79:8088/";




        /**
         * 生产环境
         */
        static {
            if (!Setting.DEBUG) {
                URL_WHOLE = "https://abc.shandongdayi.com/";
            }
        }

        public static String getWholeUrl() {
            return URL_WHOLE;
        }


    }


    /**
     *
     * 参数配置
     */
    public static class Parameter {

        /**
         * 开发环境
         */

        public static String BUCKET_NAME = "dayi-test";

        public static String TENCENT_FRESH_FACE_APP_ID = "TIDAhYLx";


        /**
         * 生产环境
         */
        static {
            if (!Setting.DEBUG) {
                BUCKET_NAME = "southhuan";
                TENCENT_FRESH_FACE_APP_ID = "IDA2ZNHR";
            }
        }



        public static String getBUCKET_NAME() {
            return BUCKET_NAME;
        }


        public static String getTencentFreshFaceAppId() {
            return TENCENT_FRESH_FACE_APP_ID;
        }

    }

}
