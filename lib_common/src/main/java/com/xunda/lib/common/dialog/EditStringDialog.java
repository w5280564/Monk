package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

/**
 * 编辑字符传内容dialog
 */
public class EditStringDialog extends Dialog implements View.OnClickListener {
	private Context mContext;
	private OnCompleteListener mListener;
	private String title;
	private String content;
	private String hint;
	private EditText et_input;
	private int maxLength;

	public EditStringDialog(@NonNull Context context, String title,String content,String hint, OnCompleteListener listener) {
		super(context, R.style.bottomrDialogStyle);
		this.mContext = context;
		this.mListener = listener;
		this.title = title;
		this.content = content;
		this.hint = hint;
		maxLength = "社群名称".equals(title)?10:200;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit_string_layout);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setCanceledOnTouchOutside(true);
		initView();
	}

	private void initView() {
		LinearLayout ll_layout = findViewById(R.id.ll_layout);
		int screenHeight = AndroidUtil.getScreenHeight(mContext);// 屏幕的宽度
		int parentHeight = (int) (screenHeight / 5f * 4);// 弹出框的高度
		ViewGroup.LayoutParams layoutParams = ll_layout.getLayoutParams();
		layoutParams.height = parentHeight;
		ll_layout.setLayoutParams(layoutParams);

		TextView tv_title = findViewById(R.id.tv_title);
		tv_title.setText(StringUtil.getStringValue(title));
		et_input = findViewById(R.id.et_input);
		et_input.setText(StringUtil.getStringValue(content));
		et_input.setHint(hint);
		et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

		findViewById(R.id.tv_close).setOnClickListener(this);
		findViewById(R.id.tv_ok).setOnClickListener(this);
	}




	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.tv_close) {
			dismiss();
		} else if (id == R.id.tv_ok) {
			String value = StringUtil.getEditText(et_input);
			if (StringUtil.isBlank(value)) {
				T.ss(hint);
				return;
			}

			mListener.OnComplete(value);
			dismiss();
		}

	}



	public interface OnCompleteListener {
		void OnComplete(String value);
	}

}
