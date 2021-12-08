package com.xunda.lib.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.StringUtil;


/**
 * 加载中Dialog
 *
 */
public class LoadingDialog extends AlertDialog {

	private Context context;
	private String message;

	public LoadingDialog(Context context,String message) {
		super(context, R.style.loadingStyle);
		this.context = context;
		this.message = message;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		TextView tips_loading_msg = findViewById(R.id.tips_loading_msg);
		tips_loading_msg.setText(StringUtil.isBlank(message)?context.getString(R.string.Common_loading):message);
	}


	@Override
	public void onBackPressed() {
		dismiss();
	}
}
