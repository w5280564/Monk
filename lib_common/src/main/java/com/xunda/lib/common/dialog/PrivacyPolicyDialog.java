package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xunda.lib.common.R;
import com.xunda.lib.common.common.utils.AndroidUtil;

/**
 * 隐私政策Dialog
 */
public class PrivacyPolicyDialog extends Dialog implements View.OnClickListener {
	private Context mContext;
	private OnPrivacyPolicyChooseListener mListener;
	private TextView tv_content;

	public PrivacyPolicyDialog(Context context, OnPrivacyPolicyChooseListener mListener) {
		super(context, R.style.CenterDialogStyle);
		this.mContext = context;
		this.mListener = mListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_privacy_policy);
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
		tv_content = findViewById(R.id.tv_content);
		setPartTextColorAndListener(mContext.getString(R.string.privacy_policy_dialog_content),"《用户协议》","《隐私政策》");
	}


	private void initEvent() {
		findViewById(R.id.tv_not_agree).setOnClickListener(this);
		findViewById(R.id.tv_agree).setOnClickListener(this);
	}



	/**
	 * 设置部分字体颜色和它的点击事件
	 */
	private void setPartTextColorAndListener(String content, final String matcherAgreement,final String matcherPrivatePolicy) {
		tv_content.setHighlightColor(ContextCompat.getColor(mContext,android.R.color.transparent));
		SpannableStringBuilder builder = new SpannableStringBuilder(content);



		int startIndex_agreement = content.indexOf(matcherAgreement);
		int endIndex_agreement = startIndex_agreement+matcherAgreement.length();
		//可单独文本前景色
		builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.animal_color)), startIndex_agreement,endIndex_agreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		//设置文本点击事件
		builder.setSpan(new PrivacyPolicyDialogClickSpannable(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.clickAgreement();
				}
			}
		}), startIndex_agreement, endIndex_agreement,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


		int startIndex_private_policy = content.indexOf(matcherPrivatePolicy);
		int endIndex_private_policy = startIndex_private_policy+matcherPrivatePolicy.length();
		//可单独文本前景色
		builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.animal_color)), startIndex_private_policy,endIndex_private_policy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		//设置文本点击事件
		builder.setSpan(new PrivacyPolicyDialogClickSpannable(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.clickPrivacyPolicy();
				}
			}
		}), startIndex_private_policy, endIndex_private_policy,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		tv_content.setMovementMethod(LinkMovementMethod.getInstance());
		tv_content.setText(builder);
	}





	/**
	 * SpannableStringBuilder 点击事件
	 */
	public class PrivacyPolicyDialogClickSpannable extends ClickableSpan implements
			View.OnClickListener {

		private View.OnClickListener onClickListener;

		public PrivacyPolicyDialogClickSpannable(View.OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
		}

		@Override
		public void onClick(View widget) {
			onClickListener.onClick(widget);
		}

		/**
		 * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
		 */
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(ContextCompat.getColor(mContext, R.color.app_main_color));
		}

	}




	@Override
	public void onBackPressed() {

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.tv_not_agree) {
			dismiss();
			mListener.notAgree();
		} else if (id == R.id.tv_agree) {
			dismiss();
			mListener.agree();
		}

	}


	public interface OnPrivacyPolicyChooseListener{
		void notAgree();
		void agree();
		void clickPrivacyPolicy();
		void clickAgreement();
	}
}
