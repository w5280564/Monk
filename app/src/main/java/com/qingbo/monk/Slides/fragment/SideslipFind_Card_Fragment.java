package com.qingbo.monk.Slides.fragment;

import static android.content.ContentValues.TAG;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.widget.StackCardsView;
import com.qingbo.monk.Slides.widget.adapter.BaseCardItem;
import com.qingbo.monk.Slides.widget.adapter.CardAdapter;
import com.qingbo.monk.Slides.widget.adapter.ImageCardItem;
import com.qingbo.monk.Slides.widget.adapter.ImageUrls;
import com.qingbo.monk.base.BaseFragment;
import com.qingbo.monk.bean.FindListBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 侧滑-关注-发现
 */
public class SideslipFind_Card_Fragment extends BaseFragment implements StackCardsView.OnCardSwipedListener, Handler.Callback {
    private String type;
    private int page = 1;
    private StackCardsView mCardsView;
    private Handler mMainHandler;

    private static final int MSG_START_LOAD_DATA = 1;
    private static final int MSG_DATA_LOAD_DONE = 2;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private CardAdapter mAdapter;
    private volatile int mStartIndex;
    private static final int PAGE_COUNT = 10;

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
//        initRecyclerView();
//        initSwipeRefreshLayoutAndAdapter("暂无数据",false);
        mCardsView = mView.findViewById(R.id.cards);
        mCardsView.addOnCardSwipedListener(this);
        mAdapter = new CardAdapter();
        mCardsView.setAdapter(mAdapter);
//        mMainHandler = new Handler(this);
//        mWorkThread = new HandlerThread("data_loader");
//        mWorkThread.start();
//        mWorkHandler = new Handler(mWorkThread.getLooper(), this);
//        mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();
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
//                        handleSplitListData(findListBean, mAdapter, limit);
//                        addCardViw();
                        loadData(mStartIndex);
                    }
                }
            }
        }, isShow);
        httpSender.setContext(mActivity);
        httpSender.sendGet();
    }


    public void initRecyclerView() {
//        LinearLayoutManager mMangaer = new LinearLayoutManager(mContext);
//        mMangaer.setOrientation(RecyclerView.VERTICAL);
//        mRecyclerView.setLayoutManager(mMangaer);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        mRecyclerView.setHasFixedSize(true);
//        mAdapter = new Find_Adapter();
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                HomeInsiderBean item = (HomeInsiderBean) adapter.getItem(position);
//                String newsUuid = item.getNewsUuid();
//                AAndHKDetail_Activity.startActivity(requireActivity(),newsUuid,"0","0");
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCardsView.removeOnCardSwipedListener(this);
//        mWorkThread.quit();
//        mWorkHandler.removeMessages(MSG_START_LOAD_DATA);
//        mMainHandler.removeMessages(MSG_DATA_LOAD_DONE);
        mStartIndex = 0;
    }

    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
        if (mAdapter.getCount() < 3) {
//            if (!mWorkHandler.hasMessages(MSG_START_LOAD_DATA)) {
//                mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();
//            }
            page++;
            getServerData();
        }
        if (mAdapter.getCount() == 5){
            mAdapter.notifyDataSetChanged();
        }
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

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MSG_START_LOAD_DATA: {
                List<BaseCardItem> data = loadData(mStartIndex);
                mMainHandler.obtainMessage(MSG_DATA_LOAD_DONE, data).sendToTarget();
                break;
            }
            case MSG_DATA_LOAD_DONE: {
                List<BaseCardItem> data = (List<BaseCardItem>) msg.obj;
                mAdapter.appendItems(data);
                mStartIndex += sizeOfImage(data);
                break;
            }
        }
        return true;
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

    private List<BaseCardItem> loadData(int startIndex) {
//        if (startIndex < ImageUrls.images.length) {
        int size = findListBean.getList().size();
        final int endIndex = Math.min(mStartIndex + PAGE_COUNT, size);
        L.d(startIndex + "");
        L.d(endIndex + "");

        List<BaseCardItem> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String avatar = findListBean.getList().get(i).getAvatar();
            String nickname = findListBean.getList().get(i).getNickname();
//
//                ImageCardItem item = new ImageCardItem(getActivity(), ImageUrls.images[i], ImageUrls.labels[i]);
            ImageCardItem item = new ImageCardItem(getActivity(), avatar, nickname);
            item.dismissDir = StackCardsView.SWIPE_ALL;
            item.fastDismissAllowed = true;
            result.add(item);
        }
        mAdapter.appendItems(result);
        mStartIndex += sizeOfImage(result);
//            if (startIndex == 0) {
//                ScrollCardItem item = new ScrollCardItem(getActivity());
//                result.add(result.size() / 2, item);
//            }
        return result;
//        }
//        return null;
    }


}
