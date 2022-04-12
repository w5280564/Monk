package com.qingbo.monk.home.fragment;

import static com.xunda.lib.common.common.utils.StringUtil.changeShapColor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.activity.InterestDetail_Activity;
import com.qingbo.monk.Slides.activity.SideslipPersonAndFund_Activity;
import com.qingbo.monk.Slides.fragment.SideslipMogul_Fragment;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.ArticleLikedBean;
import com.qingbo.monk.bean.ArticleLikedListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.qingbo.monk.bean.HomeAllCount_Bean;
import com.qingbo.monk.bean.HomeInterestBean;
import com.qingbo.monk.bean.InterestBean;
import com.qingbo.monk.bean.InterestList_Bean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.home.activity.MainActivity;
import com.qingbo.monk.home.adapter.HomeFollow_Adapter;
import com.qingbo.monk.home.adapter.HomeInterest_Adapter;
import com.qingbo.monk.person.activity.MyAndOther_Card;
import com.qingbo.monk.person.adapter.MyFollow_Adapter;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.bean.BaseSplitIndexBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 首页
 */
public class HomeFragment extends BaseTabLayoutFragment implements View.OnClickListener {
    @BindView(R.id.drawer_left_Img)
    ImageView drawer_left_Img;
    @BindView(R.id.change_Tv)
    TextView change_Tv;
    @BindView(R.id.change_Img)
    ImageView change_Img;
    @BindView(R.id.interest_Lin)
    LinearLayout interest_Lin;
    @BindView(R.id.manCount_Tv)
    TextView manCount_Tv;
    @BindView(R.id.seek_Tv)
    TextView seek_Tv;
    @BindView(R.id.listName_Tv)
    TextView listName_Tv;


    private RecyclerView mRecyclerView;
    private HomeInterest_Adapter mAdapter;
    int page = 1;
    int limit = 10;
    private HomeFollow_Adapter mAdapterFollow;
    int defaultTab = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initView(View mRootView) {
//        super.initView(mRootView);
        viewTouchDelegate.expandViewTouchDelegate(drawer_left_Img, 50);
        mTabLayout = mRootView.findViewById(R.id.card_Tab);
        mViewPager = mRootView.findViewById(R.id.card_ViewPager);
        initMenuData();
        mRecyclerView = mRootView.findViewById(R.id.interest_recycler);
        initRecyclerView();
//        initSwipeRefreshLayoutAndAdapter("暂无数据", 0, true);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        drawer_left_Img.setOnClickListener(new drawer_left_ImgClick());
        change_Tv.setOnClickListener(this);
        seek_Tv.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getInterestLab(false);
        if (mTabLayout.getSelectedTabPosition() == 0) {
            followPage =1;
            getExpertList(false);
        } else {
            getListData(false);
        }
        getAllUpdateCount(false);
        ((MainActivity) requireActivity()).changeUser();
    }


