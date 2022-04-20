package com.xunda.lib.common.common;


import android.Manifest;

/**
 *常量类
 */
public class Constants {
    public static final int REQUEST_SUCCESS_CODE = 0;//接口请求成功
    public static final int REQUEST_ERROR_IDENTIFICATION = 10003;//认证错误，请先登录
    public static final int REQUEST_ERROR_NO_LOGIN = 10008;//未登录
    public static final int REQUEST_ERROR_TOKEN = 401;//token已失效
    public static final int REQUEST_FAILURE_SERVER = -8885;//服务器错误(OKhttp报的错)
    public static final int REQUEST_FAILURE_INTERNET = -8884;//网络错误（网络连接不上）

    public static final String WEB_SITE_URL = "https://www.baidu.com";

    public static final String WECHAT_APPID = "wx9891dbe23d8ce255"; //微信开发者账号appid

    public static final String TANCENT_BUGLY_APPID = "e702e5c066"; //腾讯云bugly的AppId


    public static final String APP_PKG = "com.qingbo.monk";

    public static final int MIN_CLICK_DELAY_TIME = 1000;


    public static final String BRAND_XIAOMI = "xiaomi";
    public static final String BRAND_HONOR = "HONOR";
    public static final String BRAND_HUAWEI = "Huawei";
    public static final String BRAND_OPPO = "OPPO";
    public static final String BRAND_VIVO = "vivo";

    public static final String QQ_APP_ID = "101997485";

    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public static final  int WRITE_PERMISSION_CODE=110;
    public static final  int PERMISSION_CAMERA_CODE =111;
    public static final  int PERMISSION_RECORD_AUDIO_CODE =112;
    public static final  int PERMISSION_READ_PHONE_STATE_CODE=113;
    public static final  int PERMISSION_CALL_PHONE_CODE=114;
    public static final  int PERMISSION_READ_CONTACTS_CODE=115;
    public static final  int PERMISSION_CAMERA_SCAN_CODE=117;
    public static final  int PERMISSION_RECORD_AUDIO__CAMERA_CODE =118;
    public static final  int WRITE_PERMISSION_CODE_PHOTO=119;
    public static final  int WRITE_PERMISSION_CODE_VIDEO=120;
    public static final  int WRITE_PERMISSION_CODE_FILE=121;
    public static final  int LOCATION_CODE = 122;


    /**
     * 写入权限的权限码
     */

    public static final  String[] PERMS_WRITE_READ_CAMERA ={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};//读写权限和相册权限（打开相册）
    public static final  String[] PERMS_CAMERA ={Manifest.permission.CAMERA};//相机权限（拍照）
    public static final  String[] PERMS_RECORD_AUDIO ={Manifest.permission.RECORD_AUDIO};//麦克风权限（语音搜索）
    public static final  String[] PERMS_READ_PHONE_STATE ={Manifest.permission.READ_PRECISE_PHONE_STATE};//读取手机状态权限（获取imei,获取获取手机号码）
    public static final  String[] PERMS_CALL_PHONE ={Manifest.permission.CALL_PHONE};//拨打电话
    public static final  String[] PERMS_READ_CONTACTS ={Manifest.permission.READ_CONTACTS};//通讯录
    public static final  String[] PERMS_CAMERA_SCAN_CODE ={Manifest.permission.CAMERA};//相机权限（扫码）
    public static final  String[] PERMS_LOCATION_CODE ={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};//定位权限


    public static final int PHOTO_REQUEST_CAMERA = 1100;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 1101;// 从相册中选择




}
