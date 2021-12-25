package com.xunda.lib.common.common.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.view.MarqueeTextView;
import com.xunda.lib.common.common.utils.StringUtil;


/**
 * TitleBar封装
 *
 * @author ouyang
 */
public class CustomTitleBar extends FrameLayout implements OnClickListener {
    //默认显示图片
    public final static int IMG = 0;
    //显示文字
    public final static int TEXT = 1;
    //默认显示颜色
    public final static int BAG_COLOR = 2;
    //显示资源文件
    public final static int BAG_RES = 3;

    //默认字体颜色
    public final static int TITLE_TEXT_COLOR = Color.parseColor("#333333");

    public final static int TITLE_BACKGROUND_COLOR = Color.parseColor("#FFFFFF");

    private RelativeLayout leftClickLayout;
    private LinearLayout ll_title_bar;
    private TextView leftBtnView;
    private RelativeLayout centerClickLayout;
    private TextView centerTxtView;
    private RelativeLayout rightClickLayout;
    private TextView rightBtnView;
    private ImageView rightImgView;
    private View title_line;
    private TitleBarClickListener titleBarClickListener;

    // 标题内容
    private String title;
    // 标题文字大小
    private int titleSize = 17;
    // 标题文字颜色
    private int titleColor;

    // 标题栏背景类型(颜色、资源文件)
    private int titleBarBackgroundShowType;

    // 标题栏背景颜色
    private int titleBarBackgroundColor;
    // 标题栏背景资源
    private int titleBarBackgroundResource;
    // 是否显示标题栏底部的分割线
    private boolean isShowLine;
    // 左侧是否显示
    private int leftShowFlag;
    // 左侧显示类型(文字、图片)
    private int leftShowType;
    // 左侧文字
    private String leftTxt;
    // 左侧文字大小
    private int leftTxtSize = 14;
    // 左侧文字颜色
    private int leftTxtColor;
    // 左侧图标
    private int leftImg;

    //右侧是否显示
    private int rightShowFlag;
    // 右侧显示类型(文字、图片)
    private int rightShowType;
    // 右侧文字
    private String rightTxt;
    // 右侧文字大小
    private int rightTxtSize = 14;
    // 右侧文字颜色
    private int rightTxtColor;
    // 右侧图标
    private int rightImg;
    // 右侧文字背景
    private int rightTextBackground;

    public CustomTitleBar(Context context) {
        this(context, null, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(getContext()).inflate(R.layout.custom_titlebar_layout, this);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        titleSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, titleSize, dm);
        leftTxtSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, leftTxtSize, dm);
        rightTxtSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, rightTxtSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        title = a.getString(R.styleable.CustomTitleBar_titleBarTitle);
        titleSize = a.getDimensionPixelSize(R.styleable.CustomTitleBar_titleSize, titleSize);
        titleColor = a.getColor(R.styleable.CustomTitleBar_titleColor, TITLE_TEXT_COLOR);
        titleBarBackgroundColor = a.getColor(R.styleable.CustomTitleBar_titleBarBackgroundColor, TITLE_BACKGROUND_COLOR);
        titleBarBackgroundResource = a.getResourceId(R.styleable.CustomTitleBar_titleBarBackgroundResource, 0);
        titleBarBackgroundShowType = a.getInt(R.styleable.CustomTitleBar_titleBarBackgroundShowType, BAG_COLOR);

        isShowLine = a.getBoolean(R.styleable.CustomTitleBar_titleBarShowBottomLine, true);
        leftShowFlag = a.getInt(R.styleable.CustomTitleBar_titleBarLeftShowFlag, View.VISIBLE);
        leftShowType = a.getInt(R.styleable.CustomTitleBar_titleBarLeftShowType, IMG);
        leftTxt = a.getString(R.styleable.CustomTitleBar_titleBarLeftTxt);
        leftTxtSize = a.getDimensionPixelSize(R.styleable.CustomTitleBar_titleBarLeftTxtSize, leftTxtSize);
        leftTxtColor = a.getColor(R.styleable.CustomTitleBar_titleBarLeftTxtColor, TITLE_TEXT_COLOR);
        leftImg = a.getResourceId(R.styleable.CustomTitleBar_titleBarLeftImg, 0);

        rightShowFlag = a.getInt(R.styleable.CustomTitleBar_titleBarRightShowFlag, View.INVISIBLE);
        rightShowType = a.getInt(R.styleable.CustomTitleBar_titleBarRightShowType, IMG);
        rightTxt = a.getString(R.styleable.CustomTitleBar_titleBarRightTxt);
        rightTxtSize = a.getDimensionPixelSize(R.styleable.CustomTitleBar_titleBarRightTxtSize, rightTxtSize);
        rightTxtColor = a.getColor(R.styleable.CustomTitleBar_titleBarRightTxtColor, TITLE_TEXT_COLOR);
        rightImg = a.getResourceId(R.styleable.CustomTitleBar_titleBarRightImg, 0);
        rightTextBackground = a.getResourceId(R.styleable.CustomTitleBar_titleBarRightTextBackground, 0);
        a.recycle();

