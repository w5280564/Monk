package com.qingbo.monk.question.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.banner.widget.Banner.base.BaseBanner;
import com.google.gson.reflect.TypeToken;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseLazyFragment;
import com.qingbo.monk.bean.BaseGroupBean;
import com.qingbo.monk.bean.MyGroupBean;
import com.qingbo.monk.bean.GroupBean;
import com.qingbo.monk.bean.MyGroupList_Bean;
import com.qingbo.monk.person.activity.MyGroupList_Activity;
import com.qingbo.monk.person.adapter.MyGroupAdapter;
import com.qingbo.monk.question.activity.AllGroupListActivity;
import com.qingbo.monk.question.activity.CheckOtherGroupDetailActivity;
import com.qingbo.monk.question.activity.CreateGroupStepOneActivity;
import com.qingbo.monk.question.adapter.QuestionGroupAdapter;
import com.qingbo.monk.view.banner.QuestionGroupBanner;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.PrefUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群问答
 */
public class QuestionFragment_Group extends BaseLazyFragment {
    @BindView(R.id.img_top_banner)
    QuestionGroupBanner img_top_banner;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.group_Recycler)
    RecyclerView group_Recycler;
    @BindView(R.id.ll_top_right)
    LinearLayout ll_top_right;
    private QuestionGroupAdapter mQuestionGroupAdapter;
    private List<MyGroupBean> bannerList = new ArrayList<>();
    private MyGroupAdapter myGroupAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_group;
    }


    @Override
    protected void initView() {
        initGroupRecycler();
        initRecyclerView();
        registerEventBus();
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if (event.type == FinishEvent.CREATE_GROUP) {
            getMyGroup();
        } else if (event.type == FinishEvent.JOIN_GROUP || event.type == FinishEvent.EXIT_GROUP) {
            getAllShequn();
        }
    }

    /**
     * 我的社群
     */
    private void initGroupRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        group_Recycler.setLayoutManager(gridLayoutManager);
        myGroupAdapter = new MyGroupAdapter(true);
        group_Recycler.setAdapter(myGroupAdapter);
    }

    private void initRecyclerView() {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.layout_group_bottom, null);
        TextView tv_more = itemView.findViewById(R.id.tv_more);
        mQuestionGroupAdapter = new QuestionGroupAdapter();
        mQuestionGroupAdapter.addFooterView(itemView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mQuestionGroupAdapter);

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipAnotherActivity(AllGroupListActivity.class);
            }
        });
    }

    @Override
    protected void initEvent() {
        img_top_banner.setOnItemClickL(new BaseBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
//                skipAnotherActivity(MyGroupListActivity.class);
                String userID = PrefUtil.getUser().getId();
                MyGroupList_Activity.actionStart(mActivity, userID);
            }
        });

        mQuestionGroupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupBean mGroupBean = (GroupBean) adapter.getItem(position);
                if (mGroupBean == null) {
                    return;
                }
                CheckOtherGroupDetailActivity.actionStart(mActivity, mGroupBean.getId());
            }
        });
    }

    @Override
    protected void loadData() {
        getAllShequn();
//        getMyGroupHead("1");
    }

    private void getAllShequn() {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", "1");
        requestMap.put("limit", "4");
        HttpSender sender = new HttpSender(HttpUrl.allGroup, "全部社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseGroupBean obj = GsonUtil.getInstance().json2Bean(json_data, BaseGroupBean.class);
                            if (obj != null) {
                                List<GroupBean> list = obj.getList();
                                mQuestionGroupAdapter.setNewData(list);
                            }
                        }
                        getMyGroup();
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void getMyGroup() {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.myGroup, "我的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            handleData(json_root);
                        }
                    }

                }, true);

        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData(String json) {
        HttpBaseList<MyGroupBean> objList = GsonUtil.getInstance().json2List(json, new TypeToken<HttpBaseList<MyGroupBean>>() {
        }.getType());
        handleBanner(objList.getData());
    }


    private void handleBanner(List<MyGroupBean> tempBannerList) {
        if (!ListUtils.isEmpty(tempBannerList)) {
            bannerList.clear();
            bannerList.addAll(tempBannerList);
            img_top_banner.placeholder(R.mipmap.img_pic_none_square).setSource(bannerList).startScroll();
        } else {
            img_top_banner.setBackgroundResource(R.mipmap.img_pic_none_square);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        img_top_banner.pauseScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        img_top_banner.goOnScroll();
    }


    @OnClick({R.id.tv_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_create:
                skipAnotherActivity(CreateGroupStepOneActivity.class);
                break;
        }
    }

    //我创建的群
    private void getMyGroupHead(String type) {
        String id = PrefUtil.getUser().getId();
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userid", id);
        requestMap.put("type", type);
        HttpSender sender = new HttpSender(HttpUrl.My_SheQun_Pc, "我创建的社群", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            MyGroupList_Bean myGroupList_bean = GsonUtil.getInstance().json2Bean(json_data, MyGroupList_Bean.class);
                            myGroupAdapter.addData(myGroupList_bean.getList());
//                            handleSplitListData(myGroupList_bean, myGroupAdapter, limit);
                        }
                    }

                }, false);

        sender.setContext(mActivity);
        sender.sendGet();
    }


}
