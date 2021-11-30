package com.qingbo.monk.home.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivityWithFragment;
import com.qingbo.monk.home.fragment.HomeFragment;
import com.qingbo.monk.home.fragment.MessageFragment;
import com.qingbo.monk.home.fragment.MineFragment;
import com.qingbo.monk.home.fragment.QuestionFragment;
import com.qingbo.monk.home.fragment.UniverseFragment;

import butterknife.BindView;

/**
 * 主首页
 */
public class MainActivity extends BaseActivityWithFragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.mBottomNavigationView)
    BottomNavigationView mBottomNavigationView;
    private int fragmentId = R.id.act_main_fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        initFragment();
        mBottomNavigationView.setItemIconTintList(null);
    }

    private void initFragment() {
        addFragment(new HomeFragment());
        addFragment(new QuestionFragment());
        addFragment(new UniverseFragment());
        addFragment(new MessageFragment());
        addFragment(new MineFragment());
        showFragment(0, fragmentId);
    }

    @Override
    protected void initEvent() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.em_main_home:
                invalidateOptionsMenu();
                showFragment(0, fragmentId);
                break;
            case R.id.em_main_questions:
                showFragment(1, fragmentId);
                break;
            case R.id.em_main_universe:
                showFragment(2, fragmentId);
                break;
            case R.id.em_main_message:
                showFragment(3, fragmentId);
                break;
            case R.id.em_main_mine:
                showFragment(4, fragmentId);
                break;
        }

        return true;
    }
}