package com.qingbo.monk.Slides.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.PersomCombination_Shares_Adapter;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.baseview.ScreenUtils;
import com.qingbo.monk.bean.CharacterDetail_Bean;
import com.qingbo.monk.bean.FundHaveList_Bean;
import com.qingbo.monk.bean.LikedStateBena;
import com.qingbo.monk.bean.MyDynamicListBean;
import com.qingbo.monk.bean.MyDynamic_Bean;
import com.qingbo.monk.dialog.InfoOrArticleShare_Dialog;
import com.qingbo.monk.home.activity.ArticleDetail_Activity;
import com.qingbo.monk.person.adapter.MyDynamic_Adapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 人物详情
 */
public class SideslipPersonAndFund_Activity extends BaseRecyclerViewSplitActivity implements View.OnClickListener {
    private String nickname, id;
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;

    private String stockCode = "";
    private ImageView head_Img;
    private TextView nickName_Tv, company_Tv, brief_Tv, fundTime_Tv, Keywords_Tv, fundName_Tv;
    private FlowLayout lable_Flow;
    private PieChart chart;
    private RecyclerView mNineView;
    private ConstraintLayout stock_Con, stockContent_Con;
//    private ConstraintLayout tu_Con;

    /**
     * @param context
     * @param nickname
     * @param isShowTop 评论进入隐藏头部 正常是0 点击评论是1
     */
    public static void startActivity(Context context, String nickname, String id, String isShowTop) {
        Intent intent = new Intent(context, SideslipPersonAndFund_Activity.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("id", id);
        intent.putExtra("isShowTop", isShowTop);
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
        return R.layout.activity_sideslip_personandfund;
    }


    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initView() {
        title_bar.setTitle(nickname);
        title_bar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.app_main_color));
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.card_Recycler);
//        tu_Con = findViewById(R.id.tu_Con);
//        ConstraintLayout myCl = findViewById(R.id.myCl);
        initRecyclerView();
        initSwipeRefreshLayoutAndAdapter(true);
    }

    @Override
    protected void initEvent() {
        head_Img.setOnClickListener(this);
        fundTime_Tv.setOnClickListener(this);
        fundName_Tv.setOnClickListener(this);
    }


    @Override
    protected void onRefreshData() {
        page = 1;
        getListData(false);
    }

    @Override
    protected void onLoadMoreData() {
        page++;
        getListData(false);
    }

    @Override
    protected void getServerData() {
        mSwipeRefreshLayout.setRefreshing(true);
        getFundData();
        getPersonData(true);
        getListData(false);
    }

    MyDynamicListBean myDynamicListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        requestMap.put("userid", id);
        requestMap.put("trends", "1");
        HttpSender httpSender = new HttpSender(HttpUrl.UserCenter_Article, "社交主页-我/他的动态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (page == 1 && mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    myDynamicListBean = GsonUtil.getInstance().json2Bean(json_data, MyDynamicListBean.class);
                    if (myDynamicListBean != null) {
                        handleSplitListData(myDynamicListBean, mAdapter, limit);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyDynamic_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        addHeadView();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);
            String id = item.getArticleId();
            String type = item.getType();
            ArticleDetail_Activity.startActivity(this, id, "0", type);
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyDynamic_Bean item = (MyDynamic_Bean) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                position += 1;//添加了headView pos位置需要加1
                switch (view.getId()) {
                    case R.id.follow_Tv:
                        String otherUserId = item.getArticleId();
                        postFollowData(otherUserId, position);
                        break;
                    case R.id.follow_Img:
                        String likeId = item.getArticleId();
                        postLikedData(likeId, position);
                        break;
                    case R.id.mes_Img:
                        String articleId = item.getArticleId();
                        String type = item.getType();
                        ArticleDetail_Activity.startActivity(mActivity, articleId, "1", type);
                        break;
                    case R.id.share_Img:
                        showShareDialog(item);
                        break;
                }
            }
        });
        ((MyDynamic_Adapter) mAdapter).setOnItemImgClickLister((position, strings) -> jumpToPhotoShowActivity(position, strings));
    }

    /**
     * 资讯分享
     */
    private void showShareDialog(MyDynamic_Bean item) {
        String imgUrl = item.getAvatar();
        String downURl = HttpUrl.appDownUrl;
        String articleId = item.getArticleId();
        String title = item.getTitle();
        String content = item.getContent();
        InfoOrArticleShare_Dialog mShareDialog = new InfoOrArticleShare_Dialog(this, articleId, false, downURl, imgUrl, title, content, "分享");
        mShareDialog.show();
    }

    /**
     * 把个人数据加入列表
     */
    private void addHeadView() {
        View myView = LayoutInflater.from(this).inflate(R.layout.persondetail_fragment, null);
        head_Img = myView.findViewById(R.id.head_Img);
        nickName_Tv = myView.findViewById(R.id.nickName_Tv);
        company_Tv = myView.findViewById(R.id.company_Tv);
        brief_Tv = myView.findViewById(R.id.brief_Tv);
        lable_Flow = myView.findViewById(R.id.lable_Flow);
        chart = myView.findViewById(R.id.pieChart_View);
        mNineView = myView.findViewById(R.id.nine_grid);
        fundTime_Tv = myView.findViewById(R.id.fundTime_Tv);
        Keywords_Tv = myView.findViewById(R.id.Keywords_Tv);
        stock_Con = myView.findViewById(R.id.stock_Con);
        stockContent_Con = myView.findViewById(R.id.stockContent_Con);
        fundName_Tv = myView.findViewById(R.id.fundName_Tv);

        initPie();

        mAdapter.addHeaderView(myView);
    }

    FundHaveList_Bean fundHaveList_bean;

    private void getFundData() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("nickname", nickname);
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Have_List, "基金经理-拥有的基金", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    fundHaveList_bean = GsonUtil.getInstance().json2Bean(json_root, FundHaveList_Bean.class);
                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    CharacterDetail_Bean.DataDTO.ListDTO listDTO;
    PersomCombination_Shares_Adapter persomCombination_shares_adapter;

    private void getPersonData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", "1");
        requestMap.put("limit", "1");
        requestMap.put("nickname", nickname);//孙伟
        requestMap.put("id", id);//868
        requestMap.put("stock", stockCode);//"513060"
        requestMap.put("client", "2");
        HttpSender httpSender = new HttpSender(HttpUrl.Fund_Postion, "人物持仓", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    CharacterDetail_Bean characterDetail_bean1 = GsonUtil.getInstance().json2Bean(json_root, CharacterDetail_Bean.class);
                    if (characterDetail_bean1 != null && !ListUtils.isEmpty(characterDetail_bean1.getData().getList())) {
                        listDTO = characterDetail_bean1.getData().getList().get(0);
                        String avatar = listDTO.getAvatar();
                        GlideUtils.loadCircleImage(mActivity, head_Img, avatar, R.mipmap.icon_logo);

                        originalValue(listDTO.getNickname(), "暂未填写", "", nickName_Tv);
                        originalValue(listDTO.getCompanyName(), "暂未填写", "", company_Tv);
//                        originalValue(listDTO.getDescription(), "暂未填写", "", brief_Tv);
                        labelFlow(lable_Flow, mActivity, listDTO.getTagName());
                        originalValue(listDTO.getKeywords(), "暂未填写", "", Keywords_Tv);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mNineView.setLayoutManager(linearLayoutManager);
                        persomCombination_shares_adapter = new PersomCombination_Shares_Adapter();
                        mNineView.setAdapter(persomCombination_shares_adapter);
                        if (!ListUtils.isEmpty(listDTO.getInfo())) {
                            stock_Con.setVisibility(View.VISIBLE);
                            chart.setVisibility(View.VISIBLE);
                            if (listDTO.getInfo().get(0).getList() !=null){
                                List<CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1> list = listDTO.getInfo().get(0).getList();
                                String quarters = listDTO.getInfo().get(0).getQuarters();
                                fundTime_Tv.setText(quarters);
                                persomCombination_shares_adapter.setNewData(list);
                                setData(0, listDTO);
                            }
                        } else {
                            stock_Con.setVisibility(View.VISIBLE);
                            stockContent_Con.setVisibility(View.GONE);
                            chartViewLocation();
                        }

                        originalValue(listDTO.getStock_name(), "暂未填写", "", fundName_Tv);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 饼状图没有数据
     */
    private void noChartData(){
        stock_Con.setVisibility(View.VISIBLE);
        stockContent_Con.setVisibility(View.GONE);
        chartViewLocation();
    }

    /**
     * 饼状图没有数据  调整高度
     */
    private void chartViewLocation(){
        chart.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(chart.getLayoutParams());
        layoutParams.height = ScreenUtils.dip2px(mActivity, 100);
        layoutParams.topToBottom = R.id.stock_Con;
        layoutParams.startToStart = R.id.tu_Con;
        layoutParams.endToEnd = R.id.tu_Con;
        layoutParams.bottomToBottom =R.id.parent;
        chart.setLayoutParams(layoutParams);
    }

    private void postFollowData(String otherUserId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
//                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
//                    TextView follow_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Tv);
//                    TextView send_Mes = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.send_Mes);
//                    ((MyDynamic_Adapter) mAdapter).isFollow(followStateBena.getFollowStatus(), follow_Tv, send_Mes);
//                    if (followStateBena.getFollowStatus() == 0) {
//                        mAdapter.remove(position);
//                        mAdapter.notifyItemChanged(position);
//                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void postLikedData(String likeId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", likeId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Topic_Like, "点赞/取消点赞", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    LikedStateBena likedStateBena = GsonUtil.getInstance().json2Bean(json_data, LikedStateBena.class);
                    likeCount(position, likedStateBena);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }

    private void likeCount(int position, LikedStateBena likedStateBena) {
        ImageView follow_Img = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Img);
        TextView follow_Count = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.follow_Count);
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
            if (nowLike < 0) {
                nowLike = 0;
            }
            follow_Count.setText(nowLike + "");
        }
    }


    public void labelFlow(FlowLayout myFlow, Context mContext, String tag) {
        if (myFlow != null) {
            myFlow.removeAllViews();
        }
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        String[] tagS = tag.split(",");
        int length = tagS.length;
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
     * 设置饼形图属性
     */
    void initPie() {
//       chart = findViewById(R.id.chart1);
        chart.setNoDataTextColor(R.color.text_color_6f6f6f);
        chart.setNoDataText("暂无数据");
        chart.setUsePercentValues(true);//设置使用百分比
        chart.getDescription().setEnabled(false);//设置描述
        chart.setExtraOffsets(5, 10, 5, 5);//设置边距
        chart.setDragDecelerationFrictionCoef(0.95f);//设置摩擦系数（值越小摩擦系数越大）
//       tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//       chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
//       chart.setCenterText(generateCenterSpannableText());//设置环中间的文字
        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        chart.setDrawHoleEnabled(true); //这个方法为true就是环形图，为false就是饼图
        chart.setHoleColor(Color.WHITE);//设置环形中间空白颜色是白色
        chart.setTransparentCircleColor(Color.WHITE);//设置半透明圆环的颜色
        chart.setTransparentCircleAlpha(110);//设置半透明圆环的透明度
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);//设置半透明圆环的半径,看着就有一种立体的感觉
        chart.setDrawCenterText(true);//设置绘制环中文字
        chart.setRotationAngle(0);//设置旋转角度
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);//是否可以旋转
        chart.setHighlightPerTapEnabled(true);//点击是否放大
