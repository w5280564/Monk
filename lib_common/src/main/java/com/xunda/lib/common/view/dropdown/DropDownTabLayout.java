package com.xunda.lib.common.view.dropdown;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunda.lib.common.R;

import java.util.List;

/**
 * 下拉筛选
 * 在布局中必须只有两个layout，第一个为LinearLayout放筛选按钮；第二个为FrameLayout 放页面主内容区域
 */
public class DropDownTabLayout extends LinearLayout {
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //tab容器
    private LinearLayout tabContainer;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab字体大小
    private int menuTextSize = 14;
    //tab高度
    private int menuHeight = 40;
    //tab选中颜色
    private int textSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int textUnselectedColor = 0xff111111;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    //tab背景夜色
    private int menuBackgroundColor = 0xffffffff;
    //遮罩颜色
    private int maskColor = 0x88888888;

    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    public DropDownTabLayout(Context context) {
        this(context, null, 0);
    }

    public DropDownTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        menuHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, menuHeight, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        menuHeight = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuHeight, menuHeight);
        dividerColor = a.getColor(R.styleable.DropDownMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.DropDownMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.DropDownMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_menuUnselectedIcon, menuUnselectedIcon);
        a.recycle();
    }

    public void setDropDownTab(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }
        //获取tab容器
        LinearLayout tabMenuView = (LinearLayout) getChildAt(0);
        tabMenuView.setOrientation(VERTICAL);
        //创建tab
        LinearLayout tabMenu = createTabMenu(tabTexts);
        tabMenuView.addView(tabMenu);

        //获取内容容器
        FrameLayout contanier = (FrameLayout) getChildAt(1);
        //创建遮罩层
        createMaskView();
        contanier.addView(maskView);
        //创建弹出层
        createPopupMenuView(popupViews);
        contanier.addView(popupMenuViews);
    }

    private LinearLayout createTabMenu(@NonNull List<String> tabTexts) {
        tabContainer = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuHeight);
        tabContainer.setOrientation(HORIZONTAL);
        tabContainer.setBackgroundColor(menuBackgroundColor);
        tabContainer.setLayoutParams(params);
        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i, tabContainer);
        }
        return tabContainer;
    }

    private void createMaskView() {
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setVisibility(GONE);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
    }

    private void createPopupMenuView(@NonNull List<View> popupViews) {
        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }
    }

    private void addTab(@NonNull List<String> tabTexts, int i, LinearLayout tabContainer) {

        final LinearLayout tabLayout = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        tabLayout.setLayoutParams(params);
        tabLayout.setGravity(Gravity.CENTER);

        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
        tab.setTextColor(textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnselectedIcon), null);
        tab.setCompoundDrawablePadding(dpTpPx(5));
        tab.setText(tabTexts.get(i));
        tabLayout.addView(tab);
        tabLayout.setTag(tab);
        //添加点击事件
        tabLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tabLayout);
            }
        });

        tabContainer.addView(tabLayout);
        //添加分割线
        LayoutParams dividerParams = new LayoutParams(dpTpPx(0.2f), ViewGroup.LayoutParams.MATCH_PARENT);
        dividerParams.setMargins(0, dpTpPx(10), 0, dpTpPx(10));
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            view.setBackgroundColor(dividerColor);
            view.setLayoutParams(dividerParams);
            tabContainer.addView(view);
        }
    }

    private void switchMenu(View target) {
        for (int i = 0; i < tabContainer.getChildCount(); i = i + 2) {
            TextView textView = (TextView) tabContainer.getChildAt(i).getTag();
            if (target == tabContainer.getChildAt(i)) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_mask_in));
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    textView.setTextColor(textSelectedColor);
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(menuSelectedIcon), null);
                }
            } else {
                textView.setTextColor(textUnselectedColor);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(menuUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            TextView textView = (TextView) tabContainer.getChildAt(current_tab_position).getTag();
            textView.setTextColor(textUnselectedColor);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(menuUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_mask_out));
            current_tab_position = -1;
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 改变tab文字
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) tabContainer.getChildAt(current_tab_position).getTag()).setText(text);
        }
    }



    /**
     * 设置专业默认文字
     */
    public void setMajorTabDefaultText() {
        ((TextView) tabContainer.getChildAt(2).getTag()).setText("所属专业");
    }




    /**
     * 强制改变tab文字，用于多级的展品分类
     */
    public void setForceTabText(int position,String text) {
        ((TextView) tabContainer.getChildAt(position).getTag()).setText(text);
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
