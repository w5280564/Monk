package com.qingbo.monk.home.activity;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.SideslipPersonAndFund_Activity;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.baseview.AppBarStateChangeListener;
import com.qingbo.monk.base.baseview.ByteLengthFilter;
import com.qingbo.monk.base.rich.handle.CustomHtml;
import com.qingbo.monk.base.rich.handle.RichEditImageGetter;
import com.qingbo.monk.base.rich.view.RichEditText;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.qingbo.monk.home.fragment.ArticleDetail_Comment_Fragment;
import com.qingbo.monk.home.fragment.ArticleDetail_Zan_Fragment;
import com.qingbo.monk.message.activity.ChatActivity;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.question.activity.CheckOtherGroupDetailActivity;
import com.qingbo.monk.question.activity.GroupDetailActivity;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.view.discussionavatarview.DiscussionAvatarView;

import org.apache.commons.codec.language.bm.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * ??????????????????
 */
public class ArticleDetail_Activity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.person_Img)
    ImageView person_Img;
    @BindView(R.id.person_Name)
    TextView person_Name;
    @BindView(R.id.time_Tv)
    TextView time_Tv;
    @BindView(R.id.company_Tv)
    TextView company_Tv;
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
    @BindView(R.id.group_Con)
    ConstraintLayout group_Con;
    @BindView(R.id.title_Img)
    ImageView title_Img;
    @BindView(R.id.titleNickName_Tv)
    TextView titleNickName_Tv;
    @BindView(R.id.center_Tv)
    TextView center_Tv;
    @BindView(R.id.titleFollow_Tv)
    TextView titleFollow_Tv;
    @BindView(R.id.titleSend_Mes)
    TextView titleSend_Mes;
    @BindView(R.id.sendComment_Et)
    EditText sendComment_Et;
    @BindView(R.id.release_Tv)
    public TextView release_Tv;
    @BindView(R.id.back_Tv)
    TextView back_Tv;
    @BindView(R.id.source_Tv)
    TextView source_Tv;
    @BindView(R.id.comeFrom_Tv)
    TextView comeFrom_Tv;
    @BindView(R.id.top_Con)
    ConstraintLayout top_Con;
    @BindView(R.id.share_Tv)
    TextView share_Tv;
    @BindView(R.id.collect_Tv)
    TextView collect_Tv;

    private String articleId;
    private String isShowTop;
    private String type;
    boolean isReply = false;
    boolean isExpanded = false; //????????????
    private boolean isExpert;
    private boolean isInformation;
    public boolean isStockOrFund;

    /**
     * @param context
     * @param articleId
     * @param isShowTop ???????????????????????? ?????????0 ???????????????1
     */
    public static void startActivity(Context context, String articleId, String isShowTop, String type) {
        Intent intent = new Intent(context, ArticleDetail_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param articleId
     * @param isShowTop
     * @param type
     * @param isExpert  true????????? ??????????????????
     */
    public static void startActivity(Context context, String articleId, String isShowTop, String type, boolean isExpert) {
        Intent intent = new Intent(context, ArticleDetail_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("type", type);
        intent.putExtra("isExpert", isExpert);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param articleId
     * @param isShowTop
     * @param isInformation true????????? ???????????? ???????????????
     */
    public static void startActivity(Context context, String articleId, String isShowTop, boolean isInformation) {
        Intent intent = new Intent(context, ArticleDetail_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("isInformation", isInformation);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param articleId
     * @param isInformation
     * @param isStockOrFund ???????????????????????????????????????  ???????????????type ??????
     */
    public static void startActivity(Context context, String articleId, boolean isInformation, boolean isStockOrFund) {
        Intent intent = new Intent(context, ArticleDetail_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isInformation", isInformation);
        intent.putExtra("isStockOrFund", isStockOrFund);
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
        type = getIntent().getStringExtra("type");
        isExpert = getIntent().getBooleanExtra("isExpert", false);
        isInformation = getIntent().getBooleanExtra("isInformation", false);
        isStockOrFund = getIntent().getBooleanExtra("isStockOrFund", false);
    }

    @Override
    protected void initView() {
//        regSina();
        person_Name.setFilters(new InputFilter[]{new ByteLengthFilter(14)});//????????????
        viewTouchDelegate.expandViewTouchDelegate(back_Tv, 50);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(share_Tv, 50);
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//??????????????????????????????????????????????????????
    }

    @Override
    protected void initEvent() {
        follow_Tv.setOnClickListener(this);
        follow_Img.setOnClickListener(this);
        join_Tv.setOnClickListener(this);
        titleFollow_Tv.setOnClickListener(this);
        appLayout.addOnOffsetChangedListener(new appLayoutListener());
        mes_Img.setOnClickListener(this);
        release_Tv.setOnClickListener(this);
        back_Tv.setOnClickListener(this);
        person_Img.setOnClickListener(this);
        send_Mes.setOnClickListener(this);
        source_Tv.setOnClickListener(this);
        share_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
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
            case R.id.back_Tv:
                finish();
                break;
            case R.id.follow_Tv:
                String authorId = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                postFollowData(authorId, follow_Tv, send_Mes);
                break;
            case R.id.follow_Img:
                String likeId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
                postLikedData(likeId);
                break;
            case R.id.join_Tv:
                groupIsJoin();
                break;
            case R.id.titleFollow_Tv:
                String authorId1 = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                postFollowData(authorId1, titleFollow_Tv, titleSend_Mes);
                break;
            case R.id.mes_Img:
                String authorId2 = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                showInput(sendComment_Et, false);
                sendComment_Et.setHint("");
                break;
            case R.id.release_Tv:
                String s = sendComment_Et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    T.s("??????????????????", 2000);
                    return;
                }
                if (TextUtils.isEmpty(this.articleId) || TextUtils.isEmpty(type)) {
                    T.s("??????ID??????", 2000);
                    return;
                }
                if (isReply) {
                    ArticleDetail_Comment_Fragment o = (ArticleDetail_Comment_Fragment) tabFragmentList.get(0);
                    o.onClick(release_Tv);
                } else {
                    addComment(this.articleId, type, s);
                }
                break;
            case R.id.person_Img:
//                String authorId4 = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
//                MyAndOther_Card.actionStart(mActivity, authorId4, isExpert);
                HomeFoucsDetail_Bean.DataDTO.DetailDTO item = homeFoucsDetail_bean.getData().getDetail();
                startPerson(item);
                break;
            case R.id.send_Mes:
                String authorId3 = homeFoucsDetail_bean.getData().getDetail().getAuthorId();
                String authorName = homeFoucsDetail_bean.getData().getDetail().getAuthorName();
                String avatar = homeFoucsDetail_bean.getData().getDetail().getAvatar();
                ChatActivity.actionStart(mActivity, authorId3, authorName, avatar);
                break;
            case R.id.source_Tv:
                if (homeFoucsDetail_bean != null) {
                    String title = homeFoucsDetail_bean.getData().getDetail().getTitle();
                    String source_url = homeFoucsDetail_bean.getData().getDetail().getSource_url();
                    jumpToWebView(title, source_url);
                }
                break;
            case R.id.share_Tv:
                if (homeFoucsDetail_bean != null) {
                    HomeFoucsDetail_Bean.DataDTO.DetailDTO detail = homeFoucsDetail_bean.getData().getDetail();
                    showShareDialog(detail);
                }
                break;
            case R.id.collect_Tv:
                String articleId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
                postCollectData(articleId);
                break;
        }
    }


    /**
     * ??????
     */
    private void showShareDialog(HomeFoucsDetail_Bean.DataDTO.DetailDTO item) {
        String articleId = item.getArticleId();
        if (isStockOrFund) {

        }
        String imgUrl = item.getAvatar();
        String downURl = HttpUrl.appDownUrl;
        String title = item.getTitle();
        String content = item.getContent();
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(this, articleId, false, downURl, imgUrl, title, content, "??????");
        mShareDialog.setAuthor_id(item.getAuthorId());
        if (isStockOrFund) {
            mShareDialog.setArticleType("1");
            mShareDialog.setCollectType("3");
            mShareDialog.setForGroupType("3");
        }
        mShareDialog.show();
    }

    /**
     * ????????????
     *
     * @param item
     */
    private void startPerson(HomeFoucsDetail_Bean.DataDTO.DetailDTO item) {
        String data_source = item.getData_source();//1???????????????,???0???????????????
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getAuthorName();
            String id = item.getAuthorId();
            SideslipPersonAndFund_Activity.startActivity(mActivity, nickname, id, "0");
        } else {
            String id = item.getAuthorId();
            MyAndOther_Card.actionStart(mActivity, id, isExpert);
        }
    }


    /**
     * ??????????????????????????????
     */
    private void groupIsJoin() {
        if (homeFoucsDetail_bean.getData().getDetail().getExtra() == null) {
            return;
        }
        String action = homeFoucsDetail_bean.getData().getDetail().getAction();//1????????? 2????????????
        String id = homeFoucsDetail_bean.getData().getDetail().getExtra().getId();
        String isJoin = homeFoucsDetail_bean.getData().getDetail().getExtra().getIsJoin();
        if (TextUtils.equals(action, "1")) { //1????????? 2????????????
//                    getJoinSheQun(id);
            if (TextUtils.equals(isJoin, "1")) { //1?????????
                GroupDetailActivity.actionStart(mActivity, id);
            } else {
                CheckOtherGroupDetailActivity.actionStart(mActivity, id);
            }
        } else if (TextUtils.equals(action, "2")) {
            getJoinGroup(id);
        }
    }


    /**
     * ???????????????
     *
     * @param authorId2
     * @return
     */
    public boolean isMy(String authorId2) {
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {//?????????????????????
            return true;
        }
        return false;
    }


    /**
     * ??????????????????
     *
     * @param editView
     * @param editView ??????????????????  true??????????????????
     */
    public void showInput(View editView, boolean isReply) {
        this.isReply = isReply;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus???????????? //addAddressRemarkInfo is EditText
    }

    private List<Object> tabFragmentList = new ArrayList<>();

    @SuppressLint("WrongConstant")
    private void initTab() {
        List<String> tabsList = new ArrayList<>();
        tabsList.add("??????");
        tabsList.add("???");
        card_Tab.setTabMode(TabLayout.MODE_AUTO);
        card_Tab.setTabIndicatorFullWidth(false);//?????????????????????
        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        card_Tab.setTabTextColors(ContextCompat.getColor(mActivity, R.color.text_color_6f6f6f), ContextCompat.getColor(mActivity, R.color.text_color_444444));
//        card_Tab.setSelectedTabIndicatorColor(ContextCompat.getColor(mActivity,R.color.text_color_444444));
        //??????tab
        int sizes = tabsList.size();
        for (int i = 0; i < sizes; i++) {
            card_Tab.addTab(card_Tab.newTab().setText(tabsList.get(i)));
        }
        String articleId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
        String type = homeFoucsDetail_bean.getData().getDetail().getType();
        tabFragmentList.add(ArticleDetail_Comment_Fragment.newInstance(articleId, type, isStockOrFund));
        tabFragmentList.add(ArticleDetail_Zan_Fragment.newInstance(articleId, type));

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
        card_Tab.setupWithViewPager(card_ViewPager, false);
    }


    public HomeFoucsDetail_Bean homeFoucsDetail_bean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        if (isStockOrFund) {
            requestMap.put("type", "1");
        }
        HttpSender httpSender = new HttpSender(HttpUrl.User_Article_Detail, "??????????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    homeFoucsDetail_bean = GsonUtil.getInstance().json2Bean(json_root, HomeFoucsDetail_Bean.class);
                    if (homeFoucsDetail_bean != null) {
                        HomeFoucsDetail_Bean.DataDTO.DetailDTO detailData = homeFoucsDetail_bean.getData().getDetail();
                        type = detailData.getType();
                        String source_url = detailData.getSource_url();
                        if (TextUtils.isEmpty(source_url)) {
                            source_Tv.setVisibility(View.GONE);
                        } else {
                            source_Tv.setVisibility(View.VISIBLE);
                        }
                        String is_anonymous = detailData.getIsAnonymous();//1?????????
                        if (TextUtils.equals(is_anonymous, "1")) {
                            titleNickName_Tv.setText("????????????");
                            title_Img.setBackgroundResource(R.mipmap.icon_logo);

                            person_Name.setText("????????????");
                            person_Img.setEnabled(false);
                            person_Img.setBackgroundResource(R.mipmap.icon_logo);
                        } else {
                            GlideUtils.loadCircleImage(mContext, title_Img, detailData.getAvatar(), R.mipmap.icon_logo);
                            titleNickName_Tv.setText(detailData.getAuthorName());

                            GlideUtils.loadCircleImage(mContext, person_Img, detailData.getAvatar(), R.mipmap.icon_logo);
                            person_Name.setText(detailData.getAuthorName());
                            person_Name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});//????????????
                            labelFlow(lable_Lin, mContext, detailData.getTagName());
                            isFollow(detailData.getFollowStatus(), follow_Tv, send_Mes, is_anonymous);
                        }
                        title_Tv.setText(detailData.getTitle());
//                        center_Tv.setText(detailData.getTitle());
//                        content_Tv.setText(detailData.getContent());

                        Spanned spanned;
                        if (TextUtils.isEmpty(detailData.getHtml_content())) {
                            spanned = Html.fromHtml(detailData.getContent());
                        } else {
                            String html_content = detailData.getHtml_content();
                            html_content =  html_content.replaceAll("(?is)<style.*?>.*?</style>", "");
                            spanned = CustomHtml.fromHtml(html_content,CustomHtml.FROM_HTML_MODE_LEGACY,new RichEditImageGetter(mActivity,  content_Tv),null);
                        }
                        // remove css
                        handleHtmlClickAndStyle(mContext, content_Tv, spanned);

//                        Spanned sp = CustomHtml.fromHtml(detailData.getHtml_content(),CustomHtml.FROM_HTML_MODE_LEGACY,new RichEditImageGetter(mActivity,  content_Tv),null);
//                        content_Tv.setText(sp);


                        String companyName = TextUtils.isEmpty(detailData.getCompanyName()) ? "" : detailData.getCompanyName();
                        String userDate = DateUtil.getUserDate(detailData.getCreateTime());
                        time_Tv.setText(userDate);
                        company_Tv.setText(companyName);

                        if (TextUtils.isEmpty(detailData.getMedia_name())) {
                            comeFrom_Tv.setText("");
                        } else {
                            String data_source = String.format("?????????%1$s", detailData.getMedia_name());
                            comeFrom_Tv.setText(data_source);
                        }


                        //????????????
                        if (!TextUtils.isEmpty(detailData.getImages())) {
                            showNineView(detailData.getImages());
                        }
                        follow_Count.setText(detailData.getLikedNum());
                        mes_Count.setText(detailData.getCommentNum());
                        isLike(detailData.getLikedStatus(), detailData.getLikedNum(), follow_Img, follow_Count);
                        isCollect(detailData.getIs_collect(), collect_Tv);

                        String action = detailData.getAction();
                        if (TextUtils.equals(action, "3") || TextUtils.equals(action, "0")) {//0?????????  3??????????????? 1????????? 2????????????
                            group_Con.setVisibility(View.GONE);
                        } else {
                            group_Con.setVisibility(View.VISIBLE);
                            if (detailData.getExtra() != null) {
                                GlideUtils.loadCircleImage(mContext, groupHead_Img, detailData.getExtra().getImage(), R.mipmap.icon_logo);
                                groupName_Tv.setText(detailData.getExtra().getName());
                                groupDes_Tv.setText(detailData.getExtra().getDes());
                                groupHead(detailData.getExtra().getUserAvatar());
                                isJoinGroup(detailData.getExtra().getIsJoin());
                            }
                        }
                        initTab();
                        isChangeFold();
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    private void postFollowData(String otherUserId, TextView followView, View sendView) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "??????-????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    if (homeFoucsDetail_bean != null) {
                        Integer followStatus = followStateBena.getFollowStatus();
                        homeFoucsDetail_bean.getData().getDetail().setFollowStatus(followStatus);
                        String isAnonymous = homeFoucsDetail_bean.getData().getDetail().getIsAnonymous();
                        if (isExpanded) {
                            isFollow(followStatus, follow_Tv, send_Mes, isAnonymous);
                        } else {
                            isFollow(followStatus, titleFollow_Tv, titleSend_Mes, isAnonymous);
                        }
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * ??????
     *
     * @param likeId
     */
    private void postLikedData(String likeId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "??????/????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    if (likedStateBena != null) {
                        //0?????????????????????1????????????
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


    /**
     * ??????
     *
     * @param articleId
     */
    private void postForwardingData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        String type = "0";
        if (isStockOrFund) {
            type = "1";
        }
        requestMap.put("id", articleId);
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.Repeat_Article, "????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    private void getJoinSheQun(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.joinGroup, "??????/????????????", requestMap, new MyOnHttpResListener() {
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

    private void getJoinGroup(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "??????/???????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    String isJoin = homeFoucsDetail_bean.getData().getDetail().getIsJoin();
                    changeJoinGroup(isJoin);
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    public void addComment(String articleId, String type, String comment) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "??????-????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    T.s(json_data, 3000);
                    sendComment_Et.setText("");
                    sendComment_Et.setHint("");
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * ??????
     *
     * @param articleId
     */
    private void postCollectData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        if (isStockOrFund) {
            requestMap.put("type", "3");
        } else {
            requestMap.put("type", "0");
        }
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "??????/????????????", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CollectStateBean collectStateBean = GsonUtil.getInstance().json2Bean(json_data, CollectStateBean.class);
                    if (collectStateBean != null) {
                        Integer collect_status = collectStateBean.getCollect_status();
                        isCollect(collect_status + "", collect_Tv);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    /**
     * ??????/????????????
     *
     * @param status
     * @param collect_Tv
     */
    private void isCollect(String status, TextView collect_Tv) {
        int mipmap = R.mipmap.shoucang;
        if (TextUtils.equals(status, "1")) {
            mipmap = R.mipmap.shoucang_select;
        }
        collect_Tv.setBackgroundResource(mipmap);
    }


    /**
     * @param follow_status 0???????????? 1????????? 2????????? 3?????????????????? 4????????????
     * @param follow_Tv
     * @param send_Mes
     * @param is_anonymous  ???????????? 1?????????
     */
    public void isFollow(int follow_status, TextView follow_Tv, View send_Mes, String is_anonymous) {
        if (isExpert) {
            return;
        }
        if (isInformation) {
            return;
        }

        if (TextUtils.equals(is_anonymous, "0")) {
            String s = String.valueOf(follow_status);
            if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
                follow_Tv.setVisibility(View.VISIBLE);
                follow_Tv.setText("??????");
                follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
                changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
                send_Mes.setVisibility(View.GONE);
            } else if (TextUtils.equals(s, "1")) {
                follow_Tv.setVisibility(View.GONE);
                send_Mes.setVisibility(View.GONE);
            } else if (TextUtils.equals(s, "2")) {
                follow_Tv.setVisibility(View.VISIBLE);
                follow_Tv.setText("?????????");
                follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
                changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
                send_Mes.setVisibility(View.GONE);
            } else if (TextUtils.equals(s, "4")) {
                follow_Tv.setVisibility(View.VISIBLE);
                follow_Tv.setText("????????????");
                follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
                changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
                send_Mes.setVisibility(View.VISIBLE);
            }
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
     * ???V??????
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
     * ?????????????????????
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
     * ??????????????????
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
     * ?????????????????????
     *
     * @param isJoin 1?????????????????????????????????
     */
    private void isJoinGroup(String isJoin) {
        if (TextUtils.equals(isJoin, "1")) {
            join_Tv.setText("?????????");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            join_Tv.setText("??????");
            join_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(join_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }


    private void changeJoinGroup(String isJoin) {
        if (TextUtils.equals(isJoin, "1")) {
            homeFoucsDetail_bean.getData().getDetail().setIsJoin("0");
        } else {
            homeFoucsDetail_bean.getData().getDetail().setIsJoin("1");
        }
        isJoinGroup(homeFoucsDetail_bean.getData().getDetail().getIsJoin());
//        appLayout.setExpanded(false);
    }

    /**
     * ????????????????????????
     */
    private class appLayoutListener extends AppBarStateChangeListener {
        @Override
        public void onStateChanged(AppBarLayout appBarLayout, State state) {
            if (state == State.EXPANDED) {
                //????????????
                isExpanded = true;
                title_Img.setVisibility(View.GONE);
                titleNickName_Tv.setVisibility(View.GONE);
                center_Tv.setVisibility(View.VISIBLE);
                titleFollow_Tv.setVisibility(View.GONE);
                titleSend_Mes.setVisibility(View.GONE);
                if (homeFoucsDetail_bean != null) {
                    Integer followStatus = homeFoucsDetail_bean.getData().getDetail().getFollowStatus();
                    String isAnonymous = homeFoucsDetail_bean.getData().getDetail().getIsAnonymous();
                    isFollow(followStatus, follow_Tv, send_Mes, isAnonymous);
                }
            } else if (state == State.COLLAPSED) {
                //????????????
                isExpanded = false;
                title_Img.setVisibility(View.VISIBLE);
                titleNickName_Tv.setVisibility(View.VISIBLE);
                center_Tv.setVisibility(View.GONE);
                if (homeFoucsDetail_bean != null) {
                    Integer followStatus = homeFoucsDetail_bean.getData().getDetail().getFollowStatus();
                    String isAnonymous = homeFoucsDetail_bean.getData().getDetail().getIsAnonymous();
                    isFollow(followStatus, titleFollow_Tv, titleSend_Mes, isAnonymous);
                }
            } else {
                //????????????
                isExpanded = true;
                title_Img.setVisibility(View.GONE);
                titleNickName_Tv.setVisibility(View.GONE);
                center_Tv.setVisibility(View.VISIBLE);
                titleFollow_Tv.setVisibility(View.GONE);
                titleSend_Mes.setVisibility(View.GONE);
            }

            if (isInformation) {
                title_Img.setVisibility(View.GONE);
                titleNickName_Tv.setVisibility(View.GONE);
                center_Tv.setVisibility(View.VISIBLE);
                titleFollow_Tv.setVisibility(View.GONE);
                titleSend_Mes.setVisibility(View.GONE);
                top_Con.setVisibility(View.GONE);
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void isChangeFold() {
        if (TextUtils.equals(isShowTop, "1")) {
            appLayout.setExpanded(false);
        }
    }

    /**
     * ??????textview ?????????
     *
     * @param textView
     * @param mipmap
     */
    private void setDrawableEnd(TextView textView, int mipmap) {
        Drawable drawableEnd = getResources().getDrawable(mipmap);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                null, drawableEnd, null);
    }


    //????????????????????????
    private void handleHtmlClickAndStyle(Context context, TextView textview, Spanned spanned) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spanned);
        URLSpan[] urls = spannableStringBuilder.getSpans(0, spanned.length(), URLSpan.class);
        for (URLSpan url : urls) {
            CustomURLSpan myURLSpan = new CustomURLSpan(context, url.getURL());
            int start = spannableStringBuilder.getSpanStart(url);
            int end = spannableStringBuilder.getSpanEnd(url);
            int flags = spannableStringBuilder.getSpanFlags(url);
            spannableStringBuilder.setSpan(myURLSpan, start, end, flags);
            //????????????????????????,?????????????????????????????????????????????????????????ClickableSpan???onClick????????????????????????????????????????????????
            spannableStringBuilder.removeSpan(url);

        }
        textview.setText(spannableStringBuilder);
        //???????????????????????????????????????
        textview.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //?????????CustomURLSpan????????????????????????URLSpan
    private class CustomURLSpan extends ClickableSpan {
        private Context mContext;
        private String mUrl;

        CustomURLSpan(Context context, String url) {
            mUrl = url;
            mContext = context;
        }

        @Override
        public void onClick(View view) {
            //????????????????????????  mUrl ???<a>?????????href??????
            jumpToWebView("", mUrl);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#1e5494"));//??????????????????
//            ds.setUnderlineText(false);//???????????????
        }
    }

//    private void parseCSSFromText(String text, SpanStack spanStack){
//        try{
//            for(Rule rule : CSSParser.parse(text)) {
//                spanStack.registerCompiledRule(CSSCompiler.compile(rule, getSpanner()));
//            }
//        }catch(Exception e) {
//            Log.e("StyleNodeHandler","Unparseable CSS definition", e);
//        }
//    }



}