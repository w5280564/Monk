package com.qingbo.monk.home.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.HomeFoucsDetail_Bean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.home.adapter.Combination_Shares_Adapter;
import com.qingbo.monk.home.fragment.ArticleDetail_Comment_Fragment;
import com.qingbo.monk.home.fragment.ArticleDetail_Zan_Fragment;
import com.qingbo.monk.home.fragment.CombinationDetail_Comment_Fragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 仓位组合详情
 */
public class CombinationDetail_Activity extends BaseTabLayoutActivity implements View.OnClickListener {
    @BindView(R.id.time_Tv)
    TextView time_Tv;
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
    @BindView(R.id.card_Tab)
    TabLayout card_Tab;
    @BindView(R.id.card_ViewPager)
    ViewPager card_ViewPager;
    @BindView(R.id.appLayout)
    AppBarLayout appLayout;
    @BindView(R.id.sendComment_Et)
    EditText sendComment_Et;
    @BindView(R.id.release_Tv)
    public TextView release_Tv;
    @BindView(R.id.comName_TV)
    public TextView comName_TV;
    @BindView(R.id.chart)
    LineChart chart;


    private String articleId;
    private String isShowTop;
    private String type;
    boolean isReply = false;
    private String id;

    /**
     * @param context
     * @param articleId
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String articleId, String isShowTop, String type, String id) {
        Intent intent = new Intent(context, CombinationDetail_Activity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("isShowTop", isShowTop);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_main_color)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void setStatusBar() {
        setBar();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_combinationdetail;
    }


    @Override
    protected void initLocalData() {
        articleId = getIntent().getStringExtra("articleId");
        isShowTop = getIntent().getStringExtra("isShowTop");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//弹起键盘不遮挡布局，背景布局不会顶起
        initLineChart();
    }



    @Override
    protected void initEvent() {
        follow_Img.setOnClickListener(this);
//        appLayout.addOnOffsetChangedListener(new appLayoutListener());
        mes_Img.setOnClickListener(this);
        release_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getUserDetail(false);
    }

    @Override
    public void onClick(View v) {
        if (homeCombinationBean == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.follow_Img:
                String likeId = homeCombinationBean.getId();
                postLikedData(likeId);
                break;
            case R.id.mes_Img:
                showInput(sendComment_Et, false);
                sendComment_Et.setHint("");
                break;
            case R.id.release_Tv:
                String s = sendComment_Et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    T.s("评论不能为空", 2000);
                    return;
                }
                if (TextUtils.isEmpty(articleId) || TextUtils.isEmpty(type)) {
                    T.s("文章ID是空", 2000);
                    return;
                }
                if (isReply) {
                    ArticleDetail_Comment_Fragment o = (ArticleDetail_Comment_Fragment) tabFragmentList.get(0);
                    o.onClick(release_Tv);
                } else {
                    addComment(articleId, type, s);
                }
                break;
        }
    }

    /**
     * 点击弹出键盘
     *
     * @param editView
     * @param editView 是否回复评论  true是对评论回复
     */
    public void showInput(View editView, boolean isReply) {
        this.isReply = isReply;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        editView.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
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
//        String articleId = homeFoucsDetail_bean.getData().getDetail().getArticleId();
//        String type = homeFoucsDetail_bean.getData().getDetail().getType();
        articleId = "1";
        type = "1";
        tabFragmentList.add(CombinationDetail_Comment_Fragment.newInstance(articleId, type, id));
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
        //设置TabLayout和ViewPager联动
        card_Tab.setupWithViewPager(card_ViewPager, false);
    }


    HomeCombinationBean homeCombinationBean;

