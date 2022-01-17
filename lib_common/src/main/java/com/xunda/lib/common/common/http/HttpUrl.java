package com.xunda.lib.common.common.http;

/**
 * 接口地址
 *
 * @author ouyang
 */
public class HttpUrl {




    /**
     * 获取区号列表
     */
    public static final String getAreaCodeList = "login/login/area-code";


    /**
     * 发送验证码
     */
    public static final String sendCode = "login/login/send-msg";


    /**
     * 手机号登录
     */
    public static final String mobileLogin = "login/login/mobile-login";


    /**
     * 绑定手机号并登录
     */
    public static final String mobileBandLogin = "login/login/band-login";





    /**
     * 行业列表
     */
    public static final String industryList = "login/login/industry-list";


    /**
     * 城市列表
     */
    public static final String cityList = "login/login/city";


    /**
     * 兴趣列表
     */
    public static final String interestedList = "login/login/interested-list";


    /**
     * 单文件上传
     */
    public static final String uploadFile = "login/login/upload-file";


    /**
     * 多文件上传
     */
    public static final String uploadFiles = "login/login/upload-files-app";



    /**
     * 收获列表
     */
    public static final String harvestList = "login/login/harvest-list";


    /**
     * 第三方登录
     */
    public static final String authLogin = "login/login/auth-login";

    /**
     * 修改个人信息
     */
    public static final String Edit_Info = "user/user/edit-info";
    public static final String Interest_All = "square/interested/allgroup";//全部兴趣圈
    public static final String Interest_Group = "square/interested/interest-recommend";//推荐--兴趣圈列表
    public static final String Join_Group = "square/interested/join-interested";//加入/退出兴趣圈
    public static final String Recommend_List = "index/index/recommend-list";//首页-推荐


    /**
     * 	发送问题请求
     */
    public static final String sendQuestion = "app/chat/sendQuestion";

    /**
     * 	全部社群
     */
    public static final String allGroup = "square/detail/allshequn";

    /**
     * 	我的社群
     */
    public static final String myGroup = "square/detail/my-shequns";


    /**
     * 创建社群
     */
    public static final String createShequn = "square/square/create-shequn";

    /**
     * 编辑社群
     */
    public static final String editShequn = "square/square/shequn-edit";


    /**
     * 所有社群标签
     */
    public static final String getShequnTag = "square/square/get-tag";

    /**
     * 社群上方信息
     */
    public static final String shequnInfo = "square/detail/shequn-info";
    public static final String User_Follow = "user/user/follow"; //关注/取消关注
    public static final String Mes_Like = "comment/comment/liked-comment"; //评论 点赞/取消点赞
    public static final String Follow_List = "index/index/follow-list"; //首页-关注
    public static final String Topic_Like = "square/square/topic-like"; //点赞/取消点赞
    public static final String Insider_List = "index/index/insider-list"; //首页-内部人
    public static final String Login_Logout = "login/login/logout"; //退出登录


    public static final String createOrder = "pay/wx-pay/create-order";//APP下单，获取预支付交易会话标识
    public static final String getWXPaySign = "pay/wx-pay/sign";//获取签名



    /**
     * 问答广场(全部)
     */
    public static final String getSquareListAll = "index/index/square-list";

    /**
     * 我的发布列表
     */
    public static final String getOwnPublishList = "square/detail/own-publish";
    public static final String getPosition_List = "square/position/position-list";//首页-仓位组合列表


    /**
     * 创建话题或提问
     */
    public static final String createTopic = "square/square/create-topic";
    public static final String combination_Topic_Like = "square/position/topic-like";//首页-仓位组合点赞
    public static final String User_Article_Detail = "user/user/article-detail";//首页-个人文章详情

    /**
     * 删除话题
     */
    public static final String deleteTopic = "square/detail/delete-topic";
    public static final String feeSet = "square/set/fee-set";//入群费用设置
    public static final String themeSet = "square/set/theme-set";//设置预览主题
    public static final String userDelete = "square/set/user-delete";//删除用户
    public static final String commonUser = "square/set/common-user";//普通用户列表(按名字首字母排序)
    public static final String followMutualList = "square/set/follow-mutual";//群主相互关注的列表
    public static final String invite = "square/set/invite";//邀请好友


