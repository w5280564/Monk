package com.qingbo.monk.message.activity;

import android.view.View;
import android.widget.EditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseCameraAndGalleryActivity;
import com.qingbo.monk.bean.ServiceChatBean;
import com.qingbo.monk.bean.ServiceChatIndexBean;
import com.qingbo.monk.message.adapter.ChatAdapter;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpBaseList;
import com.xunda.lib.common.common.http.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天页
 */
public class ChatActivity extends BaseCameraAndGalleryActivity{

    @BindView(R.id.recycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    private List<String> mSensitiveWordList = new ArrayList<>();//敏感词词汇列表
    private ChatAdapter mAdapter;
    private List<ServiceChatIndexBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }


    @Override
    protected void initLocalData() {
        List<String> objList = GsonUtil.getInstance().parseDataNoKey(SharePref.local().getSensitiveWordsListJson());
        if (!ListUtils.isEmpty(objList)) {
            mSensitiveWordList.clear();
            mSensitiveWordList.addAll(objList);
        }

        addContentToList("hello world", ServiceChatIndexBean.CHAT_TYPE_ANSWER);
        addContentToList("下班了", ServiceChatIndexBean.CHAT_TYPE_SEND);
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)     //状态栏颜色，不写默认透明色
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void getServerData() {

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





    @OnClick({R.id.ll_image,R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_image:
                checkGalleryPermission(1);
                break;
            case R.id.tv_send:
                String content = StringUtil.getEditText(etContent);
                if (StringUtil.isBlank(content)) {
                    T.ss("请输入聊天内容");
                    return;
                }

                addContentToList(forbidSensitiveWord(content), ServiceChatIndexBean.CHAT_TYPE_SEND);
                mAdapter.notifyDataSetChanged();

                sendQuestion(content);
                break;
        }
    }



    /**
     * 发送问题请求
     */
    private void sendQuestion(String keyword) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("keyword",keyword);
        HttpSender sender = new HttpSender(HttpUrl.sendQuestion, "发送问题请求",
                map, new MyOnHttpResListener() {

            @Override
            public void onComplete(String json, int status, String description, String data) {
                if (status == Constants.REQUEST_SUCCESS_CODE) {
                    etContent.setText("");
                    handleData(json);
                } else {
                    T.ss(description);
                }
            }

        }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }


    private void handleData(String json) {
        HttpBaseList<ServiceChatBean> objList = GsonUtil
                .getInstance()
                .json2List(
                        json,
                        new TypeToken<HttpBaseList<ServiceChatBean>>() {
                        }.getType());
        if (!ListUtils.isEmpty(objList.getData())) {
            ServiceChatIndexBean obj = new ServiceChatIndexBean();
            obj.setList(objList.getData());
            obj.setType(ServiceChatIndexBean.CHAT_TYPE_ANSWER);
            mList.add(obj);
        }else{
            addContentToList("暂无客服", ServiceChatIndexBean.CHAT_TYPE_ANSWER);
        }

        mAdapter.notifyDataSetChanged();
    }


    /**
     * 往消息体里增加数据
     * @param content 消息内容
     * @param chatType 消息类型
     */
    private void addContentToList(String content, int chatType) {
        ServiceChatIndexBean obj = new ServiceChatIndexBean();

        List<ServiceChatBean> tempList = new ArrayList<>();
        ServiceChatBean littleObj = new ServiceChatBean();
        littleObj.setContent(content);
        tempList.add(littleObj);

        obj.setList(tempList);
        obj.setType(chatType);
        mList.add(obj);
    }



    @Override
    public void onRightClick() {
        back();
    }
}