    private void getUserDetail(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        HttpSender httpSender = new HttpSender(HttpUrl.Square_Position_List, "仓位组合详情", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
//                    CombinationDetail_Bean combinationDetail_bean = GsonUtil.getInstance().json2Bean(json_root, CombinationDetail_Bean.class);
                    homeCombinationBean = GsonUtil.getInstance().json2Bean(json_data, HomeCombinationBean.class);

                    if (homeCombinationBean != null) {
                        comName_TV.setText(homeCombinationBean.getName());
                        isLike(homeCombinationBean.getLike(), homeCombinationBean.getLikecount(), follow_Img, follow_Count);
                        mes_Count.setText(homeCombinationBean.getCommentcount());
                        time_Tv.setText(DateUtil.getUserDate(homeCombinationBean.getCreateTime()));

                        mNineView.addItemDecoration(getRecyclerViewDivider(R.drawable.recyleview_solid));//添加横向分割线
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mNineView.setLayoutManager(linearLayoutManager);
                        Combination_Shares_Adapter combination_shares_adapter = new Combination_Shares_Adapter();
                        mNineView.setAdapter(combination_shares_adapter);
                        combination_shares_adapter.setNewData(homeCombinationBean.getDetail());
                        initTab();
                        isChangeFold();
                    }

                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    private void postLikedData(String likeId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.combination_Topic_Like, "仓位点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    if (likedStateBena != null) {
                        changeLike(likedStateBena);
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    /**
     * 添加评论
     * @param articleId
     * @param type
     * @param comment
     */
    public void addComment(String articleId, String type, String comment) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId);
        requestMap.put("type", type);
        requestMap.put("comment", comment);
        requestMap.put("isAnonymous", "0");
        HttpSender httpSender = new HttpSender(HttpUrl.AddComment_Post, "文章-添加评论", requestMap, new MyOnHttpResListener() {
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

    private void initLineChart() {
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);

        //x轴下面数据
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //y轴左边数据
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);// disable dual axis (only use LEFT axis) 右边数据不显示
        // set data
//       chart.setData((LineData) mChartData);
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(750);

        setLineChartData(2);
    }

    private void setLineChartData(int cnt){
        ArrayList<Entry> values1 = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }
        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(1f);
        d1.setCircleRadius(3f);
        d1.setDrawCircleHole(false);
        d1.setHighLightColor(Color.rgb(188,219,247));//点击线颜色
        d1.setColor(Color.rgb(31, 143, 229));//连线颜色
        d1.setCircleColor(Color.rgb(31, 143, 229));//圆点颜色
        d1.setDrawValues(false);

//        ArrayList<Entry> values2 = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            values2.add(new Entry(i, values1.get(i).getY() - 30));
//        }
//
//        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
//        d2.setLineWidth(2.5f);
//        d2.setCircleRadius(4.5f);
//        d2.setHighLightColor(Color.rgb(244, 117, 117));
//        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setDrawValues(false);

        // set the filled area
        d1.setDrawFilled(true); //显示渐变阴影色
//        d1.setFillFormatter(new IFillFormatter() {
//            @Override
//            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                return chart.getAxisLeft().getAxisMinimum();
//            }
//        });

        // set color of filled area 渐变阴影颜色
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
            d1.setFillDrawable(drawable);
        } else {
            d1.setFillColor(Color.BLACK);
        }

//        ArrayList<ILineDataSet> sets = new ArrayList<>();
//        sets.add(d1);
//        sets.add(d2);
        LineData lineData = new LineData(d1);
        chart.setData(lineData);
//        return new LineData(d1);

    }



    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(likes + "");
    }

    private void changeLike(LikedStateBena likedStateBena) {
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

    /**
     * 收起整个折叠页
     */
    private void isChangeFold() {
        if (TextUtils.equals(isShowTop, "1")) {
            appLayout.setExpanded(false);
        }
    }

    /**
     * 获取分割线
     *
     * @param drawableId 分割线id
     * @return
     */
    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        CustomDecoration itemDecoration = new CustomDecoration(mContext, LinearLayoutManager.VERTICAL, false);
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, drawableId));
        return itemDecoration;
    }


}