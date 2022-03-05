package com.qingbo.monk.home.fragment;

import android.view.View;

import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.SideslipFind_Card_Fragment;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.message.activity.ContactListActivity;
import com.xunda.lib.common.bean.AppMenuBean;

import butterknife.OnClick;

/**
 * 会话列表
 */
public class MessageFragment extends BaseTabLayoutFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }
    @Override
    protected void initView(View mView) {
        mViewPager = mView.findViewById(R.id.viewpager);
        mTabLayout = mView.findViewById(R.id.tabs);
        initMenuData();
    }

    @OnClick({R.id.seek_Tv, R.id.ll_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seek_Tv:
                skipAnotherActivity(HomeSeek_Activity.class);
                break;
            case R.id.ll_contact:
                skipAnotherActivity(ContactListActivity.class);
                break;
        }
    }

    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                bean.setName("发现");
                fragments.add(SideslipFind_Card_Fragment.newInstance("1"));
            } else{
                bean.setName("好友消息");
                fragments.add(new Message_List_Fragment());
            }
            menuList.add(bean);
        }
        initChildViewPager(0);
    }

}
