package com.xunda.lib.common.router;

/**
 * ================================================
 *
 * @Description:
 * @Author: Zhangliangliang
 * @CreateDate: 2021/9/1 16:33
 * <p>
 * ================================================
 */
final public class RouterActivityPath {
    public static class Main {
        private static final String MAIN = "/main";
        /**
         * 主页面
         */
        public static final String PAGER_MAIN = MAIN + "/main";

        /**
         * 登录页
         */
        public static final String PAGER_LOGIN = MAIN + "/login";

        public static final String Nan_QRCode = MAIN + "/qrcode";

        public static final String NickName = MAIN + "/nickName";

        /**
         * 实名认证
         */
        public static final String RealName = MAIN + "/RealName";

        /**
         * 私发红包
         */
        public static final String SendRedPacketSingle = MAIN + "/redPacketSingle";


        /**
         * 好友红包详情
         */
        public static final String RedPacketDetailSingle = MAIN + "/RedPacketDetailSingle";


        /**
         * 专属红包详情
         */
        public static final String RedPacketDetailExclusive = MAIN + "/RedPacketDetailExclusive";

        /**
         * 群发红包
         */
        public static final String SendRedPacketGroup = MAIN + "/redPacketGroup";


        /**
         * 群红包详情
         */
        public static final String RedPacketDetailGroup = MAIN + "/RedPacketDetailGroup";


        /**
         * 发专属红包
         */
        public static final String SendRedPacketExclusive = MAIN + "/redPacketExclusive";

        /**
         * 修改群信息——群名、群昵称
         */
        public static final String EDIT_GROUP_INFO = MAIN + "/edit/group/info";

        /**
         * 二维码扫码申请加群验证
         */
        public static final String ADDITIVE_GROUP = MAIN + "/additive/group";

        /**
         * 添加好友验证页
         */
        public static final String ADD_FRIEND_DETAIL = MAIN + "/addfriend/detail";

        /**
         * 转账给好友
         */
        public static final String TRANSFER_ACCOUNTS = MAIN + "/transfer/accounts";

        /**
         * 转账详情
         */
        public static final String TRANSFER_ACCOUNTS_DETAIL = MAIN + "/transfer/accounts/detail";


        /**
         * 分享二维码至好友
         */
        public static final String SHARE_QRCODE_TO_FRIEND = MAIN + "/share/qrcode/tofriend";

        /**
         * 分享名片或二维码至群组
         */
        public static final String SHARE_TO_GROUP = MAIN + "/share/togroup";


        /**
         * 转发消息至群组
         */
        public static final String FORWARD_MESSAGE_TO_GROUP = MAIN + "/forwardMessage/togroup";

        /**
         * 系统公告列表
         */
        public static final String SYSTEM_NOTICE_LIST = MAIN + "/system/notice_list";


        /**
         * 系统通知列表
         */
        public static final String SYSTEM_NOTICE_USER_LIST = MAIN + "/system/notice_list_user";


        /**
         * 管理群设置
         */
        public static final String Manager_setting = MAIN + "/Manager/setting";


        /**
         * 投诉反馈
         */
        public static final String Setting_FANKUI = MAIN + "/Setting/fankui";

    }

    public static class Chat {

        private static final String CHAT = "/chat";


        /**
         * 新朋友
         */
        public static final String NEW_FRIEND = CHAT + "/new/friend";



        /**
         * 添加管理员
         */
        public static final String AddManager = CHAT + "/AddManager";
    }


}
