package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.xunda.lib.common.R;

public class ChatBottomDialog extends Dialog implements
		View.OnClickListener {
	private ChooseBottomListener listener;
	private Context context;

	public ChatBottomDialog(Context context, ChooseBottomListener listener) {
		super(context, R.style.bottomrDialogStyle);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_chat_bottom);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		dialogWindow.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		initView();
	}

	private void initView() {
		findViewById(R.id.tv_aite).setOnClickListener(this);
		findViewById(R.id.tv_red).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.tv_aite) {
			listener.aite();
			dismiss();
		} else if (id == R.id.tv_red) {
			listener.sendRed();
			dismiss();
		} else if (id == R.id.cancel) {
			dismiss();
		}
	}

	public interface ChooseBottomListener{
		void aite();
		void sendRed();
	}
}
