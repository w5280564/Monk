package com.qingbo.monk.base.baseview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.qingbo.monk.R;


public class MyCardEditView extends ConstraintLayout {
    private TextView content_Tv;
    private TextView edit_Tv;

    public MyCardEditView(Context context) {
        this(context, null);
    }

    public MyCardEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCardEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        View root = LayoutInflater.from(context).inflate(R.layout.card_itemview, this);
        TextView title_Tv = findViewById(R.id.title_Tv);
        content_Tv = findViewById(R.id.content_Tv);
        View line_View = findViewById(R.id.line_View);
        edit_Tv = findViewById(R.id.edit_Tv);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardItemView);

        int titleResourceId = a.getResourceId(R.styleable.CardItemView_cardTitle, -1);
        String title = a.getString(R.styleable.CardItemView_cardTitle);
        if (titleResourceId != -1) {
            title = getContext().getString(titleResourceId);
        }
        title_Tv.setText(title);

        int avatarSrcResourceId = a.getResourceId(R.styleable.CardItemView_cardDrawStartImg, -1);
        if (avatarSrcResourceId != -1) {
            Drawable drawable = getResources().getDrawable(avatarSrcResourceId );
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            title_Tv.setCompoundDrawables(drawable, null, null, null);
        }

        int titleColorId = a.getResourceId(R.styleable.CardItemView_cardTitleColor, -1);
        int titleColor = a.getColor(R.styleable.CardItemView_cardTitleColor, ContextCompat.getColor(getContext(), R.color.text_color_444444));
        if (titleColorId != -1) {
            titleColor = ContextCompat.getColor(getContext(), titleColorId);
        }
        title_Tv.setTextColor(titleColor);

        int titleSizeId = a.getResourceId(R.styleable.CardItemView_cardTitleSize, -1);
        float titleSize = a.getDimension(R.styleable.CardItemView_cardTitleSize, sp2px(getContext(), 14));
        if (titleSizeId != -1) {
            titleSize = getResources().getDimension(titleSizeId);
        }
        title_Tv.getPaint().setTextSize(titleSize);


        int contentResourceId = a.getResourceId(R.styleable.CardItemView_cardContent, -1);
        String content = a.getString(R.styleable.CardItemView_cardContent);
        if (contentResourceId != -1) {
            content = getContext().getString(contentResourceId);
        }
        content_Tv.setText(content);

        int contentColorId = a.getResourceId(R.styleable.CardItemView_cardContentColor, -1);
        int contentColor = a.getColor(R.styleable.CardItemView_cardContentColor, ContextCompat.getColor(getContext(), R.color.text_color_a1a1a1));
        if (contentColorId != -1) {
            contentColor = ContextCompat.getColor(getContext(), contentColorId);
        }
        content_Tv.setTextColor(contentColor);

        int contentSizeId = a.getResourceId(R.styleable.CardItemView_cardContentSize, -1);
        float contentSize = a.getDimension(R.styleable.CardItemView_cardContentSize, 14);
        if (contentSizeId != -1) {
            contentSize = getResources().getDimension(contentSizeId);
        }
        content_Tv.getPaint().setTextSize(contentSize);
//
        boolean showDivider = a.getBoolean(R.styleable.CardItemView_ShowLine, true);
        line_View.setVisibility(showDivider ? VISIBLE : GONE);


        boolean showEditTv = a.getBoolean(R.styleable.CardItemView_ShowEditTv, true);
        edit_Tv.setVisibility(showEditTv ? VISIBLE : GONE);


        a.recycle();

    }

    public TextView getContent_Tv() {
        return content_Tv;
    }

    public TextView getEdit_Tv() {
        return edit_Tv;
    }


    /**
     * sp to px
     *
     * @param context
     * @param value
     * @return
     */
    public static float sp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }
}
