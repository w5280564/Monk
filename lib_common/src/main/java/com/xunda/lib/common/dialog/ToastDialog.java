package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * 1个按钮的提醒提示弹出框
 */
public class ToastDialog extends Dialog implements
		View.OnClickListener {

	private DialogConfirmListener listener;
	private String content,title, button_text;
	private Context mContext;
	private TextView tv_button;

	public ToastDialog(Context context, String title, String content, String button_text, DialogConfirmListener confirmListener) {
		super(context, R.style.CenterDialogStyle);
		this.listener = confirmListener;
		this.title = title;
		this.content = content;
		this.button_text = button_text;
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_success);
		setCanceledOnTouchOutside(false);
		initView();
		initEvent();
		
	}


	private void initView() {
		LinearLayout ll_parent = findViewById(R.id.ll_parent);
		int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
		int parentWidth = (int) (screenWidth/3f * 2);//弹出框的宽度
		ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
		layoutParams.width = parentWidth;
		ll_parent.setLayoutParams(layoutParams);

		tv_button = findViewById(R.id.tv_button);
		TextView tv_title = findViewById(R.id.tv_title);
		TextView tv_content = findViewById(R.id.tv_content);

		tv_button.setText(button_text);
		tv_content.setText(content);
		tv_title.setText(title);
	}
	
	
	private void initEvent() {
		tv_button.setOnClickListener(this);
	}

	/**
	 * 回调接口对象
	 */

	public interface DialogConfirmListener {

		void onConfirmClick();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.tv_button) {
			listener.onConfirmClick();
			dismiss();
		}
	}


	@Override
	public void onBackPressed() {

	}


}
