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
    private TextView tv_submit_manager, tv_submit_partner,tv_join_time, tv_name;
    private ImageView iv_header;
    private View line_partner;
    private String submit_type;//1添加管理员 2添加合伙人 3设置为一般用户
    private String role;

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
        line_partner = findViewById(R.id.line_partner);
        iv_header = findViewById(R.id.iv_header);
        tv_submit_manager = findViewById(R.id.tv_submit_manager);
        tv_submit_partner = findViewById(R.id.tv_submit_partner);
        tv_join_time = findViewById(R.id.tv_join_time);
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(StringUtil.isBlank(obj.getNickname())?"":obj.getNickname());
        tv_join_time.setText(StringUtil.isBlank(obj.getCreateTime())?"":obj.getCreateTime()+"  加入");
        GlideUtils.loadCircleImage(mContext, iv_header, obj.getAvatar());

        role = obj.getRole();
        if ("0".equals(role)) {//1管理员2合伙人0一般用户3群主
            tv_submit_manager.setVisibility(View.VISIBLE);
            tv_submit_partner.setVisibility(View.VISIBLE);
            line_partner.setVisibility(View.VISIBLE);
        }else if("2".equals(role)) {//合伙人
            tv_submit_manager.setVisibility(View.GONE);
            tv_submit_partner.setVisibility(View.VISIBLE);
            line_partner.setVisibility(View.GONE);
            tv_submit_partner.setText("取消合伙人");
        }else{//管理员
            tv_submit_partner.setVisibility(View.GONE);
            tv_submit_manager.setVisibility(View.VISIBLE);
            line_partner.setVisibility(View.GONE);
            tv_submit_manager.setText("取消管理员");
        }
    }

    private void initEvent() {
        tv_submit_manager.setOnClickListener(this);
        tv_submit_partner.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    /**
     * 回调接口对象
     */
    public interface ConfirmListener {
        void onSet(String user_id,String type,String current_personal_role);
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.tv_cancel) {
            dismiss();
        } else if (id == R.id.tv_submit_manager) {
            if("1".equals(role)) {
                submit_type ="3";
            }else{
                submit_type ="1";
            }
            listener.onSet(obj.getId(),submit_type,role);
            dismiss();
        } else if (id == R.id.tv_submit_partner) {
            if("2".equals(role)) {
                submit_type ="3";
            }else{
                submit_type ="2";
            }
            listener.onSet(obj.getId(),submit_type,role);
            dismiss();
        }
    }
}
