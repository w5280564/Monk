package com.qingbo.monk.base.rich.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.qingbo.monk.R;
import com.qingbo.monk.base.rich.bean.MyFontStyle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FontStyleSelectView extends LinearLayout {
    private Context mContext;
    private MyFontStyle fontStyle;
    private OnFontStyleSelectListener onFontStyleSelectListener;

    @BindView(R.id.btn_bold)
    Button btn_bold;
    @BindView(R.id.btn_italic)
    Button btn_italic;
    @BindView(R.id.btn_underline)
    Button btn_underline;
    @BindView(R.id.btn_streak)
    Button btn_streak;

    public FontStyleSelectView(Context context) {
        super(context);
        initView(context);
    }

    public FontStyleSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FontStyleSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.view_font_style_select,this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_bold)
    protected void btn_bold_click(View view){
        setFontStyle(view);
    }
    @OnClick(R.id.btn_italic)
    protected void btn_italic_click(View view){
        setFontStyle(view);
    }
    @OnClick(R.id.btn_underline)
    protected void btn_underline_click(View view){
        setFontStyle(view);
    }
    @OnClick(R.id.btn_streak)
    protected void btn_streak_click(View view){
        setFontStyle(view);
    }

    private void setFontStyle(View view){
        Button button = (Button) view;
        button.setTextColor(Color.BLACK);
        boolean flag= false;
        if(onFontStyleSelectListener!=null&&fontStyle!=null){
            switch (view.getId()){
                case R.id.btn_bold:
                    fontStyle.isBold=!fontStyle.isBold;
                    flag=fontStyle.isBold;
                    onFontStyleSelectListener.setBold(fontStyle.isBold);
                    if(flag){
                        button.setTextColor(Color.RED);
                    }
                    break;
                case R.id.btn_italic:
                    fontStyle.isItalic=!fontStyle.isItalic;
                    flag=fontStyle.isItalic;
                    onFontStyleSelectListener.setItalic(fontStyle.isItalic);
                    if(flag){
                        button.setTextColor(Color.RED);
                    }
                    break;
                case R.id.btn_underline:
                    fontStyle.isUnderline=!fontStyle.isUnderline;
                    flag=fontStyle.isUnderline;
                    onFontStyleSelectListener.setUnderline(fontStyle.isUnderline);
                    break;
                case R.id.btn_streak:
                    fontStyle.isStreak=!fontStyle.isStreak;
                    flag=fontStyle.isStreak;
                    onFontStyleSelectListener.setStreak(fontStyle.isStreak);
                    break;
            }

        }
    }

    public void initFontStyle(MyFontStyle fontStyle){
        this.fontStyle = fontStyle;
        initDefaultStyle();
        if(fontStyle.isBold){
            btn_bold.setTextColor(Color.RED);
        }
        if(fontStyle.isItalic){
            btn_italic.setTextColor(Color.RED);
        }
        if(fontStyle.isUnderline){
//            btn_underline.setTextColor(Color.RED);
        }
        if(fontStyle.isStreak){
//            btn_streak.setTextColor(Color.RED);
        }
    }

    private void initDefaultStyle(){
        btn_bold.setTextColor(Color.BLACK);
        btn_italic.setTextColor(Color.BLACK);
        btn_underline.setTextColor(Color.BLACK);
        btn_streak.setTextColor(Color.BLACK);
    }

    public interface OnFontStyleSelectListener{
        void setBold(boolean isBold);
        void setItalic(boolean isItalic);
        void setUnderline(boolean isUnderline);
        void setStreak(boolean isStreak);
    }

    public void setOnFontStyleSelectListener(OnFontStyleSelectListener onFontStyleSelectListener) {
        this.onFontStyleSelectListener = onFontStyleSelectListener;
    }

    public void setFontStyle(MyFontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }
}
