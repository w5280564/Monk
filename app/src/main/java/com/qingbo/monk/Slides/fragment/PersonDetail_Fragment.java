package com.qingbo.monk.Slides.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.adapter.PersomCombination_Shares_Adapter;
import com.qingbo.monk.Slides.adapter.StockCombination_Shares_Adapter;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.CharacterDetail_Bean;
import com.qingbo.monk.bean.CharacterList_Bean;
import com.qingbo.monk.bean.StockCombinationListBean;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 人物详情
 */
public class PersonDetail_Fragment extends BaseFragment implements View.OnClickListener {
    private String nickname, id;
    @BindView(R.id.head_Img)
    ImageView head_Img;
    @BindView(R.id.nickName_Tv)
    TextView nickName_Tv;
    @BindView(R.id.company_Tv)
    TextView company_Tv;
    @BindView(R.id.brief_Tv)
    TextView brief_Tv;
    @BindView(R.id.lable_Flow)
    FlowLayout lable_Flow;
    @BindView(R.id.pieChart_View)
    PieChart chart;
    @BindView(R.id.nine_grid)
    RecyclerView mNineView;
    @BindView(R.id.fundTime_Tv)
    TextView fundTime_Tv;
    @BindView(R.id.Keywords_Tv)
    TextView Keywords_Tv;
    @BindView(R.id.stock_Con)
    ConstraintLayout stock_Con;
    @BindView(R.id.stockContent_Con)
    ConstraintLayout stockContent_Con;

    /**
     * @param
     * @return
     */
    public static PersonDetail_Fragment newInstance(String nickname, String id) {
        Bundle args = new Bundle();
        args.putString("nickname", nickname);
        args.putString("id", id);
        PersonDetail_Fragment fragment = new PersonDetail_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        nickname = getArguments().getString("nickname");
        id = getArguments().getString("id");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.persondetail_fragment;
    }

    @Override
    protected void initView() {
        initPie();
    }

    @Override
    protected void initEvent() {
        head_Img.setOnClickListener(this);
        fundTime_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        getListData(true);
    }




    CharacterDetail_Bean.DataDTO.ListDTO listDTO;
    PersomCombination_Shares_Adapter persomCombination_shares_adapter;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
//        requestMap.put("page", "1");
//        requestMap.put("limit", "1");
        requestMap.put("nickname", nickname);
        requestMap.put("id", id);
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

//                        nickName_Tv.setText(listDTO.getNickname());
//                        company_Tv.setText(listDTO.getCompanyName());
                        originalValue(listDTO.getNickname(), "暂未填写", "", nickName_Tv);
                        originalValue(listDTO.getCompanyName(), "暂未填写", "", company_Tv);
                        originalValue(listDTO.getDescription(), "暂未填写", "", brief_Tv);
//                        brief_Tv.setText(listDTO.getDescription());
                        labelFlow(lable_Flow, mActivity, listDTO.getTagName());
//                        Keywords_Tv.setText(listDTO.getKeywords());
                        originalValue(listDTO.getKeywords(), "暂未填写", "", Keywords_Tv);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        mNineView.setLayoutManager(linearLayoutManager);
                        persomCombination_shares_adapter = new PersomCombination_Shares_Adapter();
                        mNineView.setAdapter(persomCombination_shares_adapter);
                        if (!ListUtils.isEmpty(listDTO.getInfo())) {
                            stock_Con.setVisibility(View.VISIBLE);
                            chart.setVisibility(View.VISIBLE);
                            List<CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1> list = listDTO.getInfo().get(0).getList();
                            String quarters = listDTO.getInfo().get(0).getQuarters();
                            fundTime_Tv.setText(quarters);
                            persomCombination_shares_adapter.setNewData(list);
                            setData(0, listDTO);
                        }else {
                            stock_Con.setVisibility(View.VISIBLE);
                            stockContent_Con.setVisibility(View.GONE);
                            chart.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
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


    //选中展示时间 只设置一组数据就好
    private void setTime(CharacterDetail_Bean.DataDTO.ListDTO characterDetail_bean) {
        if (ListUtils.isEmpty(characterDetail_bean.getInfo())) {
            return;
        }
        final List<String> mOptionsItems = new ArrayList<>();
        for (CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO name : characterDetail_bean.getInfo()) {
            mOptionsItems.add(name.getQuarters());
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(requireActivity(), (options1, option2, options3, v) -> {
            fundTime_Tv.setText(mOptionsItems.get(options1));
            List<CharacterDetail_Bean.DataDTO.ListDTO.InfoDTO.ListDT1> list = characterDetail_bean.getInfo().get(options1).getList();
            persomCombination_shares_adapter.setNewData(list);
            setData(options1, characterDetail_bean);

        }).build();
        pvOptions.setPicker(mOptionsItems);
        pvOptions.show();
    }


    /**
     * 设置饼形图属性
     */
    void initPie() {
//       chart = findViewById(R.id.chart1);
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
            Integer value = characterDetail_bean.getInfo().get(index).getPieData().getData().get(i).getValue();
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fundTime_Tv:
                setTime(listDTO);
                break;
            case R.id.head_Img:
                MyAndOther_Card.actionStart(mActivity, id);
                break;
        }
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
            tv.setText(hint + (CharSequence) value);
        }
    }

}
