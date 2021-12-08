package com.qingbo.monk.base;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * tablayout的基类
 */
public class BaseTabLayoutFragment extends BaseFragment {
    protected List<Fragment> fragments = new ArrayList<>();
    protected List<AppMenuBean> menuList = new ArrayList<>();
    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initEvent() {
        mTabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
    }





    public class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {


        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            View view = tab.getCustomView();
            if (null != view) {
                setTextViewStyle(view, 18, R.color.text_color_444444, Typeface.DEFAULT_BOLD, View.VISIBLE);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            View view = tab.getCustomView();
            if (null != view) {
                setTextViewStyle(view, 15, R.color.text_color_a1a1a1, Typeface.DEFAULT, View.INVISIBLE);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


    protected void initViewPager(int position) {
        NormalFragmentAdapter mFragmentAdapter = new NormalFragmentAdapter(mActivity.getSupportFragmentManager(), fragments, menuList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size());
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }

        View view = mTabLayout.getTabAt(0).getCustomView();
        if (null != view) {
            setTextViewStyle(view, 18, R.color.text_color_444444, Typeface.DEFAULT_BOLD, View.VISIBLE);
        }

        mViewPager.setCurrentItem(0);
    }



    private void setTextViewStyle(View view, int size, int color, Typeface textStyle,int visibility) {
        TextView mTextView = view.findViewById(R.id.tab_item_textview);
        View line = view.findViewById(R.id.line);
        mTextView.setTextSize(size);
        mTextView.setTextColor(ContextCompat.getColor(mContext, color));
        mTextView.setTypeface(textStyle);
        line.setVisibility(visibility);
    }


    /**
     * 自定义Tab的View
     * @param currentPosition
     * @return
     */
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab, null);
        TextView textView = view.findViewById(R.id.tab_item_textview);
        textView.setText(menuList.get(currentPosition).getName());
        return view;
    }

}
