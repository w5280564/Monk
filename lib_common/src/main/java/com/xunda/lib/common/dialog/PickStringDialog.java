package com.xunda.lib.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.view.wheelview.StringWheelMain;
import java.util.List;

/**
 * 单个选择器 数据源是字符串集合
 */
public class PickStringDialog extends Dialog {
	private View view;
	private String title;
	private Activity activity;
	private ChooseCallback chooseCallback;
	private List<String> mList;

	public PickStringDialog(Activity activity, List<String> mList, String title, ChooseCallback chooseCallback) {
		super(activity, R.style.bottomrDialogStyle);
		this.activity = activity;
		this.chooseCallback = chooseCallback;
		this.mList = mList;
		this.title = title;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = getLayoutInflater().inflate(R.layout.picker_single_layout, null);
		setCanceledOnTouchOutside(true);
		Window window = getWindow();
        TextView tv_ok = (TextView) view.findViewById(R.id.tv_ok);
        TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		final StringWheelMain wheelMain = new StringWheelMain(view, activity);

        wheelMain.setList(mList);
		wheelMain.initStringPicker();
        tv_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				callback(wheelMain.getValue());
			}
		});

		tv_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setContentView(view);
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenWidth = AndroidUtil.getScreenWidth(activity);//屏幕的宽度
		int dialogWidth = (int) (screenWidth / 5f * 4);//弹出框的宽度
		lp.width = dialogWidth;
		window.setAttributes(lp);
		window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
	}


	/**
	 * 点击确定
	 * @param value
	 */
    private void callback(String value) {
		if (chooseCallback != null) {
			chooseCallback.onConfirm(value);
		}
	}


	public interface ChooseCallback{
		 void onConfirm(String value);
	}

}
