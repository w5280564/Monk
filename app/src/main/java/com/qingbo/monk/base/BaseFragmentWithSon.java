package com.qingbo.monk.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 含有Fragment的Fragment的基类
 * @author ouyang
 *
 */
public class BaseFragmentWithSon extends BaseFragment{
	protected List<BaseFragment> mFragmentList = new ArrayList<BaseFragment>();
	private int cuurent = 0x001;
	
	/** 添加fragment,必须继承BaseFragment */
	protected void addFragment(BaseFragment fragment) {
		if (mFragmentList == null) {
			mFragmentList = new ArrayList<BaseFragment>();
		}
		mFragmentList.add(fragment);
	}

	protected void showFragment(int index, int fragmentId) {

		if (cuurent != 0x001 && getCurrentFrl() == mFragmentList.get(index)) {
			return;
		}
		FragmentManager manage = mActivity.getSupportFragmentManager();
		FragmentTransaction transaction = manage.beginTransaction();
		BaseFragment frl = mFragmentList.get(index);
		if (frl.isAdded()) {
			frl.onResume();
		} else {
			transaction.add(fragmentId, frl);
		}

		for (int i = 0; i < mFragmentList.size(); i++) {
			Fragment fragment = mFragmentList.get(i);
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			if (index == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commitAllowingStateLoss();
		}
		transaction.commitAllowingStateLoss();
		cuurent = index;
	}

	protected BaseFragment getCurrentFrl() {

		return mFragmentList.get(cuurent);
	}

	@Override
	protected int getLayoutId() {
		return 0;
	}
}
