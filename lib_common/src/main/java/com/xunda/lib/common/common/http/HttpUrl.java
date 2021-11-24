package com.xunda.lib.common.common.http;

import com.xunda.lib.common.common.Config;

/**
 * 接口地址
 *
 * @author ouyang
 */
public class HttpUrl {

    public static String server = Config.Link.getWholeUrl();// 全局接口地址


//-------------------------------------------首页模块-------------------------------------------------------------
    /**
     * 刷新首页数据
     */
    public static final String getHome = server + "product/home";

    /**
     * 商品分类二联
     */
    public static final String getCateGoryList = server + "product/cateGoryList";


    /**
     * 根据分类ID获取商品列表
     */
    public static final String getGoodslistByCateGory = server + "product/listByCateGory";

    /**
     * 购物车列表
     */
    public static final String getCartList = server + "cart/list";

    /**
     * 修改产品数量
     */
    public static final String postCartChangeCount = server + "cart/changeCount";

    /**
     * 批量删除购物车
     */
    public static final String postCartDelete = server + "cart/delete";

    /**
     * 清空购物车
     */
    public static final String postCartClear = server + "cart/clear";

    /**
     * 我的收货地址
     */
    public static final String getAddressList = server + "address/list";

    /**
     * 添加、修改收货地址
     */
    public static final String postAddressAddOrUpdate = server + "address/addOrUpdate";

    /**
     * 设置默认地址
     */
    public static final String postAddressDefault = server + "address/default";

    /**
     * 删除地址
     */
    public static final String postAddressDelete = server + "address/delete";


    /**
     * 省市区(全部)
     */
    public static final String getAllDistricts = server + "common/allDistricts";


    /**
     * 订单列表
     */
    public static final String getOrderList = server + "order/list";

    /**
     * 修改订单收货地址
     */
    public static final String postOrderChangeAddress = server + "order/changeAddress";

    /**
     * 订单中添加到购物车
     */
    public static final String postOrderAddToCart = server + "order/addToCart";

    /**
     * 删除商品订单
     */
    public static final String postOrderDelete = server + "order/delete";

    /**
     * 订单详情
     */
    public static final String getOrderInfo = server + "order/info";

    /**
     * 申请退款
     */
    public static final String postOrderApplyRefund = server + "order/applyRefund";


//-------------------------------------------聊天模块-------------------------------------------------------------

    /**
     * 搜索群组
     */
    public static final String getSearchGroup = server + "group/searchGroup";

    /**
     * 查找用户
     */
    public static final String getSearchUser = server + "user/search";

    /**
     * 好友详情
     */
    public static final String getFriendInfo = server + "friend/info";


    /**
     * 添加好友
     */
    public static final String postFriendAdd = server + "friend/add";


    /**
     * 群组信息及我的相关
     */
    public static final String getMyGroupInfo = server + "group/myGroupInfo";

    /**
     * 好友申请列表
     */
    public static final String getFriendApplyList = server + "friend/applyList";

    /**
     * 修改群组信息
     */
    public static final String postGroupUpdateInfo = server + "group/updateInfo";



    /**
     * 根据southId获取群信息
     */
    public static final String searchGroupBySouthId = server + "group/searchGroupBySouthId";

    /**
     * 修改群昵称
     */
    public static final String postGroupUpdateNickName = server + "group/updateNickname";

    /**
     * 申请加群（扫码加群）
     */
    public static final String postGroupApplyInto = server + "group/applyInto";



    /**
     * 商品详情
     */
    public static final String getGoodsDetail = server + "product/info";




    /**
     * 获取用户个人信息
     */
    public static final String getPersonInfo = server + "app/PersonInfo/index";



    /**
     * 版本更新
     */
    public static final String versionUpdate = server + "public/isForceUpdate";




    public static final String intellectSearch = server + "app/AppIndex/searchSuggest";




    /**
     * 发送验证码
     */
    public static final String sendCode = server + "user/smsCode";


    /**
     * 账号密码登录
     */
    public static final String accountLogin = server + "user/login";


    /**
     * 注册
     */
    public static final String register = server + "user/register";


    /**
     * 添加购物车
     */
    public static final String addCart = server + "cart/add";

