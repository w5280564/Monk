package com.xunda.lib.common.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.tabs.TabLayout;
import com.xunda.lib.common.R;

public class MyTabLayout extends TabLayout {
    public MyTabLayout(Context context) {
        super(context);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addOnTabSelectedListener(@NonNull BaseOnTabSelectedListener listener) {
        super.addOnTabSelectedListener(listener);
        addOnTabSelectedListener(new MyBaseOnTabSelectedListener());
    }


    public class MyBaseOnTabSelectedListener implements OnTabSelectedListener {

        @Override
        public void onTabSelected(Tab tab) {
            //在这里可以设置选中状态下  tab字体显示样式
//                mViewPager.setCurrentItem(tab.getPosition());
            View view = tab.getCustomView();
            if (null != view) {
                setTextViewStyle(view, 18, R.color.text_color_444444, Typeface.DEFAULT_BOLD, View.VISIBLE);
            }
        }

        @Override
        public void onTabUnselected(Tab tab) {
            View view = tab.getCustomView();
            if (null != view) {
                setTextViewStyle(view, 15, R.color.text_color_a1a1a1, Typeface.DEFAULT, View.INVISIBLE);
            }
        }

        @Override
        public void onTabReselected(Tab tab) {

        }
    }



    private void setTextViewStyle(View view, int size, int color, Typeface textStyle,int visibility) {
        TextView mTextView = view.findViewById(R.id.tab_item_textview);
        View line = view.findViewById(R.id.line);
        mTextView.setTextSize(size);
        mTextView.setTextColor(ContextCompat.getColor(this.getContext(), color));
        mTextView.setTypeface(textStyle);
        line.setVisibility(visibility);
    }

    //
//    /**
//     * 自定义Tab的View
//     * @param currentPosition
//     * @return
//     */
//    private View getTabView(int currentPosition) {
//        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_tab, null);
//        TextView textView = view.findViewById(R.id.tab_item_textview);
//        textView.setText(menuList.get(currentPosition).getName());
//        return view;
//    }
}
