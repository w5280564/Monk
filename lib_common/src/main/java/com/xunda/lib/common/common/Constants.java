package com.xunda.lib.common.common;


import android.Manifest;

/**
 *常量类
 */
public class Constants {

    public static String file_bucketName  = Config.Parameter.getBUCKET_NAME();// 文件上传桶

    public static String fresh_face_AppId  = Config.Parameter.getTencentFreshFaceAppId();// 腾讯云刷脸ID


    public static final int REQUEST_SUCCESS_CODE = 0;//接口请求成功
    public static final int REQUEST_FAILURE_TOKEN = 201;//其他账号登录，被挤下线
    public static final int REQUEST_FAILURE_SERVER = -8885;//服务器错误(OKhttp报的错)
    public static final int REQUEST_FAILURE_INTERNET = -8884;//网络错误（网络连接不上）

    public static final String WEB_SITE_URL = "http://www.shandongdayi.com/";

    public static final String WECHAT_APPID = "wx7d3f753b6c67c11b"; //微信开发者账号appid

    public static final String TANCENT_BUGLY_APPID = "ac35e72a1e"; //腾讯云bugly的AppId

    public static final String SYSTEM_NOTICE_CONVERSATION_ID = "888888888888888888"; //系统公告会话id

    public static final String SYSTEM_NOTICE_USER_CONVERSATION_ID = "southnotifyadmin"; //系统通知会话id

    public static final String APP_PKG = "com.qingbo.monk";

    public static final int MIN_CLICK_DELAY_TIME = 1000;

    public static final String BRAND_XIAOMI = "xiaomi";
    public static final String BRAND_HONOR = "HONOR";
    public static final String BRAND_HUAWEI = "Huawei";
    public static final String BRAND_OPPO = "OPPO";
    public static final String BRAND_VIVO = "vivo";


    public static final String endPoint = "obs.cn-east-3.myhuaweicloud.com";
    public static final String ak = "MAU13JILTZHVEIEY7CXY";
    public static final String sk = "vuVi643UlB6bSSQg0Grq3exlgjkrhim33xz3Hk95";

    //腾讯刷脸
    public static final String keyLicence_fresh_face = "Rx4oUaaUkuBWmflc7PqpPBJgYZ0uB5XoXknmZZ1Rtg24+U5nIcgIDlaZ+ZBVc2/pDGvsdP1BsHVkJ7ICh7c6AKw/Zx+gcibLEmIx7q2mWkcCKmFQN0LMVyCAbq/iGOM1Ns4LiiS2B4caRT0ktv4fUJrt0XBZUKuI3nCW8VtMwGuvLU63GY6l/3KJPETTtYCGLlfmK8Z8DeXIbgwBRHVTj7OO5k3l11ebVP9NDfOfpPDtx4yII7ykdwlAllITvlEU6uflZ2NV1JLSDgUgA1o7xnkef6UqSd7umWG/NT7MIk5xJQ9dXF4kRe46UzmJoY9NnjErwC4PqO5myI0xG4vD6A==";


    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public static final  int WRITE_PERMISSION_CODE=110;
    public static final  int PERMISSION_CAMERA_CODE =111;
    public static final  int PERMISSION_CAMERA_IDCARD_CODE =118;
    public static final  int PERMISSION_RECORD_AUDIO_CODE =112;
    public static final  int PERMISSION_READ_PHONE_STATE_CODE=113;
    public static final  int PERMISSION_CALL_PHONE_CODE=114;
    public static final  int PERMISSION_READ_CONTACTS_CODE=115;
    public static final  int PERMISSION_DOUNIU_CODE=116;
    public static final  int PERMISSION_CAMERA_SCAN_CODE=117;
    public static final  int PERMISSION_RECORD_AUDIO__CAMERA_CODE =118;
    public static final  int WRITE_PERMISSION_CODE_PHOTO=119;
    public static final  int WRITE_PERMISSION_CODE_VIDEO=120;
    public static final  int WRITE_PERMISSION_CODE_FILE=121;
    public static final  int LOCATION_CODE = 122;


    /**
     * 写入权限的权限码
     */

    public static final  String[] PERMS_WRITE_READ ={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};//读写权限（打开相册）
    public static final  String[] PERMS_CAMERA ={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};//相机权限（拍照）
    public static final  String[] PERMS_CAMERA_IDCARD ={Manifest.permission.CAMERA};//相机权限（拍照）
    public static final  String[] PERMS_RECORD_AUDIO ={Manifest.permission.RECORD_AUDIO};//麦克风权限（语音搜索）
    public static final  String[] PERMS_READ_PHONE_STATE ={Manifest.permission.READ_PRECISE_PHONE_STATE};//读取手机状态权限（获取imei,获取获取手机号码）
    public static final  String[] PERMS_CALL_PHONE ={Manifest.permission.CALL_PHONE};//拨打电话
    public static final  String[] PERMS_READ_CONTACTS ={Manifest.permission.READ_CONTACTS};//通讯录
    public static final  String[] PERMS_DOUNIU ={Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};//小视频录制页所需权限
    public static final  String[] PERMS_CAMERA_SCAN_CODE ={Manifest.permission.CAMERA};//相机权限（扫码）
    public static final  String[] PERMS_LOCATION_CODE ={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};//定位权限


    public static final int PHOTO_REQUEST_CAMERA = 1100;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 1101;// 从相册中选择



    public static final int SCAN_CODE_PASS = 100;//展会扫码通过
    public static final int SCAN_CODE_WARNING = 101;//扫码警告


}
