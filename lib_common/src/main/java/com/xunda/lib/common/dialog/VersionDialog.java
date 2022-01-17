package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 版本升级弹出框
 */
public class VersionDialog extends Dialog implements
		View.OnClickListener {

	private VersionConfirmListener listener;
	private Context mContext;
	private TextView tv_update,tv_content;
	private ImageView iv_cancel;
	private String update_content;
	private int isForceUpdate;

	public VersionDialog(Context context,String update_content,int isForceUpdate,VersionConfirmListener confirmListener) {
		super(context, R.style.CenterDialogStyle);
		this.listener = confirmListener;
		this.mContext = context;
		this.update_content = update_content;
		this.isForceUpdate = isForceUpdate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.version_dialog);
		setCanceledOnTouchOutside(false);
		initView();
		initEvent();
	}

	private void initView() {
//		LinearLayout ll_parent = findViewById(R.id.ll_parent);
//		int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
//		int parentWidth = (int) (screenWidth /5f * 4);//弹出框的宽度
//		int parentHeight = (int) (parentWidth*1f);//弹出框的高度
//		ViewGroup.LayoutParams layoutParams = ll_parent.getLayoutParams();
//		layoutParams.width = parentWidth;
//		layoutParams.height = parentHeight;
//		ll_parent.setLayoutParams(layoutParams);
		iv_cancel =  findViewById(R.id.iv_cancel);
		tv_update =  findViewById(R.id.tv_update);
		tv_content = findViewById(R.id.tv_content);
//		tv_content.setText(StringUtil.isBlank(update_content)?"":update_content);
		iv_cancel.setVisibility(isForceUpdate==0?View.VISIBLE:View.GONE);//0推荐更新1强制
	}
	
	
	private void initEvent() {
		iv_cancel.setOnClickListener(this);
		tv_update.setOnClickListener(this);
	}
	
	/**
	 * 回调接口对象
	 */

	public interface VersionConfirmListener {
		void onDownload();
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.iv_cancel) {
			dismiss();
		} else if (id == R.id.tv_update) {
			listener.onDownload();
		}
	}

	@Override
	public void onBackPressed() {

	}
}
