package com.qingbo.monk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qingbo.monk.bean.GroupMemberBean;
import com.xunda.lib.common.R;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.utils.StringUtil;

/**
 * 设置或取消管理员弹窗
 */
public class SetManagerDialog extends Dialog implements View.OnClickListener {

    private ConfirmListener listener;
    private Context mContext;
    private GroupMemberBean obj;
    private TextView tv_submit, tv_join_time, tv_name;
    private ImageView iv_header;
    private String submit_type;//1添加管理员 2添加合伙人 3设置为一般用户

    public SetManagerDialog(Context context, GroupMemberBean obj, ConfirmListener confirmListener) {
        super(context, R.style.bottomrDialogStyle);
        this.listener = confirmListener;
        this.mContext = context;
        this.obj = obj;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_manager);
        setCanceledOnTouchOutside(true);
        initView();
        initEvent();

    }

    private void initView() {
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);// 弹出框在底部位置
        iv_header = findViewById(R.id.iv_header);
        tv_submit = findViewById(R.id.tv_submit);
        tv_join_time = findViewById(R.id.tv_join_time);
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(StringUtil.isBlank(obj.getNickname())?"":obj.getNickname());
        tv_join_time.setText(StringUtil.isBlank(obj.getCreateTime())?"":obj.getCreateTime()+"  加入");
        GlideUtils.loadCircleImage(mContext, iv_header, obj.getAvatar());

        String role = obj.getRole();
        if ("0".equals(role)) {//1管理员2合伙人0一般用户3群主
            tv_submit.setText("设置成管理员");
            submit_type ="1";//1添加管理员 2添加合伙人 3设置为一般用户
        }else if("2".equals(role)) {
            tv_submit.setText("取消合伙人");
            submit_type ="3";
        }else{
            tv_submit.setText("取消管理员");
            submit_type ="3";
        }
    }

    private void initEvent() {
        tv_submit.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */

    public interface ConfirmListener {
        void onSet(String user_id,String type);
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_submit) {
            listener.onSet(obj.getId(),submit_type);
            dismiss();
        }
    }
}
