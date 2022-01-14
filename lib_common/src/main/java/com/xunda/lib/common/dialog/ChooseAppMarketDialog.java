package com.xunda.lib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xunda.lib.common.R;
import com.xunda.lib.common.adapter.ChooseAppMarketAdapter;
import com.xunda.lib.common.bean.AppMarketBean;
import com.xunda.lib.common.common.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择应用市场弹出框
 */
public class ChooseAppMarketDialog extends Dialog{

	private DialogItemChooseListener listener;
	private Context mContext;
	private List<AppMarketBean> mMarketList = new ArrayList<>();
	private RecyclerView recycleView;
	private ChooseAppMarketAdapter mAdapter;
	private int not_renew;//是否强制更新 0推荐更新 1强制


	public ChooseAppMarketDialog(Context context, List<AppMarketBean> mList,int not_renew, DialogItemChooseListener confirmListener) {
		super(context, R.style.CenterDialogStyle);
		this.listener = confirmListener;
		this.mContext = context;
        this.not_renew = not_renew;
        this.mMarketList = mList;
//        addWBServiceApk();
	}

	private void addWBServiceApk() {
		AppMarketBean obj = new AppMarketBean();
		obj.setMarketName("官网");
		obj.setIconResource(R.mipmap.icon_browser);
		mMarketList.add(obj);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_market_dialog);
		setCanceledOnTouchOutside(not_renew==0?true:false);
		initView();
		initEvent();
		
	}


	private void initView() {
		RelativeLayout rl_parent = findViewById(R.id.rl_parent);
		int screenWidth = AndroidUtil.getScreenWidth(mContext);//屏幕的宽度
		int parentWidth = (int) (screenWidth/5f * 4);//弹出框的宽度
		ViewGroup.LayoutParams layoutParams = rl_parent.getLayoutParams();
		layoutParams.width = parentWidth;
		rl_parent.setLayoutParams(layoutParams);

		recycleView = findViewById(R.id.recycleView);
		mAdapter = new ChooseAppMarketAdapter(mMarketList);
		GridLayoutManager layoutManager = new GridLayoutManager(mContext,5);
		recycleView.setLayoutManager(layoutManager);
		recycleView.setAdapter(mAdapter);
	}
	
	
	private void initEvent() {
		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				listener.onItemChooseClick(mMarketList.get(position));
			}
		});
	}

	/**
	 * 回调接口对象
	 */

	public interface DialogItemChooseListener {

		void onItemChooseClick(AppMarketBean obj);
	}



	@Override
	public void onBackPressed() {

	}
}
