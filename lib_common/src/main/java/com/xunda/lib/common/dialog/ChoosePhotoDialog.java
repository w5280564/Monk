package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.xunda.lib.common.R;

/**
 * 选择照片的dialog
 * @author Administrator
 *
 */
public class ChoosePhotoDialog extends Dialog implements
		View.OnClickListener {
	private ChoosePhotoListener listener;
	private Context context;

	public ChoosePhotoDialog(Context context, ChoosePhotoListener listener) {
		super(context, R.style.bottomrDialogStyle);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_take_photo);
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		dialogWindow.setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		initView();
	}

	private void initView() {
		findViewById(R.id.tv_dialog_title).setOnClickListener(this);
		findViewById(R.id.choosePhoto).setOnClickListener(this);
		findViewById(R.id.openCamera).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.choosePhoto) {
			listener.choosePhoto();
			dismiss();
		} else if (id == R.id.openCamera) {
			listener.takePhoto();
			dismiss();
		} else if (id == R.id.cancel) {
			dismiss();
		}
	}

	public interface ChoosePhotoListener{
		void takePhoto();
		void choosePhoto();
	}
}