    /**
     * 全部兴趣组
     *
     * @param isShow
     */
    InterestList_Bean interestList_bean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", 6 + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_All, "全部兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                interestList_bean = new Gson().fromJson(json_data, InterestList_Bean.class);
                if (interestList_bean != null) {
                    handleSplitListData(interestList_bean, mAdapter, 6, page);
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 我的关注
     *
     * @param isShowAnimal
     */
    ArticleLikedListBean articleLikedListBean;
    int followPage = 1;

    private void getExpertList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", followPage + "");
        requestMap.put("limit", 3 + "");
        HttpSender sender = new HttpSender(HttpUrl.User_Follow_List, "关注列表", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            articleLikedListBean = GsonUtil.getInstance().json2Bean(json_data, ArticleLikedListBean.class);
                            if (articleLikedListBean != null) {
                                handleSplitListData(articleLikedListBean, mAdapterFollow, 3, followPage);
                            }
                        }
                    }

                }, isShowAnimal);

        sender.setContext(mActivity);
        sender.sendGet();
    }


    /**
     * 未读消息
     */
    private void getAllUpdateCount(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender httpSender = new HttpSender(HttpUrl.User_AllUpdate_Count, "新增的总数", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeAllCount_Bean homeAllCount_bean = GsonUtil.getInstance().json2Bean(json_root, HomeAllCount_Bean.class);
                        manCount_Tv.setVisibility(View.GONE);
                    if (!TextUtils.equals(homeAllCount_bean.getData(), "0")) {
                        manCount_Tv.setVisibility(View.VISIBLE);
                        manCount_Tv.setText(homeAllCount_bean.getData());
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_Tv:
                rotate(change_Img);
                getInterestLab(false);
                break;
            case R.id.seek_Tv:
                skipAnotherActivity(HomeSeek_Activity.class);
                break;
        }
    }


    private class drawer_left_ImgClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((MainActivity) requireActivity()).LeftDL();
        }
    }

    /**
     * 我的兴趣组
     */
    public void initRecyclerView() {
        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
        mMangaer.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HomeInterest_Adapter();
        mRecyclerView.setAdapter(mAdapter);
        //  使用加载更多
        mAdapter.setEnableLoadMore(true);
        addMore();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InterestBean item = (InterestBean) adapter.getItem(position);
                InterestDetail_Activity.startActivity(requireActivity(), "0", item.getId());
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.join_Tv:
                        InterestBean item = (InterestBean) adapter.getItem(position);
                        String joinStatus = item.getJoinStatus();
                        if (!TextUtils.equals(joinStatus, "1")) {
                            changeJoin(item.getJoinStatus(), position, item);
                            getJoin(item.getId());
                        }
                        break;
                }
            }
        });
    }

    private void addMore() {
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page += 1;
                getListData(false);
            }
        });
    }


    /**
     * 关注列表
     */
    private void initFollowRecyclerView() {
        mAdapterFollow = new HomeFollow_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapterFollow);
        //  使用加载更多
        mAdapterFollow.setEnableLoadMore(true);
        followAddMore();

        mAdapterFollow.setOnItemClickListener((adapter, view, position) -> {
            ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
            if (item == null) {
                return;
            }
            postDeleArticleData(item.getId(), item);

        });

        mAdapterFollow.setOnItemChildClickListener(new MyFollow_Adapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleLikedBean item = (ArticleLikedBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.join_Tv:
                        String likeId = item.getId();
                        postFollowData(likeId, position);
                        break;

                }
            }
        });
    }

    private void followAddMore() {
        //加载更多
        mAdapterFollow.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                followPage += 1;
                getExpertList(false);
            }
        });
    }

    /**
     * 人物跳转
     * @param item
     */
    private void startPerson(ArticleLikedBean item) {
        String data_source = item.getData_source();//1是虚拟人物,为0是注册用户
        if (TextUtils.equals(data_source, "1")) {
            String nickname = item.getNickname();
            String id = item.getId();
            SideslipPersonAndFund_Activity.startActivity(mActivity, nickname, id, "0");
        }else {
            String id = item.getId();
            MyAndOther_Card.actionStart(mActivity, id);
        }
    }


    private void postFollowData(String otherUserId, int position) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    TextView join_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.join_Tv);
                    mAdapterFollow.isFollow(followStateBena.getFollowStatus(), join_Tv);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }



    @SuppressLint("WrongConstant")
    private void initMenuData() {
        ArrayList<String> tabName = new ArrayList<>();
        tabName.add("关注");
        tabName.add("推荐");
        tabName.add("大咖");
        tabName.add("内部人");
        tabName.add("仓位组合");
        for (int i = 0; i < tabName.size(); i++) {
            AppMenuBean bean = new AppMenuBean();
            bean.setName(tabName.get(i));
            menuList.add(bean);
        }
        String titleType = "";
        fragments.add(HomeFocus_Fragment.newInstance(titleType));
        fragments.add(HomeCommendFragment.newInstance(titleType));
//        tabFragmentList.add(HomeFocus_Fragment.newInstance(titleType));
        fragments.add(SideslipMogul_Fragment.newInstance(""));
        fragments.add(HomeInsider_Fragment.newInstance("1"));
        fragments.add(HomeCombination_Fragment.newInstance(titleType));
        setTabTextSize(16,14);
        initChildViewPager(1);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    listName_Tv.setText("我的关注");
                    initFollowRecyclerView();
                    followPage = 1;
                    getExpertList(false);
                    defaultTab = tab.getPosition();
                } else {
                    if (defaultTab == 0) {
                        listName_Tv.setText("我的兴趣组");
                        initRecyclerView();
                        page = 1;
                        getListData(false);
                        defaultTab = tab.getPosition();
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getJoin(String ID) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("id", ID);
        HttpSender httpSender = new HttpSender(HttpUrl.Join_Group, "加入/退出兴趣组", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {

                }
            }
        }, false);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


    private void rotate(View view) {
        Long animateTime = 500L;
        RotateAnimation animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        //设置无限循环
        animation.restrictDuration(Animation.INFINITE);
        //设置匀速旋转
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(animateTime);
        view.startAnimation(animation);
    }
    /**
     * 全局处理分页的公共方法
     *
     * @param obj      具体的分页对象  列表适配器
     * @param mAdapter
     */
    protected void handleSplitListData(BaseSplitIndexBean obj, BaseQuickAdapter mAdapter, int mPageSize, int page) {
        if (obj != null) {
            int allCount = StringUtil.isBlank(obj.getCount()) ? 0 : Integer.parseInt(obj.getCount());
            int bigPage = 0;//最大页
            if (allCount % mPageSize != 0) {
                bigPage = allCount / mPageSize + 1;
            } else {
                bigPage = allCount / mPageSize;
            }
            if (page == bigPage) {
                mAdapter.loadMoreEnd();//显示“没有更多数据”
            }

            boolean isRefresh = page == 1 ? true : false;
            if (!ListUtils.isEmpty(obj.getList())) {
                int size = obj.getList().size();

                if (isRefresh) {
                    mAdapter.setNewData(obj.getList());
                } else {
                    mAdapter.addData(obj.getList());
                }


                if (size < mPageSize) {
                    mAdapter.loadMoreEnd(isRefresh);//第一页的话隐藏“没有更多数据”，否则显示“没有更多数据”
                } else {
                    mAdapter.loadMoreComplete();
                }
            } else {

                if (isRefresh) {
                    mAdapter.setNewData(null);//刷新列表
                } else {
                    mAdapter.loadMoreEnd(false);//显示“没有更多数据”
                }
            }
        }
    }

    /**
     * 修改加入状态
     *
     * @param stateIndex 1已加入 其他都是未加入
     * @param position
     */
    private void changeJoin(String stateIndex, int position, InterestBean interestBean) {
        if (interestBean != null) {
            TextView join_Tv = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.join_Tv);
            if (TextUtils.equals(stateIndex, "1")) {
                stateIndex = "0";
            } else {
                stateIndex = "1";
            }
            interestBean.setJoinStatus(stateIndex);
            mAdapter.joinState(stateIndex, join_Tv);
        }
    }

    private void getInterestLab(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
//        requestMap.put("page", page + "");
        requestMap.put("limit", "3");
        HttpSender httpSender = new HttpSender(HttpUrl.Interest_Group, "推荐--兴趣组列表", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    HomeInterestBean interestBean = new Gson().fromJson(json_root, HomeInterestBean.class);
                    if (interestBean != null) {
                        labelList(mContext, interest_Lin, interestBean.getData());
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    /**
     * 首页我的兴趣组
     *
     * @param context
     * @param myFlex
     * @param model
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void labelList(Context context, LinearLayout myFlex, HomeInterestBean.DataDTO model) {
        if (myFlex != null) {
            myFlex.removeAllViews();
        } else {
            return;
        }
        int size = model.getList().size();
        for (int i = 0; i < size; i++) {
            View myView = LayoutInflater.from(context).inflate(R.layout.interest_lable, null);
            HomeInterestBean.DataDTO.ListDTO data = model.getList().get(i);
            ImageView head_Tv = myView.findViewById(R.id.head_Tv);
            TextView shares_Tv = myView.findViewById(R.id.shares_Tv);
            TextView add_Tv = myView.findViewById(R.id.add_Tv);
            TextView join_Tv = myView.findViewById(R.id.join_Tv);
            String headUrl = data.getGroupImage();
            GlideUtils.loadCircleImage(context, head_Tv, headUrl, R.mipmap.icon_logo);
            shares_Tv.setText(data.getGroupName());
            add_Tv.setText(data.getJoinNum() + "加入");
            int state = data.getJoinStatus();
            joinState(state, join_Tv);
            myView.setTag(i);
            join_Tv.setTag(i);
            myFlex.addView(myView);
            myView.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                String id = model.getList().get(tag).getId();
                InterestDetail_Activity.startActivity(requireActivity(), "0", id);
            });
            join_Tv.setOnClickListener(v -> {
                int tag = (Integer) v.getTag();
                int stateIndex = model.getList().get(tag).getJoinStatus();
                if (state != 1) {
                    changeState(stateIndex, join_Tv, model);
                    getJoin(model.getList().get(tag).getId());
                }
            });
        }
    }

    //加入的状态
    private void joinState(int state, TextView joinTv) {
        if (state == 1) { //1已加入 其他都是未加入
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        } else {
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        }
    }

    //修改加入状态
    private void changeState(int state, TextView joinTv, HomeInterestBean.DataDTO model) {
        int tag = (Integer) joinTv.getTag();
        if (state == 1) {//1已加入 其他都是未加入
            model.getList().get(tag).setJoinStatus(0);
            joinTv.setText("加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.app_main_color));
        } else {
            model.getList().get(tag).setJoinStatus(1);
            joinTv.setText("已加入");
            joinTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            changeShapColor(joinTv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
        }
    }

    /**
     * 删除查看发文状态
     *
     * @param followId
     */
    private void postDeleArticleData(String followId,ArticleLikedBean item) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("followId", followId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Clear_Article, "删除查看发文状态", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    startPerson(item);
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}
