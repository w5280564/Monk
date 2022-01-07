package com.qingbo.monk.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.bean.MessageRecordBean;
import com.qingbo.monk.bean.ReceiveMessageBean;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * ================================================
 *
 * @Description:会话列表
 * <p>
 * ================================================
 */
public class MessageListAdapter extends BaseQuickAdapter<MessageRecordBean, BaseViewHolder> {
    public MessageListAdapter() {
        super(R.layout.item_message_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageRecordBean item) {
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_content = helper.getView(R.id.tv_content);
        TextView tv_unread_msg_number = helper.getView(R.id.tv_unread_msg_number);
        ImageView iv_header = helper.getView(R.id.iv_header);
        helper.setText(R.id.tv_name,item.getNickname());

        int unreadNum = item.getUnreadNum();
        if (unreadNum==0) {
            tv_unread_msg_number.setVisibility(View.GONE);
        }else{
            tv_unread_msg_number.setVisibility(View.VISIBLE);
            tv_unread_msg_number.setText(handleUnreadNum(unreadNum));
        }

        ReceiveMessageBean lastMsg = item.getLastMsg();
        if (lastMsg!=null) {
            tv_time.setText(StringUtil.getStringValue(lastMsg.getTime()));
            if ("text".equals(lastMsg.getMsgType())) {
                tv_content.setText(StringUtil.getStringValue(lastMsg.getMessage()));
            }else{
                tv_content.setText("[图片]");
            }
        }else{
            tv_time.setText("");
            tv_content.setText("");
        }
        GlideUtils.loadCircleImage(mContext, iv_header, item.getAvatar());
    }

    private String handleUnreadNum(int unreadMsgCount) {
        if(unreadMsgCount <= 99) {
            return String.valueOf(unreadMsgCount);
        }else {
            return "99+";
        }
    }
}
