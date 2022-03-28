package com.qingbo.monk.home.fragment;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.Slides.fragment.SideslipFind_Card_Fragment;
import com.qingbo.monk.base.BaseTabLayoutFragment;
import com.qingbo.monk.bean.System_MesCount_Bean;
import com.qingbo.monk.home.activity.HomeSeek_Activity;
import com.qingbo.monk.message.activity.ContactListActivity;
import com.qingbo.monk.person.activity.Person_system;
import com.xunda.lib.common.bean.AppMenuBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会话列表
 */
public class MessageFragment extends BaseTabLayoutFragment {
    @BindView(R.id.system_msg_Tv)
    TextView system_msg_Tv;

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

    @OnClick({R.id.seek_Tv, R.id.ll_contact, R.id.system_Con})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.system_Con:
                skipAnotherActivity(Person_system.class);
                break;
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
            } else {
                bean.setName("好友消息");
                fragments.add(new Message_List_Fragment());
            }
            menuList.add(bean);
        }
        initChildViewPager(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        getNumList(false);
    }

    /**
     * 消息页面 有消息未查看
     *
     * @param isShowAnimal
     */
    private void getNumList(boolean isShowAnimal) {
        HashMap<String, String> requestMap = new HashMap<>();
        HttpSender sender = new HttpSender(HttpUrl.System_Mes_Count, "系统消息数", requestMap,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {
                            System_MesCount_Bean system_mesCount_bean = GsonUtil.getInstance().json2Bean(json_data, System_MesCount_Bean.class);
                            String messageCount = system_mesCount_bean.getMessageCount();
                            String systemCount = system_mesCount_bean.getSystemCount();
                            if (TextUtils.equals(messageCount, "0")) {
                                unread_msg_Tv.setVisibility(View.GONE);
                            } else {
                                if (Objects.equals(Objects.requireNonNull(mTabLayout.getTabAt(1)).getText(), "好友消息")) {
                                    unread_msg_Tv.setVisibility(View.VISIBLE);
                                }
                            }
                            if (TextUtils.equals(systemCount, "0")) {
                                system_msg_Tv.setVisibility(View.GONE);
                            } else {
                                system_msg_Tv.setVisibility(View.VISIBLE);
                                int count = Integer.parseInt(systemCount);
                                system_msg_Tv.setText(handleUnreadNum(count));
                            }

                        }
                    }
                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private String handleUnreadNum(int unreadMsgCount) {
        if (unreadMsgCount <= 99) {
            return String.valueOf(unreadMsgCount);
        } else {
            return "99+";
        }
    }

}
