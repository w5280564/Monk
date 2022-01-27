package com.qingbo.monk.message.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.WebSocketHelper;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity_Single;
import com.qingbo.monk.bean.BaseReceiveMessageBean;
import com.xunda.lib.common.bean.ReceiveMessageBean;
import com.qingbo.monk.message.adapter.ChatAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.ReceiveSocketMessageEvent;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天页
 */
public class ChatActivity extends BaseCameraAndGalleryActivity_Single implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.et_content)
    EditText etContent;
    private List<String> mSensitiveWordList = new ArrayList<>();//敏感词词汇列表
    private ChatAdapter mAdapter;
    private List<ReceiveMessageBean> mList = new ArrayList<>();
    private String id,header,name;
    private int page = 1;
    private int limit = 20;
    private String selfUserId;
    private LinearLayoutManager layoutManager;


    public static void actionStart(Context context, String id,String name,String header) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        intent.putExtra("header",header);
        context.startActivity(intent);
    }




    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }


    @Override
    protected void initLocalData() {
        selfUserId = SharePref.user().getUserId();
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        header = getIntent().getStringExtra("header");
        title = name;

        List<String> objList = GsonUtil.getInstance().parseDataNoKey(SharePref.local().getSensitiveWordsListJson());
        if (!ListUtils.isEmpty(objList)) {
            mSensitiveWordList.clear();
            mSensitiveWordList.addAll(objList);
        }
        registerEventBus();
    }


    @Subscribe
    public void onReceiveSocketMessageEvent(ReceiveSocketMessageEvent event) {
        if(event.type == ReceiveSocketMessageEvent.RECEIVE_MESSAGE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ReceiveMessageBean receiveObj = event.receiveObj;
                    if (receiveObj==null) {
                        return;
                    }
                    receiveObj.setFromHeader(header);
                    if (id.equals(receiveObj.getFrom())) {
                        mAdapter.addData(receiveObj);
                        mRecyclerView.scrollToPosition(mAdapter.getData().size()-1);
                    }
                }
            });
        }
    }

    /**
     * 屏蔽敏感词汇
     * @param sendBeforeMessage
     */
    private String forbidSensitiveWord(String sendBeforeMessage) {
        if (!ListUtils.isEmpty(mSensitiveWordList)) {
            for (int i = 0; i < mSensitiveWordList.size(); i++) {
                if (sendBeforeMessage.contains(mSensitiveWordList.get(i))) {
                    sendBeforeMessage = sendBeforeMessage.replaceAll(mSensitiveWordList.get(i), "***");
                }
            }
            return sendBeforeMessage;
        }
        return "";
    }


    @Override
    protected void initView() {
        super.initView();
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.animal_color));
        layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ReceiveMessageBean mReceiveMessageBean = (ReceiveMessageBean) adapter.getItem(position);
                if (mReceiveMessageBean==null) {
                    return;
                }

                if (ReceiveMessageBean.MESSAGE_TYPE_IMAGE.equals(mReceiveMessageBean.getMsgType())) {
                    jumpToPhotoShowActivitySingle(mReceiveMessageBean.getMessage());
                }
            }
        });


        //这里控制只要点击输入框就滚动到最底部
        etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (layoutManager.findLastCompletelyVisibleItemPosition() < mAdapter.getItemCount() - 1) {
                            layoutManager.setStackFromEnd(true);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            layoutManager.setStackFromEnd(false);
                            mRecyclerView.setLayoutManager(layoutManager);
                        }
                    }
                });
                if (mList.size() > 0) {
                    mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                }
            }
        });

        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String content = StringUtil.getEditText(etContent);
                    if (!StringUtil.isBlank(content)) {
                        sendMessage(content, ReceiveMessageBean.MESSAGE_TYPE_TEXT);
                    }else{
                        T.ss("请输入聊天内容");
                    }
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    protected void getServerData() {
        getMessageList(true);
    }



    /**
     * 聊天记录
     */
    private void getMessageList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userid", id);
        requestMap.put("page", page + "");
        requestMap.put("limit", limit + "");
        HttpSender sender = new HttpSender(HttpUrl.messageList, "聊天记录", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            BaseReceiveMessageBean mObj = GsonUtil.getInstance().json2Bean(json_data, BaseReceiveMessageBean.class);
                            handleSplitListData(mObj);
                        }
                    }

                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }




    /**
     * 分页
     */
    private void handleSplitListData(BaseReceiveMessageBean obj) {
        if (obj != null) {
            List<ReceiveMessageBean> mTempList = obj.getList();
            Collections.reverse(obj.getList());
            if (!ListUtils.isEmpty(mTempList)) {
                for (ReceiveMessageBean mObj:mTempList) {
                    if (!StringUtil.isBlank(mObj.getFrom())) {
                        if (mObj.getFrom().equals(selfUserId)) {
                            mObj.setType(ReceiveMessageBean.CHAT_TYPE_SEND);
                        }else{
                            mObj.setType(ReceiveMessageBean.CHAT_TYPE_RECEIVER);
                            mObj.setFromHeader(header);
                        }
                    }
                }
                if (ListUtils.isEmpty(mAdapter.getData())) {
                    mAdapter.setNewData(mTempList);
                    mRecyclerView.scrollToPosition(mAdapter.getData().size()-1);
                }else{
                    mAdapter.addData(0,mTempList);
                }
            }
        }
    }


    @OnClick({R.id.ll_image,R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_image:
                checkGalleryPermission();
                break;
            case R.id.tv_send:
                String content = StringUtil.getEditText(etContent);
                if (StringUtil.isBlank(content)) {
                    T.ss("请输入聊天内容");
                    return;
                }

                sendMessage(content, ReceiveMessageBean.MESSAGE_TYPE_TEXT);
                break;
        }
    }

    private void sendMessage(String content, String messageTypeText) {
        WebSocketHelper.getInstance().sendMessage(content, messageTypeText, id);
        addContentToList(content, messageTypeText, ReceiveMessageBean.CHAT_TYPE_SEND);
    }


    /**
     * 往消息体里增加数据
     * @param content 消息内容
     * @param chatType 消息类型
     */
    private void addContentToList(String content,String msgType, int chatType) {
        ReceiveMessageBean obj = new ReceiveMessageBean();
        obj.setMessage(content);
        obj.setMsgType(msgType);
        obj.setFrom(id);
        obj.setType(chatType);
        obj.setFromName(name);
        obj.setTime(DateUtil.dateToString(System.currentTimeMillis(), DateUtil.PATTERN_STANDARD19H));
        mAdapter.addData(obj);
        if (ReceiveMessageBean.MESSAGE_TYPE_TEXT.equals(msgType)) {
            etContent.setText("");
        }
        mRecyclerView.scrollToPosition(mAdapter.getData().size()-1);
    }



    @Override
    public void onRightClick() {
        back();
    }

    @Override
    protected void onUploadSuccess(String imageString) {
        sendMessage(imageString, ReceiveMessageBean.MESSAGE_TYPE_IMAGE);
    }

    @Override
    protected void onUploadFailure(String error_info) {

    }


    @Override
    public void onRefresh() {
        page++;
        getMessageList(false);
    }



    // 点击空白区域 自动隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
}

