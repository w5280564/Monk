package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * 隐私政策Dialog
 */
public class PrivacyPolicyDialogAgain extends Dialog implements View.OnClickListener {
	private Context mContext;
	private OnPrivacyPolicyAgainChooseListener mListener;

	public PrivacyPolicyDialogAgain(Context context, OnPrivacyPolicyAgainChooseListener mListener) {
		super(context, R.style.CenterDialogStyle);
		this.mContext = context;
		this.mListener = mListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_privacy_policy_again);
		setCanceledOnTouchOutside(false);
		initView();
		initEvent();
	}



	private void initView() {
		LinearLayout ll_parent = findViewById(R.id.ll_parent);
		int screenWidth = AndroidUtil.getScreenWidth(mContext);// 屏幕的宽度
		int parentWidth = (int) (screenWidth / 3f * 2);// 弹出框的宽度
		ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
		layoutParams.width = parentWidth;
		ll_parent.setLayoutParams(layoutParams);
	}


	private void initEvent() {
		findViewById(R.id.exit).setOnClickListener(this);
		findViewById(R.id.tv_agree).setOnClickListener(this);
	}



	@Override
	public void onBackPressed() {

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.exit) {
			dismiss();
			mListener.exit();
		} else if (id == R.id.tv_agree) {
			dismiss();
			mListener.agree();
		}

	}


	public interface OnPrivacyPolicyAgainChooseListener{
		void exit();
		void agree();
	}
}
