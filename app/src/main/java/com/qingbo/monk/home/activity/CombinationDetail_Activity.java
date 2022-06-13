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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.base.HideIMEUtil;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.CollectStateBean;
import com.qingbo.monk.bean.CombinationLineChart_Bean;
import com.qingbo.monk.bean.HomeCombinationBean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.adapter.Combination_Shares_Adapter;
import com.qingbo.monk.home.fragment.CombinationDetail_Comment_Fragment;
import com.qingbo.monk.home.fragment.CombinationDetail_Zan_Fragment;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.itemdecoration.CustomDecoration;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 组合详情
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
    @BindView(R.id.label_Name)
    TextView label_Name;
    @BindView(R.id.share_Tv)
    TextView share_Tv;
    @BindView(R.id.collect_Tv)
    TextView collect_Tv;

    private String isShowTop;
    boolean isReply = false;
    private String id;

    /**
     * @param context
     * @param id
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String isShowTop, String id) {
        Intent intent = new Intent(context, CombinationDetail_Activity.class);
        intent.putExtra("isShowTop", isShowTop);
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
        isShowTop = getIntent().getStringExtra("isShowTop");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void initView() {
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(mes_Img, 50);
        viewTouchDelegate.expandViewTouchDelegate(share_Tv, 50);
        HideIMEUtil.wrap(this, sendComment_Et);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//弹起键盘不遮挡布局，背景布局不会顶起
        initLineChart();
        StringUtil.setColor(mContext, 0, label_Name);
    }


    @Override
    protected void initEvent() {
        follow_Img.setOnClickListener(this);
        mes_Img.setOnClickListener(this);
        release_Tv.setOnClickListener(this);
        share_Tv.setOnClickListener(this);
        collect_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getUserDetail(true);
        addLineData(id, "6", false);
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
                sendMes();
                break;
            case R.id.share_Tv:
                if (homeCombinationBean != null) {
                    showShareDialog(homeCombinationBean);
                }
                break;
            case R.id.collect_Tv:
                String articleId = homeCombinationBean.getId();
                postCollectData(articleId);
                break;
        }
    }

    /**
     * 分享
     */
    private void showShareDialog(HomeCombinationBean item) {
        String imgUrl = "";
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getId();
        String title = item.getName();
        String content = "";
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(this, articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.setAuthor_id("");
        mShareDialog.setArticleType("3");
        mShareDialog.setCollectType("2");
        mShareDialog.setForGroupType("1");
        mShareDialog.show();
    }

    /**
     * 发送评论
     */
    private void sendMes() {
        String s = sendComment_Et.getText().toString();
        if (TextUtils.isEmpty(s)) {
            T.s("评论不能为空", 2000);
            return;
        }
        if (TextUtils.isEmpty(id)) {
            T.s("文章ID是空", 2000);
            return;
        }
        if (isReply) {
            CombinationDetail_Comment_Fragment o = (CombinationDetail_Comment_Fragment) tabFragmentList.get(0);
            o.onClick(release_Tv);
        } else {
            addComment(id, s);
        }
    }

    /**
     * 是否是自己
     *
     * @param authorId2
     * @return
     */
    public boolean isMy(String authorId2) {
        String id = PrefUtil.getUser().getId();
        String authorId = authorId2;
        if (TextUtils.equals(id, authorId)) {//是自己不能评论
            return true;
        }
        return false;
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
        tabFragmentList.add(CombinationDetail_Comment_Fragment.newInstance(id, homeCombinationBean.getName()));
        tabFragmentList.add(CombinationDetail_Zan_Fragment.newInstance(id, ""));

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
                    homeCombinationBean = GsonUtil.getInstance().json2Bean(json_data, HomeCombinationBean.class);
                    if (homeCombinationBean != null) {
                        comName_TV.setText(homeCombinationBean.getName());
                        isLike(homeCombinationBean.getLike(), homeCombinationBean.getLikecount(), follow_Img, follow_Count);
                        mes_Count.setText(homeCombinationBean.getCommentcount());
                        time_Tv.setText(DateUtil.getUserDate(homeCombinationBean.getCreateTime()));

//                        mNineView.addItemDecoration(getRecyclerViewDivider(R.drawable.recyleview_solid));//添加横向分割线
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mNineView.setLayoutManager(linearLayoutManager);
                        Combination_Shares_Adapter combination_shares_adapter = new Combination_Shares_Adapter();
                        mNineView.setAdapter(combination_shares_adapter);
                        combination_shares_adapter.setNewData(homeCombinationBean.getDetail());
                        isCollect(homeCombinationBean.getIs_collect(), collect_Tv);
                        initTab();
                        isChangeFold();
                    }

                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    /**
     * 折线图数据
     *
     * @param id
     * @param type
     */
    public void addLineData(String id, String type, boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("type", type);
        HttpSender httpSender = new HttpSender(HttpUrl.LineChart_Position, "仓位组合--净值折线", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {

                    CombinationLineChart_Bean combinationLineChart_bean = GsonUtil.getInstance().json2Bean(json_data, CombinationLineChart_Bean.class);
                    if (combinationLineChart_bean != null) {
                        int labelCount = chart.getXAxis().getLabelCount();
                        setLineChartData(combinationLineChart_bean);
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
     * 收藏
     *
     * @param articleId
     */
    private void postCollectData(String articleId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("articleId", articleId + "");
        requestMap.put("type", "2");
        HttpSender httpSender = new HttpSender(HttpUrl.Collect_Article, "收藏/取消收藏", requestMap, new MyOnHttpResListener() {
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
     * 收藏/取消收藏
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
     * 添加评论
     *
     * @param id
     * @param comment
     */
    public void addComment(String id, String comment) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", id);
        requestMap.put("comment", comment);
        requestMap.put("is_anonymous", "0");
        HttpSender httpSender = new HttpSender(HttpUrl.Combination_AddComment, "仓位组合-添加评论", requestMap, new MyOnHttpResListener() {
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
     * 折线图初始化
     */
    private void initLineChart() {
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        chart.getDescription().setEnabled(false); // 不显示描述
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);// 取消缩放
        chart.setNoDataText("暂无数据");
        //x轴下
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true); //X轴数字竖线
        xAxis.setDrawLabels(true);//绘制X轴上的对应值
        xAxis.setDrawAxisLine(false);
//        xAxis.setGranularity(1f); //设置x轴间距
        xAxis.setLabelCount(6, false);//横坐标显示7个
        xAxis.setAxisMaximum(6);//横坐标最多
        xAxis.setAxisMinimum(0f);//横坐标最少

        //y轴左边
        YAxis leftAxis = chart.getAxisLeft();
        // 强制显示Y轴6个坐标 第二个参数表示是否平均分配 如果为true则按比例分为6个点、如果为false则适配X刻度的值来分配点
        leftAxis.setLabelCount(5, true);
        leftAxis.setAxisMinimum(0f); // y轴坐标最少
//        leftAxis.setAxisMaximum(1.3f);// y轴坐标最大
//        leftAxis.setDrawGridLines(false); //Y轴横线线
        //y轴右边
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);// disable dual axis (only use LEFT axis) 右边数据不显示
        // set data
        // do not forget to refresh the chart
        // holder.chart.invalidate();
        chart.animateX(750);
    }

    /**
     * 折线图加载数据
     *
     * @param combinationLineChart_bean
     */
    private void setLineChartData(CombinationLineChart_Bean combinationLineChart_bean) {
        if (ListUtils.isEmpty(combinationLineChart_bean.getData().getJingzhiLine())) {
            return;
        }
        //x轴格式化一周时间
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (ListUtils.isEmpty(combinationLineChart_bean.getDatetime())) {
                    return "";
                } else {
                    int value1 = (int) value;
                    String xlineDate = DateUtil.getLineDate(combinationLineChart_bean.getDatetime().get(value1));
                    return xlineDate;
                }
            }
        });
//        List<String> jingzhiLine = combinationLineChart_bean.getData().getJingzhiLine();
//        float max = Float.parseFloat(Collections.max(jingzhiLine));
//        chart.getAxisLeft().setAxisMaximum(max);//左边y轴最大值
//        L.d("max", max + "");
        ArrayList<Entry> values1 = new ArrayList<>();
        int size = combinationLineChart_bean.getData().getJingzhiLine().size();
        for (int i = 0; i < size; i++) {
            float v = Float.parseFloat(combinationLineChart_bean.getData().getJingzhiLine().get(i));
            values1.add(new Entry(i, v));
        }
        LineDataSet d1 = new LineDataSet(values1, ""); //label是显示提示
        d1.setLineWidth(1f);
        d1.setCircleRadius(3f);
        d1.setDrawCircleHole(false);
        d1.setHighLightColor(Color.rgb(188, 219, 247));//点击线颜色
        d1.setColor(Color.rgb(31, 143, 229));//连线颜色
        d1.setCircleColor(Color.rgb(31, 143, 229));//圆点颜色
        d1.setDrawValues(true); // 坐标不显示值
        // set the filled area
        d1.setDrawFilled(true); // 填充颜色(渐变色)
        d1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());
        // set color of filled area 渐变阴影颜色
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
            d1.setFillDrawable(drawable);
        } else {
            d1.setFillColor(Color.BLACK);
        }
        LineData lineData = new LineData(d1);
        chart.setData(lineData);
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