package com.xunda.lib.common.view.dropdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.xunda.lib.common.R;

/**
 * 下拉筛选
 */
public class DropDownMenuLayout extends FrameLayout{
	//弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //遮罩颜色
    private int maskColor = Color.parseColor("#7f000000");
    
    private boolean isShowing;
    
    private DropdownListener listener;
    
    public void setDropdownListener(DropdownListener listener){
    	this.listener = listener;
    }
    
	public DropDownMenuLayout(Context context) {
		this(context, null, 0);
	}
	
	public DropDownMenuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DropDownMenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        maskColor = a.getColor(R.styleable.DropDownMenu_maskColor, maskColor);
        a.recycle();
	}
	
	public void setDropDownMenu(@NonNull View popupView){
		//创建遮罩层
		createMaskView();
		addView(maskView);
		//创建弹出层
		createPopupMenuView(popupView);
		addView(popupMenuViews);
	}
	
	private void createMaskView(){
		maskView = new View(getContext());
        maskView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setVisibility(GONE);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	closeMenu();
            }
        });
	}
	
	private void createPopupMenuView(View popupView){
		popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        popupView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        popupMenuViews.addView(popupView);
	}
	
	public void showMenu(){
		if(isShowing){
			closeMenu();
		}else{
			popupMenuViews.setVisibility(View.VISIBLE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_menu_in));
            maskView.setVisibility(VISIBLE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_mask_in));
			isShowing = true;
			if(listener != null){
            	listener.onDropdownOpen();
            }
		}
	}
	
	/**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isShowing) {
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popwindow_mask_out));
            isShowing = false;
            if(listener != null){
            	listener.onDropdownClose();
            }
        }
    }
	
	/**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }
    
    public interface DropdownListener{
    	public void onDropdownOpen();
    	public void onDropdownClose();
    }
}
