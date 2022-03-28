package com.qingbo.monk.Slides.fragment;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.widget.StackCardsView;
import com.qingbo.monk.Slides.widget.carditem.BaseCardItem;
import com.qingbo.monk.Slides.widget.carditem.CardAdapter;
import com.qingbo.monk.Slides.widget.carditem.ImageCardItem;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.FindBean;
import com.qingbo.monk.bean.FindListBean;
import com.qingbo.monk.bean.FollowStateBena;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 侧滑-关注-发现
 */
public class SideslipFind_Card_Fragment extends BaseFragment implements StackCardsView.OnCardSwipedListener, View.OnClickListener {
    private String type;
    private int page = 1;
    private StackCardsView mCardsView;

    private CardAdapter mAdapter;
    private volatile int mStartIndex;
    private static final int PAGE_COUNT = 10;

    @BindView(R.id.cha_Img)
    ImageView cha_Img;
    @BindView(R.id.love_Img)
    ImageView love_Img;

    /**
     * @return
     */
    public static SideslipFind_Card_Fragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        SideslipFind_Card_Fragment fragment = new SideslipFind_Card_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initLocalData() {
        super.initLocalData();
        type = getArguments().getString("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_card;
    }

    @Override
    protected void initView(View mView) {
        mCardsView = mView.findViewById(R.id.cards);
        mCardsView.addOnCardSwipedListener(this);
        mAdapter = new CardAdapter();
        mCardsView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
//        getListData(true);
    }

    @Override
    protected void initEvent() {
        cha_Img.setOnClickListener(this);
        love_Img.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        super.getServerData();
        getListData(true);
    }

    FindListBean findListBean;

    private void getListData(boolean isShow) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("page", page + "");
        requestMap.put("limit", 10 + "");
        HttpSender httpSender = new HttpSender(HttpUrl.Recommend_Follow, "关注—发现-推荐关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    findListBean = GsonUtil.getInstance().json2Bean(json_data, FindListBean.class);
                    if (findListBean != null) {
                        loadData(mStartIndex);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCardsView.removeOnCardSwipedListener(this);
        mStartIndex = 0;
    }

    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
        if (mAdapter.getCount() < 3) {
            page++;
            getServerData();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCardScrolled(View view, float progress, int direction) {
        Log.d(TAG, "onCardScrolled: view=" + view.hashCode() + ", progress=" + progress + ",direction=" + direction);
        Object tag = view.getTag();
        if (tag instanceof ImageCardItem.ViewHolder) {
            ImageCardItem.ViewHolder vh = (ImageCardItem.ViewHolder) tag;
            if (progress > 0) {
                switch (direction) {
                    case StackCardsView.SWIPE_LEFT:
                        vh.left.setAlpha(progress);
                        vh.right.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_RIGHT:
                        vh.right.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_UP:
                        vh.up.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.right.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_DOWN:
                        vh.down.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.right.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        break;
                }
            } else {
                vh.left.setAlpha(0f);
                vh.right.setAlpha(0f);
                vh.up.setAlpha(0f);
                vh.down.setAlpha(0f);
            }
        }
    }

    private int sizeOfImage(List<BaseCardItem> items) {
        if (items == null) {
            return 0;
        }
        int size = 0;
        for (BaseCardItem item : items) {
            if (item instanceof ImageCardItem) {
                size++;
            }
        }
        return size;
    }

    /**
     * 加载卡片数据
     *
     * @param startIndex
     * @return
     */
    private List<BaseCardItem> loadData(int startIndex) {
        int size = findListBean.getList().size();
        List<BaseCardItem> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FindBean findBean = findListBean.getList().get(i);
            ImageCardItem item = new ImageCardItem(getActivity(),findBean);
            item.dismissDir = StackCardsView.SWIPE_ALL;
            item.fastDismissAllowed = true;
            result.add(item);
        }
        addMore(result);
        return result;
    }

    /**
     * 加载更多
     *
     * @param data
     */
    private void addMore(List<BaseCardItem> data) {
        mAdapter.appendItems(data);
        mStartIndex += sizeOfImage(data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cha_Img:
                mCardsView.removeCover(StackCardsView.SWIPE_LEFT);
                break;
            case R.id.love_Img:
                mCardsView.removeCover(StackCardsView.SWIPE_RIGHT);
                List<BaseCardItem> baseCardItems = mAdapter.getmItems();
                ImageCardItem baseCardItem1 = (ImageCardItem) baseCardItems.get(0);
                String id = baseCardItem1.getFindBean().getId();
                postFollowData(id);
                break;
        }
    }

    private void postFollowData(String otherUserId) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("otherUserId", otherUserId + "");
        HttpSender httpSender = new HttpSender(HttpUrl.User_Follow, "关注-取消关注", requestMap, new MyOnHttpResListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(String json_root, int code, String msg, String json_data) {
                if (code == Constants.REQUEST_SUCCESS_CODE) {
                    FollowStateBena followStateBena = GsonUtil.getInstance().json2Bean(json_data, FollowStateBena.class);
                    if (followStateBena != null){
                    }
                }
            }
        }, true);
        httpSender.setContext(mActivity);
        httpSender.sendPost();
    }


}
