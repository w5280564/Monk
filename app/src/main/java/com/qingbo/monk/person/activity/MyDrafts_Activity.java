package com.qingbo.monk.person.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.HomeInsiderHK_Fragment;
import com.qingbo.monk.base.BaseRecyclerViewSplitActivity;
import com.qingbo.monk.base.BaseTabLayoutActivity;
import com.qingbo.monk.bean.MyDraftsList_Bean;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.fragment.HomeInsider_Fragment;
import com.qingbo.monk.person.adapter.MyDraftsAdapter;
import com.qingbo.monk.person.fragment.MyDrafts_Crate_Fragment;
import com.qingbo.monk.person.fragment.MyDrafts_question_Fragment;
import com.qingbo.monk.question.activity.PublisherQuestionActivity;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.titlebar.CustomTitleBar;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.dialog.TwoButtonDialogBlue;

import java.util.HashMap;

import butterknife.BindView;

/**
 * 我的草稿箱
 */
public class MyDrafts_Activity extends BaseTabLayoutActivity {
    @BindView(R.id.title_bar)
    CustomTitleBar title_bar;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mydrafts;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.card_ViewPager);
        mTabLayout = findViewById(R.id.card_Tab);
        initMenuData();
    }

    @SuppressLint("WrongConstant")
    private void initMenuData() {
        for (int i = 0; i < 2; i++) {
            AppMenuBean bean = new AppMenuBean();
            if (i == 0) {
                fragments.add(MyDrafts_question_Fragment.newInstance("1"));
                bean.setName("问答广场");
            } else {
                fragments.add(MyDrafts_Crate_Fragment.newInstance("2"));
                bean.setName("发布动态");
            }
            menuList.add(bean);
        }

        initViewPager(0);
    }



}