        initView();
        setAttr();
    }

    protected void initView() {
        leftClickLayout = findViewById(R.id.title_bar_left);
        leftBtnView = findViewById(R.id.title_bar_left_btn);

        centerClickLayout =  findViewById(R.id.title_bar_center);
        centerTxtView = findViewById(R.id.title_bar_center_txt);

        ll_title_bar =  findViewById(R.id.ll_title_bar);
        rightClickLayout =  findViewById(R.id.title_bar_right);
        rightBtnView =  findViewById(R.id.title_bar_right_btn);
        rightImgView =  findViewById(R.id.title_bar_right_img);
        title_line =  findViewById(R.id.title_line);
        title_line.setVisibility(isShowLine?VISIBLE:GONE);

        if (titleBarBackgroundShowType == BAG_COLOR) {
            ll_title_bar.setBackgroundColor(titleBarBackgroundColor);
        }else{
            ll_title_bar.setBackgroundResource(titleBarBackgroundResource);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int left = leftClickLayout.getMeasuredWidth();
        int right = rightClickLayout.getMeasuredWidth();
        int center = centerClickLayout.getMeasuredWidth();

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int center2 = width - left - right;
        if (center > center2) {
            centerTxtView.setPadding(left, 0, right, 0);
        } else {
            centerTxtView.setPadding(0, 0, 0, 0);
        }
    }

    protected void setAttr() {
        setTitle(title);
        handleLeft();
        handleRight();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            centerTxtView.setText(title);
            centerTxtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            centerTxtView.setTextColor(titleColor);
        }
    }

    public String getTitle() {
        if(centerTxtView!=null){
            String title = centerTxtView.getText().toString().trim();
            if(!StringUtil.isBlank(title)){
                return  title;
            }else{
                return  null;
            }
        }else{
            return  null;
        }
    }

    public void setTitleColor(int color) {
        centerTxtView.setTextColor(color);
    }

    /**
     * 左侧按钮处理
     */
    protected void handleLeft() {
        if (leftShowFlag == View.VISIBLE) {
            showLeft();
            // 左侧显示才设置具体内容
            if (leftShowType == TEXT) {
                // 左侧显示类型为文字时，文字内容必须设置
                if (TextUtils.isEmpty(leftTxt)) {
                    throw new RuntimeException("The left button text can not be empty");
                }
                leftBtnView.setText(leftTxt);
                leftBtnView.setBackgroundResource(0);
                leftBtnView.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTxtSize);
                leftBtnView.setTextColor(leftTxtColor);
            } else {
                // 左侧显示类型为图片时，图片资源必须设置
                if (leftImg == 0) {
                    throw new RuntimeException("The left button image resource is not set");
                }
                leftBtnView.setText("");
                leftBtnView.setBackgroundResource(leftImg);
            }
        } else {
            hideLeft();
        }
    }

    public void hideLeft() {
        leftClickLayout.setVisibility(View.INVISIBLE);
        leftClickLayout.setOnClickListener(null);
    }

    public void showLeft() {
        leftClickLayout.setVisibility(View.VISIBLE);
        leftClickLayout.setOnClickListener(this);
    }

    /**
     * 右侧按钮处理
     */
    protected void handleRight() {
        if (rightShowFlag == View.VISIBLE) {
            showRight();
            // 右侧显示才设置具体内容
            if (rightShowType == TEXT) {
                // 右侧显示类型为文字时，文字内容必须设置
                if (TextUtils.isEmpty(rightTxt)) {
                    throw new RuntimeException("The right button text can not be empty");
                }
                rightBtnView.setText(rightTxt);
                rightBtnView.setBackgroundResource(rightTextBackground);
                rightBtnView.setGravity(Gravity.CENTER);
//                rightBtnView.setPadding(15,4,15,4);
                rightBtnView.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTxtSize);
                rightBtnView.setTextColor(rightTxtColor);
            } else {
                // 右侧显示类型为图片时，图片资源必须设置
                if (rightImg == 0) {
                    throw new RuntimeException("The right button image resource is not set");
                }
                rightBtnView.setVisibility(GONE);
                rightImgView.setVisibility(VISIBLE);
                rightImgView.setImageResource(rightImg);
            }
        } else {
            hideRight();
        }
    }

    public void hideRight() {
        rightClickLayout.setVisibility(View.INVISIBLE);
        rightClickLayout.setOnClickListener(null);
    }

    public void showRight() {
        rightClickLayout.setVisibility(View.VISIBLE);
        rightClickLayout.setOnClickListener(this);
    }


    public void hideTitle() {
        ll_title_bar.setVisibility(View.GONE);
    }


    public void showTitle() {
        ll_title_bar.setVisibility(View.VISIBLE);
    }


    public void setRightTxtColor(int color) {
        rightBtnView.setTextColor(getResources().getColor(color));
    }

    public void setRightEnabled(boolean flag, int color) {
        if (flag) {
            // 可点
            rightClickLayout.setOnClickListener(this);
        } else {
            // 不可点
        }
        if (rightShowFlag == VISIBLE && rightShowType == TEXT) {
            rightBtnView.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置标题栏右侧图片
     *
     */
    public void setRightImg(int resid) {
        if (rightShowFlag == VISIBLE && rightShowType == IMG) {
            rightImgView.setImageResource(resid);
        }
    }

    /**
     * 设置标题栏右侧图片
     *
     */
    public void setRightText(String text) {
        if (rightShowFlag == VISIBLE && rightShowType == TEXT) {
            rightBtnView.setText(text);
        }
    }

    public void setLeftImg(int resid) {
        if (leftShowFlag == VISIBLE && leftShowType == IMG) {
            leftBtnView.setBackgroundResource(resid);
        }
    }


    public RelativeLayout getRightLayout() {
        return  rightClickLayout;
    }



    public void hideLine() {
        title_line.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (titleBarClickListener == null) {
            return;
        }
        if (v.getId() == R.id.title_bar_left) {
            titleBarClickListener.onLeftClick();
        } else if (v.getId() == R.id.title_bar_right) {
            titleBarClickListener.onRightClick();
        }
    }

    public TitleBarClickListener getTitleBarClickListener() {
        return titleBarClickListener;
    }

    public void setTitleBarClickListener(TitleBarClickListener titleBarClickListener) {
        this.titleBarClickListener = titleBarClickListener;
    }

    protected int dp2px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    public interface TitleBarClickListener {
        void onLeftClick();

        void onRightClick();
    }
}