    /**
     * 在线聊天
     */
    public static final String friendList = "user/chat-online/friend-list";//好友列表
    public static final String messageList = "user/chat-online/message-list";//聊天记录
    public static final String getAllUnreadNumber = "user/chat-online/get-unread";//获取所有未读消息数量
    public static final String conversationList = "user/chat-online/chat-list";//会话列表
    public static final String appVersion = "index/app-version/version";//获取当前版本

    /**
     * 编辑话题
     */
    public static final String editQuestion = "square/detail/up-publish";
    public static final String Article_CommentList = "comment/comment/article-comment-list";//获取文章评论
    public static final String Article_LikedList = "comment/article/liked-list";//文章点赞列表
    public static final String AddComment_Post = "comment/comment/post-comment";//添加评论
    public static final String Insider_Detail = "index/index/insider-detail";//内部人详情页
    public static final String Recommend_Follow = "user/user/recommend-follow";//关注发现-推荐关注
    public static final String Leader_TagList = "index/index/leader-tag-list";//侧滑—大佬标签列表
    public static final String Leader_List = "index/index/leader-list";//侧滑—大佬tab页面
    public static final String Expert_List = "index/index/expert-list";//侧滑—专家
    public static final String StockOrFund_Message = "fund/stock/get-zixun";//个股/基金--资讯
    public static final String StockOrFund_Question = "fund/stock/question-list";//个股/基金--问答
    public static final String Fund_Notice = "fund/fund/notice";//基金公告
    public static final String Fund_Position = "fund/fund/fund-position";//基金持仓
    public static final String Fund_Manager = "fund/fund/fund-manager";//基金经理
    public static final String Fund_Thigh = "fund/fund/thigh";//十大股东/十大流通股东/基金持股
    public static final String FundStock_MesLike = "fund/stock/zixun-like";//资讯点赞
    public static final String Fund_Postion = "fund/fund/character-position";//人物
    public static final String Character_List = "fund/fund/character-list";//人物列表
    public static final String CommentChildren_List = "comment/comment/comment-children-list";//回复评论列表-获取评论回复
    public static final String Square_Position_List = "square/position/position-detail";//仓位组合详情
    public static final String Show_Comment_List = "square/position/show-comment";//仓位组合评论详情
    public static final String LineChart_Position = "square/position/jingzhi-line";//仓位组合--净值折线
    public static final String groupDetailAllTab = "square/detail/topic-list";//全部
    public static final String getGroupToQuestionList = "square/square/getshequn-question-list";//去提问列表
    public static final String checkOtherGroupDetail = "square/square/shequn-detail";//未加入的社群详情
    public static final String getPreviewGroupDetail = "square/square/show-before";//社群预览
    public static final String joinGroup = "square/square/join-shequn";//加入社群
    public static final String Combination_AddComment = "square/position/add-comment";//仓位组合-添加评论
    public static final String waitAnswerList = "square/square/question-list";//等待回答问题列表
    public static final String answerQuestion = "square/square/answer-question";//回答社群问题
    public static final String showTheme = "square/square/show-theme";//预览主题
    public static final String editTheme = "square/square/theme-edit";//编辑主题
    public static final String Interest_My = "square/interested/mygroup-app";//我的兴趣圈
    public static final String Interest_Detail = "square/interested/interested-info";//兴趣圈详情
    public static final String groupUserList = "square/set/user-list";//群成员列表
    public static final String setAdmins = "square/set/add-admins";//添加管理员/合伙人
    public static final String Interest_AllMember = "square/interested/member-all";//添加管理员/合伙人
    public static final String User_Info = "user/user/info";//用户信息
    public static final String My_SheQun_Pc = "square/detail/my-shequns-pc";//我的社群
    public static final String UserCenter_Article = "user/user/originator-center";//个人中心文章列表
    public static final String UserColumn_List = "login/login/column-list";//专栏名称列表
    public static final String UserColumn_AddOrUpdate = "user/user/edit-column";//添加/更新专栏
    public static final String UserColumn_Del = "user/user/del-column";//删除专栏
    public static final String User_Comment = "user/user/my-comment";//我的评论
    public static final String User_History = "user/user/look-history";//浏览记录
    public static final String User_History_All = "user/user/look-history-all";//浏览全部记录
    public static final String User_FeedBack = "user/feedback/feedback-save";//发布意见反馈





}

