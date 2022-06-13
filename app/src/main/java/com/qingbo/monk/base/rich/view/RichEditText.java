package com.qingbo.monk.base.rich.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;
import android.os.Build;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

import com.qingbo.monk.base.rich.bean.MyFontStyle;
import com.qingbo.monk.base.rich.bean.SpanPart;

import java.util.ArrayList;
import java.util.List;

/**
 * awarmisland
 * RichEditText 富文本
 */
public class RichEditText extends AppCompatEditText implements View.OnClickListener {
    private Context mContext;
    public static final int EXCLUD_MODE = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    public static final int EXCLUD_INCLUD_MODE = Spannable.SPAN_EXCLUSIVE_INCLUSIVE;
    public static final int INCLUD_INCLUD_MODE = Spannable.SPAN_INCLUSIVE_INCLUSIVE;
    private OnSelectChangeListener onSelectChangeListener;

    public RichEditText(Context context) {
        super(context);
        initView(context);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {
        int start = getSelectionStart() - 1;
        if (start == -1) {
            start = 0;
        }
        if (start < -1) {
            return;
        }
        MyFontStyle fontStyle = getFontStyle(start, start);
        setBold(fontStyle.isBold);
        setItalic(fontStyle.isItalic);
        setUnderline(fontStyle.isUnderline);
//        setStreak(fontStyle.isStreak);
        setFontSize(fontStyle.fontSize);
        setFontColor(fontStyle.color);
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelect(start, start);
            onSelectChangeListener.onFontStyleChang(fontStyle);
        }
    }