//       chart.setDrawEntryLabels(false);//数据圈内文字是否显示
        chart.setEntryLabelColor(R.color.text_color_6f6f6f);
        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);
        // add a selection listener
//       chart.setOnChartValueSelectedListener(this);
        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);
        //图例设置 小图标样式
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false); //显示
    }


    //固定设置饼状图颜色
    int[] colorStr = {
            Color.rgb(234, 116, 159), Color.rgb(210, 141, 198), Color.rgb(54, 186, 206),
            Color.rgb(255, 95, 144), Color.rgb(254, 166, 58), Color.rgb(201, 217, 59),
            Color.rgb(95, 255, 110), Color.rgb(193, 47, 229), Color.rgb(59, 217, 204)};

    //       int[] colorStr = {R.color.text_color_ea749f , R.color.text_color_d28dc6, R.color.text_color_36bace, R.color.text_color_ff5f90, R.color.text_color_fea63a, R.color.text_color_c9d93b, R.color.text_color_5fff6e, R.color.text_color_c12fe5, R.color.text_color_3bd9cc};


    /**
     * 饼状图加载数据
     *
     * @param index                默认0 弹窗选中下表
     * @param characterDetail_bean 渲染数据
     */
    private void setData(int index, CharacterDetail_Bean.DataDTO.ListDTO characterDetail_bean) {
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        ArrayList<PieEntry> entries = new ArrayList<>();
        int size = characterDetail_bean.getInfo().get(index).getPieData().getData().size();
        for (int i = 0; i < size; i++) {
            String name = characterDetail_bean.getInfo().get(index).getPieData().getData().get(i).getName();
            float value = characterDetail_bean.getInfo().get(index).getPieData().getData().get(i).getValue().floatValue();
            entries.add(new PieEntry(value, name));
//            entries.add(new PieEntry((float) (Math.random() * range) + range / 5, parties[i % parties.length]));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(5f);//设置饼块选中时偏离饼图中心的距离

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : colorStr)
            colors.add(c);
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.GRAY);//设置连接线的颜色
//        dataSet.setColor(R.color.text_color_main);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//连线百分比数据名字是否显示
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        //圆圈线连接文字颜色
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));//添加chart设置百分比显示
        data.setValueTextSize(11f);