    /**
     * 直接购买
     */
    public static final String straightBuy = server + "order/create";

    /**
     * 从购物车购买
     */
    public static final String cartBuy = server + "cart/orderBatch";


    /**
     * 校验忘记密码 验证码
     */
    public static final String checkForPassCode = server + "public/checkChangeCode";

    /**
     * 忘记密码修改
     */
    public static final String resetPassword = server + "public/forgetPassword";

    /**
     * 获取banner接口
     */
    public static final String getBanner = server + "product/banner";

    /**
     * 退出登录接口
     */
    public static final String logout = server + "user/outLogin";


    /**
     * 账号注销
     */
    public static final String userDelete= server + "user/delete";


    /**
     * 设置支付密码
     */
    public static final String setPayPassWord = server + "user/setPayPassWord";

    /**
     * 实名认证
     */
    public static final String realName = server + "identification/idCard";


    /**
     * 用零钱支付
     */
    public static final String payByAccount = server + "order/payByAccount";


    /**
     * 银行卡支付
     */
    public static final String payByBankCard = server + "order/payByBankCard";

    /**
     * 零钱支付余额详情
     */
    public static final String balanceInfo = server + "order/balanceInfo";

    /**
     * 我的钱包
     */
    public static final String getMineWallet = server + "account/myAccount";

    /**
     * 获取所有银行卡列表
     */
    public static final String getAllBankList = server + "account/bankList";

    /**
     * 零钱明细列表
     */
    public static final String changeDetailList = server + "account/changeDetailsListById";

    /**
     * 实名认证（非）
     */
    public static final String realNameNot = server + "user/isIdCardTrue";

    /**
     * 修改支付密码
     */
    public static final String changePayWord = server + "user/changePayPassword";

    /**
     * 获取刷脸参数
     */
    public static final String getFaceUse = server + "identification/faceIdInfo";

    /**
     * 人脸核身
     */
    public static final String faceNucleus = server + "identification/faceNucleus";

    /**
     * 我的银行卡列表
     */
    public static final String MineBankList = server + "account/bankCardList";

    /**
     * 充值第一步
     */
    public static final String ChargeOne = server + "account/getRechargeId";

    /**
     * 充值第二步
     */
    public static final String ChargeTwo = server + "account/recharge";

    /**
     * 提现
     */
    public static final String withdrawal = server + "account/withdrawal";

    /**
     * 零钱明细详情
     */
    public static final String ChargeDetail = server + "account/changeDetailsInfo";

    /**
     * 红包记录列表
     */
    public static final String EnvelopList = server + "redPacket/packetLogByPayId";

    /**
     * 获取用户信息
     */
    public static final String UserInfo = server + "user/getUserInfo";

    /**
     * 提交反馈信息
     */
    public static final String quesSend = server + "user/questionBackByLogin";

    /**
     * 修改个人信息
     */
    public static final String ChangeNickName = server + "user/update";

    /**
     * 校验新手机号
     */
    public static final String checkNewPhone = server + "user/checkNewPhone";

    /**
     * 校验旧手机号
     */
    public static final String checkOldPhone = server + "user/checkOldPhone";

    /**
     * 校验登录密码
     */
    public static final String checkLoginPassword = server + "user/checkLoginPassword";

    /**
     * 修改登录密码
     */
    public static final String setLoginPassword = server + "user/setLoginPassword";

    /**
     * 获取用户个人设置
     */
    public static final String getUserInfoSet = server + "user/searchConfig";

    /**
     * 用户个人设置
     */
    public static final String SetUserInfoSet = server + "user/updateConfig";

    /**
     * 好友黑名单
     */
    public static final String FriendBlacks = server + "friend/blacks";

    /**
     * 加入移除黑名单
     */
    public static final String RemoveOrJoin = server + "friend/setBlack";


    /**
     * 给好友发红包
     */
    public static final String sendToFriendRedPacket = server + "redPacket/sendToFriend";

    /**
     * 群发红包
     */
    public static final String sendToGroupRedPacket = server + "redPacket/sendToGroup";

    /**
     * 获取订单数
     */
    public static final String orderCountByStatus = server + "order/orderCountByStatus";