    /**
     * public setting
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setBold(boolean isBold) {
        setStyleSpan(isBold, Typeface.BOLD);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setItalic(boolean isItalic) {
        setStyleSpan(isItalic, Typeface.ITALIC);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setUnderline(boolean isUnderline) {
        setUnderlineSpan(isUnderline);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void setStreak(boolean isStreak) {
        setStreakSpan(isStreak);
    }

    public void setFontSize(int size) {
        setFontSizeSpan(size);
    }

    public void setFontColor(int color) {
        setForcegroundColor(color);
    }

    public void setImg(String path) {
//        if(!TextUtils.isEmpty(path)) {
//            ImagePlate plate = new ImagePlate(this, mContext);
//            plate.image(path);
//        }
    }

    /**
     * bold italic
     *
     * @param isSet
     * @param type
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setStyleSpan(boolean isSet, int type) {
        MyFontStyle fontStyle = new MyFontStyle();
        if (type == Typeface.BOLD) {
            fontStyle.isBold = true;
        } else if (type == Typeface.ITALIC) {
            fontStyle.isItalic = true;
        }
        setSpan(fontStyle, isSet, StyleSpan.class);
    }

    /**
     * underline
     *
     * @param isSet
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setUnderlineSpan(boolean isSet) {
        MyFontStyle fontStyle = new MyFontStyle();
        fontStyle.isUnderline = true;
        setSpan(fontStyle, isSet, UnderlineSpan.class);
    }

    /**
     * Strikethrough
     *
     * @param isSet
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setStreakSpan(boolean isSet) {
        MyFontStyle fontStyle = new MyFontStyle();
        fontStyle.isStreak = true;
        setSpan(fontStyle, isSet, StrikethroughSpan.class);
    }

    /**
     * 设置 字体大小
     *
     * @param size
     */
    private void setFontSizeSpan(int size) {
        if (size == 0) {
            size = MyFontStyle.NORMAL;
        }
        MyFontStyle fontStyle = new MyFontStyle();
        fontStyle.fontSize = size;
        setSpan(fontStyle, true, AbsoluteSizeSpan.class);
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    private void setForcegroundColor(int color) {
        if (color == 0) {
            color = Color.parseColor(MyFontStyle.BLACK);
        }
        MyFontStyle fontStyle = new MyFontStyle();
        fontStyle.color = color;
        setSpan(fontStyle, true, ForegroundColorSpan.class);
    }

    /**
     * 通用set Span
     *
     * @param fontStyle
     * @param isSet
     * @param tClass
     * @param <T>
     */
    private <T> void setSpan(MyFontStyle fontStyle, boolean isSet, Class<T> tClass) {
        Log.d("setSpan", "");
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int mode = EXCLUD_INCLUD_MODE;
        T[] spans = getEditableText().getSpans(start, end, tClass);
        //获取
        List<SpanPart> spanStyles = getOldFontSytles(spans, fontStyle);
        for (SpanPart spanStyle : spanStyles) {
            if (spanStyle.start < start) {
                if (start == end) {
                    mode = EXCLUD_MODE;
                }
                getEditableText().setSpan(getInitSpan(spanStyle), spanStyle.start, start, mode);
            }
            if (spanStyle.end > end) {
                getEditableText().setSpan(getInitSpan(spanStyle), end, spanStyle.end, mode);
            }
        }
        if (isSet) {
            if (start == end) {
                mode = INCLUD_INCLUD_MODE;
            }
            getEditableText().setSpan(getInitSpan(fontStyle), start, end, mode);
        }
    }

    /**
     * 获取当前 选中 spans
     *
     * @param spans
     * @param fontStyle
     * @param <T>
     * @return
     */
    private <T> List<SpanPart> getOldFontSytles(T[] spans, MyFontStyle fontStyle) {
        List<SpanPart> spanStyles = new ArrayList<>();
        for (T span : spans) {
            boolean isRemove = false;
            if (span instanceof StyleSpan) {//特殊处理 styleSpan
                int style_type = ((StyleSpan) span).getStyle();
                if ((fontStyle.isBold && style_type == Typeface.BOLD)
                        || (fontStyle.isItalic && style_type == Typeface.ITALIC)) {
                    isRemove = true;
                }
            } else {
                isRemove = true;
            }
            if (isRemove) {
                SpanPart spanStyle = new SpanPart(fontStyle);
                spanStyle.start = getEditableText().getSpanStart(span);
                spanStyle.end = getEditableText().getSpanEnd(span);
                if (span instanceof AbsoluteSizeSpan) {
                    spanStyle.fontSize = ((AbsoluteSizeSpan) span).getSize();
                } else if (span instanceof ForegroundColorSpan) {
                    spanStyle.color = ((ForegroundColorSpan) span).getForegroundColor();
                }
                spanStyles.add(spanStyle);
                getEditableText().removeSpan(span);
            }
        }
        return spanStyles;
    }

    /**
     * 返回 初始化 span
     *
     * @param fontStyle
     * @return
     */
    private CharacterStyle getInitSpan(MyFontStyle fontStyle) {
        if (fontStyle.isBold) {
            return new StyleSpan(Typeface.BOLD);
        } else if (fontStyle.isItalic) {
            return new StyleSpan(Typeface.ITALIC);
        } else if (fontStyle.isUnderline) {
            return new UnderlineSpan();
        } else if (fontStyle.isStreak) {
            return new StrikethroughSpan();
        } else if (fontStyle.fontSize > 0) {
            return new AbsoluteSizeSpan(fontStyle.fontSize, true);
        } else if (fontStyle.color != 0) {
            return new ForegroundColorSpan(fontStyle.color);
        }
        return null;
    }

    /**
     * 获取某位置的  样式
     *
     * @param start
     * @param end
     * @return
     */
    private MyFontStyle getFontStyle(int start, int end) {
        MyFontStyle fontStyle = new MyFontStyle();
        CharacterStyle[] characterStyles = getEditableText().getSpans(start, end, CharacterStyle.class);
        for (CharacterStyle style : characterStyles) {
            if (style instanceof StyleSpan) {
                int type = ((StyleSpan) style).getStyle();
                if (type == Typeface.BOLD) {
                    fontStyle.isBold = true;
                } else if (type == Typeface.ITALIC) {
                    fontStyle.isItalic = true;
                }
            } else if (style instanceof UnderlineSpan) {
                fontStyle.isUnderline = true;
            } else if (style instanceof StrikethroughSpan) {
                fontStyle.isStreak = true;
            } else if (style instanceof AbsoluteSizeSpan) {
                fontStyle.fontSize = ((AbsoluteSizeSpan) style).getSize();
            } else if (style instanceof ForegroundColorSpan) {
                fontStyle.color = ((ForegroundColorSpan) style).getForegroundColor();
            }
        }
        return fontStyle;
    }


    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    public interface OnSelectChangeListener {
        void onFontStyleChang(MyFontStyle fontStyle);

        void onSelect(int start, int end);
    }
}