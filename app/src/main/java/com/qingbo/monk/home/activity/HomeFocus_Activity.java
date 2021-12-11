package com.qingbo.monk.home.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.qingbo.monk.home.fragment.ArticleDetail_Comment_Fragment;
import com.qingbo.monk.home.fragment.ArticleDetail_Zan_Fragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 个人文章详情
 */
public class HomeFocus_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.person_Img)
    ImageView person_Img;
    @BindView(R.id.person_Name)
    TextView person_Name;
    @BindView(R.id.time_Tv)
    TextView time_Tv;
    @BindView(R.id.lable_Lin)
    LinearLayout lable_Lin;
    @BindView(R.id.follow_Tv)
    TextView follow_Tv;
    @BindView(R.id.send_Mes)
    TextView send_Mes;
    @BindView(R.id.title_Tv)
    TextView title_Tv;
    @BindView(R.id.content_Tv)
    TextView content_Tv;
    @BindView(R.id.nine_grid)
    RecyclerView mNineView;
    @BindView(R.id.follow_Img)
    ImageView follow_Img;
    @BindView(R.id.follow_Count)
    TextView follow_Count;
    @BindView(R.id.mes_Img)
    ImageView mes_Img;
    @BindView(R.id.mes_Count)
    TextView mes_Count;
    @BindView(R.id.groupHead_Img)
    ImageView groupHead_Img;
    @BindView(R.id.groupName_Tv)
    TextView groupName_Tv;
    @BindView(R.id.groupDes_Tv)
    TextView groupDes_Tv;
    @BindView(R.id.join_Tv)
    TextView join_Tv;
    @BindView(R.id.headListView)
    DiscussionAvatarView headListView;
    @BindView(R.id.card_Tab)
    TabLayout card_Tab;
    @BindView(R.id.card_ViewPager)
    ViewPager card_ViewPager;
    @BindView(R.id.appLayout)
    AppBarLayout appLayout;


    private String articleId;
    private String isShowTop;

    /**
     * @param context
     * @param articleId
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String articleId, String isShowTop) {
        Intent intent = new Intent(context, HomeFocus_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_focus;
    }


    @Override
    protected void initLocalData() {
        articleId = getIntent().getStringExtra("articleId");
        isShowTop = getIntent().getStringExtra("isShowTop");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        follow_Tv.setOnClickListener(this);
        follow_Img.setOnClickListener(this);
        join_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getUserDetail(false);
    }

    @Override
    public void onClick(View v) {
        if (homeFoucsDetail_bean == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.follow_Tv:
                String authorId = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                postFollowData(authorId);
                break;
            case R.id.follow_Img:
                String likeId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
                postLikedData(likeId);
                break;
            case R.id.join_Tv:
                if (homeFoucsDetail_bean.getData().getDetail().getExtra() == null) {
                    return;
                }
                String action = homeFoucsDetail_bean.getData().getDetail().getAction();//1是社群 2是兴趣圈
                String id = homeFoucsDetail_bean.getData().getDetail().getExtra().getId();
                if (TextUtils.equals(action, "1")) {
                    getJoinSheQun(id);
                }else if (TextUtils.equals(action, "2")){
                    getJoinGroup(id);
                }
                break;
        }
    }

    private List<Object> tabFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    private void initTab() {
        List<String> tabsList = new ArrayList<>();
        tabsList.add("评论");
        tabsList.add("赞");
        card_Tab.setTabMode(TabLayout.MODE_AUTO);
        card_Tab.setTabIndicatorFullWidth(false);//下标跟字一样宽
        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        card_Tab.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
//        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity,R.color.text_color_444444));
        //添加tab
        int sizes = tabsList.size();
        for (int i = 0; i < sizes; i++) {
            card_Tab.addTab(card_Tab.newTab().setText(tabsList.get(i)));
        }
        String articleId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
        String type = homeFoucsDetail_bean.getData().getDetail().getType();
        tabFragmentList.add(ArticleDetail_Comment_Fragment.newInstance(articleId,type));
        tabFragmentList.add(ArticleDetail_Zan_Fragment.newInstance(articleId,type));
//        tabFragmentList.add(HomeCommendFragment.newInstance(articleId,type));

        card_ViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return (Fragment) tabFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return tabFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabsList.get(position);
            }
        });
        //设置TabLayout和ViewPager联动
        card_Tab.setupWithViewPager(card_ViewPager, false);
    }


    HomeFoucsDetail_Bean homeFoucsDetail_bean;
    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", "1");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Article_Detail, "个人文章详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFoucsDetail_bean = GsonUtil.getInstance().json2Bean(json_root, HomeFoucsDetail_Bean.class);
                    if (homeFoucsDetail_bean != null) {
                        HomeFoucsDetail_Bean.DataDTO.DetailDTO detailData = homeFoucsDetail_bean.getData().getDetail();
                        String is_anonymous = detailData.getIsAnonymous();//1是匿名
                        if (TextUtils.equals(is_anonymous, "1")) {
                            person_Name.setText("匿名用户");
                            person_Img.setEnabled(false);
                        } else {
                            GlideUtils.loadCircleImage(mContext, person_Img, detailData.getAvatar(), R.mipmap.icon_logo);
                            person_Name.setText(detailData.getTitle());
                            person_Name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//昵称字数
                            labelFlow(lable_Lin, mContext, detailData.getTagName());
                            isFollow(detailData.getFollowStatus(), follow_Tv, send_Mes);
                        }
                        title_Tv.setText(detailData.getTitle());
                        content_Tv.setText(detailData.getContent());
                        String userDate = DateUtil.getUserDate(detailData.getCreateTime()) + " " + detailData.getCompanyName();
                        time_Tv.setText(userDate);
                        //多张图片
                        if (!TextUtils.isEmpty(detailData.getImages())) {
                            showNineView(detailData.getImages());
                        }
                        follow_Count.setText(detailData.getLikedNum());
                        mes_Count.setText(detailData.getCommentNum());
                        isLike(detailData.getLikedStatus(), detailData.getLikedNum(), follow_Img, follow_Count);

                        if (detailData.getExtra() != null) {
                            GlideUtils.loadCircleImage(mContext, groupHead_Img, detailData.getExtra().getImage(), R.mipmap.icon_logo);
                            groupName_Tv.setText(detailData.getExtra().getName());
                            groupDes_Tv.setText(detailData.getExtra().getDes());
                            groupHead(detailData.getExtra().getUserAvatar());
                            isJoinGroup(detailData.getExtra().getIsJoin());
                        }
                        initTab();
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void postFollowData(String otherUserId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void postLikedData(String likeId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    if (likedStateBena != null) {
                        //0取消点赞成功，1点赞成功
                        int nowLike;
                        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
                        if (likedStateBena.getLiked_status() == 0) {
                            nowLike -= 1;
                            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
                        } else if (likedStateBena.getLiked_status() == 1) {
                            follow_Img.setBackgroundResource(R.mipmap.dianzan);
                            nowLike += 1;
                        }
                        follow_Count.setText(nowLike + "");
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void getJoinSheQun(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_SheQun, "加入/退出社群", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    String isJoin = homeFoucsDetail_bean.getData().getDetail().getIsJoin();
                    changeJoinGroup(isJoin);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }
    private void getJoinGroup(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣圈", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    String isJoin = homeFoucsDetail_bean.getData().getDetail().getIsJoin();
                    changeJoinGroup(isJoin);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.GONE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }

    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(nowLike + "");
    }

    /**
     * 大V标签
     *
     * @param myFlow
     * @param mContext
     * @param tag
     */
    public void labelFlow(LinearLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
        if (length > 2) {
            length = 2;
        }
        for (int i = 0; i < length; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.group_label, null);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.setMargins(0, 0, 0, 0);
            view.setLayoutParams(itemParams);
            TextView label_Name = view.findViewById(R.id.label_Name);
            StringUtil.setColor(mContext, i, label_Name);
            label_Name.setText(tagS[i]);
            label_Name.setTag(i);
            myFlow.addView(view);
            label_Name.setOnClickListener(v -> {
            });
        }
    }

    /**
     * 加载多图适配器
     *
     * @param imgString
     */
    private void showNineView(String imgString) {
        String[] imgS = imgString.split(",");
        List<String> strings = Arrays.asList(imgS);
        mNineView.setLayoutManager(new NineGridLayoutManager(mNineView.getContext()));
        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        mNineView.setAdapter(nineGridAdapter);
        nineGridAdapter.setNewData(strings);
        nineGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                jumpToPhotoShowActivity(position, strings);
            }
        });
    }

    /**
     * 横向多个头像
     *
     * @param userS
     */
    private void groupHead(String userS) {
        if (!TextUtils.isEmpty(userS)) {
            String[] imgS = userS.split(",");
//            List<String> mList = Arrays.asList(imgS);
            List<String> mList = new ArrayList<>();
            int length = imgS.length;
            if (length > 3) {
                length = 3;
            }
            for (int i = 0; i < length; i++) {
                mList.add(imgS[i]);
            }
            headListView.initDatas(mList);
        }
    }

    /**
     * 是否加入群或圈
     *
     * @param isJoin
     */
    private void isJoinGroup(String isJoin) {
        if (TextUtils.equals(isJoin, "0")) {
            join_Tv.setText("已加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            join_Tv.setText("加入");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }


    private void changeJoinGroup(String isJoin) {
        if (TextUtils.equals(isJoin, "0")) {
            homeFoucsDetail_bean.getData().getDetail().setIsJoin("1");
        } else {
            homeFoucsDetail_bean.getData().getDetail().setIsJoin("0");
        }
        isJoinGroup(homeFoucsDetail_bean.getData().getDetail().getIsJoin());
    }


}