    /**
     * 好友红包详情
     */
    public static final String friendRedPacketDetail = server + "redPacket/friendRedPacketDetail";

    /**
     * 领取好友红包
     */
    public static final String receiveFriendRedPacket = server + "redPacket/receiveFriendRedPacket";

    /**
     * 群组红包详情（可以开则显示开）
     */
    public static final String groupRedPacketDetail = server + "redPacket/groupRedPacketDetail";

    /**
     *  看看大家的手气（红包详情）
     */
    public static final String otherRedPacketDetail = server + "redPacket/otherRedPacketDetail";


    /**
     * 抢群红包
     */
    public static final String receiveGroupRedPacket = server + "redPacket/receiveGroupRedPacket";

    /**
     * 转账给好友
     */
    public static final String transferToFriend = server + "transfer/toFriend";


    /**
     * 转账详情
     */
    public static final String transferInfo = server + "transfer/info";


    /**
     * 接收或退还转账
     */
    public static final String transferReceiveOrReturn = server + "transfer/receiveOrReturn";


    /**
     * 客服
     */
    public static final String KeFuUrl = server + "receptionist/myMoRoom";

    /**
     * 身份证识别获取签名
     */
    public static final String ocrSign = server + "identification/ocrSign";


    /**
     * 封禁说明
     */
    public static final String banDetails = server + "user/banDetails";




    /**
     * 最近公告
     */
    public static final String lastNotice = server + "public/lastNotice";

    /**
     * 公告详情
     */
    public static final String noticeDetail = server + "public/noticeDetail";

    /**
     * 公告列表
     */
    public static final String noticeList = server + "public/noticeList";

    /**
     * 解绑银行卡
     */
    public static final String unboundBankCard = server + "account/unboundBankCard";

    /**
     * 敏感词列表
     */
    public static final String sensitiveWord = server + "public/sensitiveWord";

    /**
     * 添加银行卡
     */
    public static final String addBankCard = server + "account/addBankCard";


    /**
     * 获取真实姓名
     */
    public static final String getRealName = server + "user/realName";


    /**
     * 绑定银行卡检验验证码
     */
    public static final String checkBankCardSmsCode = server + "account/checkBankCardSmsCode";


    public static final String groupUserList = server + "group/userList";


    /**
     * 设置群管理员
     */
    public static final String groupMangerSet = server + "group/mangerSet";


    /**
     * 首页商品分页列表
     */
    public static final String homeProductList = server + "product/productList";

    /**
     * 结束人工
     */
    public static final String endArtificial = server + "receptionist/endArtificial";

    /**
     * 是否在人工服务
     */
    public static final String isService = server + "receptionist/isService";


    /**
     * 获取答案
     */
    public static final String receptionistAnswer = server + "receptionist/answer";


    /**
     * 系统通知列表
     */
    public static final String userSystemNotify = server + "userSystemNotify/list";


    /**
     * 根据卡号获取银行名
     */
    public static final String getBankByNumber = server + "account/bankName";


    /**
     * 校验其他验证码
     */
    public static final String checkOtherCode = server + "user/checkOtherCode";

    /**
     * 根据环信id和群组id获取我在群中的昵称
     */
    public static final String getMyGroupNicknameByHxIdOrGroupId = server + "group/myGroupNicknameByHxIdOrGroupId";



    /**
     * 邀请群成员
     */
    public static final String groupMangerUser = server + "group/mangerUser";

    /**
     * 配置接口
     */
    public static final String getSouthConfig = server + "account/southConfig";


    /**
     * 封禁提现校验身份
     */
    public static final String withdrawalCheckIdentity = server + "public/withdrawalCheckIdentity";


    /**
     * 封禁提现校验支付密码
     */
    public static final String withdrawalCheckPayPassword = server + "public/withdrawalCheckPayPassword";


    /**
     * 好友红包记录详情
     */
    public static final String friendRedPacketLogDetail = server + "redPacket/friendRedPacketLogDetail";


    /**
     * 群组红包记录详情
     */
    public static final String groupRedPacketLogDetail = server + "redPacket/groupRedPacketLogDetail";
}

