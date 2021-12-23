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
    public static final String uploadFiles = "login/login/upload-files";



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
    public static final String All_Group = "square/interested/allgroup";//全部兴趣圈
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
    public static final String Join_SheQun = "square/square/join-shequn";//加入社群

    /**
     * 删除话题
     */
    public static final String deleteTopic = "square/detail/delete-topic";


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
    public static final String bandLogin = "login/login/band-login";//绑定手机号并登录
    public static final String bandAccount = "user/user/band";//绑定第三方账号
    public static final String Fund_Postion = "fund/fund/character-position";//人物

}

