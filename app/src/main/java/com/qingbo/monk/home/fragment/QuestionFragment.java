package com.qingbo.monk.home.fragment;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.xunda.lib.common.base.NormalFragmentAdapter;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.view.MyViewPagerNoScroll;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 问答
 */
public class QuestionFragment extends BaseFragment {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    MyViewPagerNoScroll mViewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<AppMenuBean> menuList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question;
    }

    @Override
    protected void initLocalData() {
        initMenuData();
    }


    @Override
    protected void initEvent() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在这里可以设置选中状态下  tab字体显示样式
                mViewPager.setCurrentItem(tab.getPosition());
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
        });

    }

    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(new QuestionFragment_Square());
                bean.setName("问答广场");
            } else{
                fragments.add(new QuestionFragment_Group());
                bean.setName("问答社群");
            }
            menuList.add(bean);
        }

        initViewPager();
    }


    private void initViewPager() {
        NormalFragmentAdapter mFragmentAdapterAdapter = new NormalFragmentAdapter(mActivity.getSupportFragmentManager(), fragments, menuList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapterAdapter);
        mViewPager.setOffscreenPageLimit(menuList.size());
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
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
