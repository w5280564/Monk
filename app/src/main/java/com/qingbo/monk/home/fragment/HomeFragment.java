package com.qingbo.monk.home.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.home.activity.MainActivity;

import java.util.Objects;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.btn_drawer_left)
    Button btn_drawer_left;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btn_drawer_left.setOnClickListener(new btn_drawer_leftClick());
    }

    private class btn_drawer_leftClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((MainActivity)requireActivity()).LeftDL();
        }
    }


}