//        data.setValueTextColor(R.color.text_color_6f6f6f);
        data.setValueTextColors(colors);//连接线颜色跟圆圈颜色统一
//        data.setValueTypeface(tf);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();

    }


    /**
     * 没有数据添加默认值
     *
     * @param value
     * @param originalStr
     */
    private void originalValue(Object value, String originalStr, String hint, TextView tv) {
        if (TextUtils.isEmpty((CharSequence) value)) {
            tv.setText(hint + originalStr);
        } else {
            tv.setText(hint + value);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fundTime_Tv:
                setTime(listDTO);
                break;
            case R.id.head_Img:
//                MyAndOther_Card.actionStart(mActivity, id);
                break;
            case R.id.fundName_Tv:
                setFundName(fundHaveList_bean);
                break;
        }
    }

    //选中展示时间 只设置一组数据就好
    private void setTime(CharacterDetail_Bean.DataDTO.ListDTO characterDetail_bean) {
        if (ListUtils.isEmpty(characterDetail_bean.getInfo())) {
            return;
        }
        final List<String> mOptionsItems = new ArrayList<>();
        for (CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO name : characterDetail_bean.getInfo()) {
            mOptionsItems.add(name.getQuarters());
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mActivity, (options1, option2, options3, v) -> {
            fundTime_Tv.setText(mOptionsItems.get(options1));
            if (listDTO.getInfo().get(0).getList() !=null) {
                List<CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1> list = characterDetail_bean.getInfo().get(options1).getList();
                persomCombination_shares_adapter.setNewData(list);
                setData(options1, characterDetail_bean);
            }
        }).build();
        pvOptions.setPicker(mOptionsItems);
        pvOptions.show();
    }

    //选中基金名字 只设置一组数据
    private void setFundName(FundHaveList_Bean fundHaveList_bean) {
        if (ListUtils.isEmpty(fundHaveList_bean.getData())) {
            return;
        }
        final List<Map<Object, String>> allMap = new ArrayList<>();
        final List<String> mOptionsItems = new ArrayList<>();
        for (FundHaveList_Bean.DataDTO oneData : fundHaveList_bean.getData()) {
            Map<Object, String> oneMap = new HashMap<>();
            oneMap.put("name", oneData.getName());
            oneMap.put("code", oneData.getCode());
            mOptionsItems.add(oneData.getName());
            allMap.add(oneMap);
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                fundName_Tv.setText(allMap.get(options1).get("name"));
                stockCode = allMap.get(options1).get("code");
                getPersonData(true);
            }
        }).build();
        pvOptions.setPicker(mOptionsItems);
        pvOptions.show();
    }


}